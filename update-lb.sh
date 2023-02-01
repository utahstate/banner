#!/bin/sh

SERVICE=$1
ENV=$2

CERTI_ARN="$(aws acm list-certificates | jq -r '.CertificateSummaryList[0] .CertificateArn')"
LB_DNS="$(copilot svc show --name $SERVICE --json | jq '.variables[] | select((.environment == "'$ENV'") and (.name == "COPILOT_LB_DNS")) | .value')"
LB_ARN="$(aws elbv2 describe-load-balancers --output json | jq -r '.LoadBalancers[] | select(.DNSName == '$LB_DNS') | .LoadBalancerArn')"
TG_ARN="$(copilot svc show --name $SERVICE --json --resources | jq -r '.resources | .["'${ENV}'"][] | select((.type == "AWS::ElasticLoadBalancingV2::TargetGroup")) | .physicalID')"

LISTENER_ARN="$(aws elbv2 describe-listeners --load-balancer-arn $LB_ARN --output json | jq -r '.Listeners[] | select(.Port == 443) | .ListenerArn')"
if [ -z "$LISTENER_ARN" ]
then
  aws elbv2 create-listener \
    --load-balancer-arn $LB_ARN \
    --protocol HTTPS \
    --port 443 \
    --ssl-policy "ELBSecurityPolicy-TLS-1-2-Ext-2018-06" \
    --certificates CertificateArn=$CERTI_ARN \
    --default-actions Type=forward,TargetGroupArn=$TG_ARN
else
  echo "Nothing to do."
fi
