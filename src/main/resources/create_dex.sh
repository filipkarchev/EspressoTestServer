#!/bin/bash

echo -e "Enter your package name:"
read package

folders=$(echo $package | tr "." "\n")

packageDir=""
firstFolder=""

for addr in $folders
do
    firstFolder=$addr
    break
    #packageDir="$packageDir/$addr"
done

#packageDir="$packageDir/"
#packageDir=${packageDir#*/}
#echo $packageDir

#finalFolder="build/intermediates/classes/androidTest/release/$firstFolder"

cd build/intermediates/classes/androidTest/release

jar cvf testFiles.jar $firstFolder

mv testFiles.jar ../../../../../testFiles.jar

cd ../../../../../

dx --dex --verbose --output=testFiles.dex testFiles.jar
