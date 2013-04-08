#!/bin/bash

function crash {
    echo $1
    exit 1
}

function testDir {
    test -d "$1" || crash "\"$1\" is missing!"
}

function emptyDir {
    test "${#1}" -lt 5 && crash "\"$1\" is suspicously short!"
    if [ -d "$1" ]; then
        echo "Purging directory \"$1\"."
        rm -fr "$1"/* || crash "Cannot purge."
    else
        echo "Creating directory \"$1\"."
        mkdir $1
    fi        
}

function printAction {
    echo ">> $1..."
}

function printDone {
    echo ">> Done"
}

function getAbsCurrDir {
    echo "$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
}

forgeDir="build"
mcpDir="$forgeDir/mcp"
coreDir="core"
binDataDir="bin_data"
srcModDir="src"
