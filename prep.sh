#!/bin/bash
INSTANCE=zpprd


echo "Removing old war and folder"
rm -rf FacultySelfService.war
rm -rf FacultySelfService

echo "Making new folder"
mkdir FacultySelfService

echo "Downloading war from build.banner"
scp root@build.banner.usu.edu:/u01/deploy/$INSTANCE/self-service/FacultySelfService.war .

echo "Extracting war"
cd FacultySelfService
jar xvf ../FacultySelfService.war
cd ..

