#!/bin/bash

set -eo pipefail
# Get current ZPROD private IP address
read ZPROD_IP < <(aws ec2 describe-instances | jq '.Reservations.[] | .Instances.[] | select(.Tags.[] | .Key=="Name" and .Value=="zprod.db.usu.edu") | .PrivateIpAddress' -r)
source copilot_apps.sh

# Update certificate ARN
read arn < <(aws acm list-certificates | jq '[.CertificateSummaryList.[] | select(.DomainName=="banner.usu.edu" and .Status=="ISSUED") | .CertificateArn][0]' -r)
yq -Y '.http.public.certificates |= ["'"${arn}"'"]' -i copilot/environments/recovery/manifest.yml

copilot app init banner
copilot init -t "Load Balanced Web Service" -n static --port 80 --deploy no --env recovery

# Initialize AWS apps
for aws_appname in "${!apps[@]}"; do
	copilot init -t "Load Balanced Web Service" -n "${aws_appname}" --port 8080 --deploy no --env recovery
done

# Update manifest files
for aws_appname in "${!apps[@]}"; do
	image="usuit/banner:${apps[${aws_appname}],,}-zprod-latest"
	yq -Y ".image.location |= \"$image\"" -i "copilot/${aws_appname}/manifest.yml"
	yq -Y ".variables.BANNERDB_JDBC |= \"jdbc:oracle:thin:@//$ZPROD_IP:1521/zprod\"" -i "copilot/${aws_appname}/manifest.yml"
done

# This step requires human intervention!!!
cat <<EOF
This next step requires human interaction. Please do the following:

1. Name the environment 'recovery'
2. Select 'Profile default'
3. Select 'No, I'd like to import existing resources (VPC, subnets)'
4. Select these public subnets:
  - subnet-09f6b50a17739ef72 (USUIT-ecs-pub-usw2a)
  - subnet-0daa4e259d191a83e (USUIT-ecs-pub-usw2b)
5. Select these private subnets:
  - subnet-06966a19a24235bcb (USUIT-ecs-priv-usw2b)
  - subnet-003245c9fd29658a8 (USUIT-ecs-priv-usw2a)

EOF
copilot env init --import-cert-arns "${arn}" --name recovery

copilot env deploy --name recovery

EFS_ID=fs-07e9d58f380a486b1

SUBNETS=$(aws cloudformation describe-stacks --stack-name banner-recovery | jq '.Stacks[] | .Outputs[] | select(.OutputKey == "PublicSubnets") | .OutputValue')
SUBNET1=$(echo $SUBNETS | jq -r 'split(",") | .[0]')
SUBNET2=$(echo $SUBNETS | jq -r 'split(",") | .[1]')
ENV_SG=$(aws cloudformation describe-stacks --stack-name banner-recovery | jq -r '.Stacks[] | .Outputs[] | select(.OutputKey == "EnvironmentSecurityGroup") | .OutputValue')

MOUNT_TARGET_1_ID=$(aws efs create-mount-target --subnet-id $SUBNET1 --security-groups $ENV_SG --file-system-id $EFS_ID | jq -r .MountTargetID)
MOUNT_TARGET_2_ID=$(aws efs create-mount-target --subnet-id $SUBNET2 --security-groups $ENV_SG --file-system-id $EFS_ID | jq -r .MountTargetID)

MOUNT_TARGET_1_ID=fsmt-00ec7d93b6270f0ab
MOUNT_TARGET_2_ID=fsmt-0653dc7d9bd25b6f7

if command -v tmux &>/dev/null; then
	tmux new-session -s banner-dr-deploy \; detach-client
	TMUX_AVAIL=1
fi

function deploy_app {
	local appname="$1"
	if [ ${TMUX_AVAIL:-0} -eq 1 ]; then
		tmux neww -t 'banner-dr-deploy:{end}' -a -n "${appname}" copilot svc deploy --name "${appname}"
	else
		copilot svc deploy --name "${appname}"
	fi
}
deploy_app static
for aws_appname in "${!apps[@]}"; do
	deploy_app "${aws_appname}"
done

if [ ${TMUX_AVAIL:-0} -eq 1 ]; then
	tmux killw -t 'banner-dr-deploy:0'
	tmux attach -t banner-dr-deploy
fi
