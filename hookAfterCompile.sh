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
