#!/bin/bash
. ./initVars.sh

#main

#code preparation
mcp=$mcpDir
srcBase="$mcp/src_base"
src="$mcp/src"
srcCore="$coreDir/src"
srcMod="$srcModDir"
srcMC="$src/minecraft"

printAction "Checking directories"
testDir "$mcp"
testDir "$srcBase"
testDir "$srcCore"
testDir "$srcMod"
printDone

printAction "Emptying src directory"
emptyDir "$src"
printDone

printAction "Copying original source files"
cp -r $srcBase/* "$src" || exit 1
printDone

printAction "Copying core and mod files"
cp -r $srcCore/* "$srcMC" || exit 1
cp -r $srcMod/* "$srcMC" || exit 1
printDone

echo "Preparations complete"
echo

#compiling
DIR=`getAbsCurrDir`
PATH="$PATH:$DIR/utils/"

work=( './recompile.sh' './reobfuscate.sh' './pack.sh' )
workDir=( "$mcp" "$mcp" "" )

for (( i = 0 ; i < ${#work[@]} ; i++ )) do
    t="${work[$i]}"
    p="$DIR"/"${workDir[$i]}"
    echo "Running: \"$t\" at \"$p\""
    echo
    cd "$p"
    $t
    if [ "$?" -ne 0 ]; then
        echo "Failure!"
        exit 1
    fi
done

echo "Success!"
cd "$DIR"
