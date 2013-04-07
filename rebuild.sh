#!/bin/bash

function crash {
    echo $1
    exit 1
}

function testDir {
    test -d "$1" || crash "$1 is missing"
}

function emptyDir {
    test "${#1}" -lt 5 && crash "$1 is suspicously short"
    if [ -d "$1" ]; then
        rm -fr "$1/*"
    else
        echo "creating dir: $1"
        mkdir $1
    fi        
}

function printAction {
    echo -n "$1..."
}

function printDone {
    echo "done"
}

#main

#code preparation
. ./initVars.sh
mcp=$mcpDir
srcBase="$mcp/src_base"
src="$mcp/src"
srcCore="core/src"
srcMod="src"
op=`pwd`

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
cp -r $srcBase/* "$src"
printDone

printAction "Copying core and mod files"
cp -r $srcCore/* "$src"
cp -r $srcMod/* "$src"
printDone

echo "Preparations complete"
echo

#compiling
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PATH="$PATH:$DIR/utils/"

cd "$mcp"
declare -a Work=('./recompile.sh' './reobfuscate.sh' './pack.sh');

for t in "${Work[@]}"; do
    echo "Running: $t"
    $t
    if [ "$?" -ne 0 ]; then
        echo "Failure!"
        exit 1
    fi
done

echo "Success!"
cd "$op"
