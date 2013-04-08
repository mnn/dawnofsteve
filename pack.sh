#!/bin/bash
. ./initVars.sh

output=jar_output
outtmp="$output/tmp"
binPath="$binDataDir"
dist="$output/dist"

function parseVersion {
    ver_line=`grep -Ei 'String +Version' $srcModDir/monnef/dawn/Reference.java` || crash "No version line found."
    version=`sed -r 's/^.*"(.*)".*;$/\1/' <<< "$ver_line"` || crash "Cannot parse version line."
    test ${#version} -gt 8 && crash "Parsed version is suspiciously long, stopping"

    echo "$version"
}

#main

printAction "Preparing"
od=`getAbsCurrDir`
test ${#output} -lt 5 && crash "weird string in output directory variable. halting for safety reasons."

testDir "$output"
emptyDir "$output"
emptyDir "$outtmp"
emptyDir "$dist"

printAction "Parsing version"
version=`parseVersion`
echo "Version detected: [$version]"

printAction "Copying mod files..."
cp -r $binPath/* "$outtmp" || crash "Cannot copy bin_data directory."
cp -r $mcpDir/reobf/minecraft/monnef/dawn "$outtmp" || crash "Cannot copy obfuscated classes."

printAction "Packing mod to jar"
outNameMod="Dawn_of_Steve_$version"
cd "$outtmp"
zip -q -9r "../$outNameMod.jar" ./*
cd "$od"

printAction "Creating final zip"
mkdir "$dist/mods" || crash "Cannot create mods directory."
cp "$output/$outNameMod.jar" "$dist/mods"
cd "$dist"
outNameFinal="${outNameMod}_packed"
zip -q -9r "../$outNameFinal.zip" ./*
cd "$od"

printDone
