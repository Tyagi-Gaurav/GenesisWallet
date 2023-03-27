#!/usr/bin/env sh

############################################################
# Set Variables                                            #
############################################################
REMOVE_ORPHANS=""
BUILD='./gradlew clean build'
SKIP_TEST=''

############################################################
# Help                                                     #
############################################################
Usage()
{
   # Display Help
   echo "Run full local build"
   echo
   echo "Syntax: full_local_build.sh [-o|-h|-b|-t]"
   echo "options:"
   echo "o    Remove orphan containers."
   echo "b    Skip build."
   echo "t    Skip test."
   echo "h    Print this Help."
   echo
}

############################################################
############################################################
# Main program                                             #
############################################################
############################################################
while getopts :obht flag ; do
    case "${flag}" in
        o)
           REMOVE_ORPHANS="--remove-orphans";;
        b)
          BUILD="";;
        t)
          SKIP_TEST='-x test -x integrationTest' ;;
        h)
          Usage
          exit;;
        *) # Invalid option
          echo "Error: Invalid option: ${flag}"
          exit;;
    esac
done

FULL_BUILD_COMMAND="$BUILD $SKIP_TEST"

set -a
docker-compose down --rmi all
eval "$FULL_BUILD_COMMAND"

set -e #Fail on error
docker-compose --env-file ~/.secret/env.file up -d --build $REMOVE_ORPHANS
WAIT_TIME=15
echo "Waiting for ${WAIT_TIME} seconds for container to come up"
sleep ${WAIT_TIME}

./gradlew localFunctional