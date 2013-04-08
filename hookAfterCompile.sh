#!/bin/bash
. ./initVars.sh

export targetDir="$mcpDir/eclipse/Minecraft/bin/"
args="-d --preserve=all -f -r"

function copy {
    files=`eval echo $1`
    cp $args $files "$targetDir"
}

copy "bin_data/*"
copy "$forgeDir/common/forge_at.cfg"
copy "$forgeDir/fml/common/*.{cfg,info,properties}"

coreDummyName="monnefCore_dummy.jar"
coreModsDir="$mcpDir/jars/coremods"
if [ ! -f "$coreModsDir/$coreDummyName" ]; then
    echo "Not found dummy core file, copying"
    cp "$coreDir/$coreDummyName" "$coreModsDir" || crash "cannot copy dummy core file"
fi
