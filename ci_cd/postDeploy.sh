echo "HELLO! WE RAN THE POST DEPLOY SCRIPT!"
echo "CURRENT_DIR: $CURRENT_DIR"
echo "STAGINGPATH: $STAGINGPATH"
echo "STAGE_SUBDIR: $STAGE_SUBDIR"
echo "STAGINGLOC: $STAGINGLOC"
echo "APP_NAME: $APP_NAME"
echo "APPWAR $APPWARE"
echo "TARGET_DIR: $TARGET_DIR"
echo "SRC_DIR: $SRC_DIR"
echo "DEPLOYMENT_WAR $DEPLOYMENT_WAR"

INSTANCE=$(cut -d'/' -f4 <<< "$TARGET_DIR")
VERSION=$(cut -d'_' -f4 <<< "$STAGE_SUBDIR")
ssh rancher@rancher-util.ser321.usu.edu "cd /home/rancher/github/banner/ci_cd && ./prep.sh $APP_NAME $VERSION $INSTANCE"
