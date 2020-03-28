#!/bin/sh
OWN_NAME=setenv.sh
BASENAME="$(basename -- "$0")"

SOURCED=0
if [ -n "$ZSH_EVAL_CONTEXT" ]; then
    [[ $ZSH_EVAL_CONTEXT =~ :file$ ]] && SOURCED=1
elif [ -n "$BASH_VERSION" ]; then
    [[ $0 != $BASH_SOURCE ]] && SOURCED=1
elif [ "$OWN_NAME" != "$BASENAME" ]; then
    SOURCED=1
fi

if [ "$SOURCED" -ne 1 ]; then
    echo "* Please call as '. ./$OWN_NAME', not './$OWN_NAME' !!!---"
    echo "* Also please DO NOT set back the executable attribute"
    echo "* On this file. It was cleared on purpose."

    chmod -x ./$OWN_NAME
    exit
fi

export MYSQL_USER="nextdoor"
export MYSQL_PASSWORD="nextdoor12345"

java -version
echo "mysql user: ${MYSQL_USER}"
