#!/usr/bin/env sh

############################################################
# Set Variables                                            #
############################################################
REMOVE_ORPHANS=""
REMOVE_IMAGES=""
BUILD='mvn clean package'
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
   echo "o    Remove orphan containers & images."
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
while getopts :obqht flag ; do
    case "${flag}" in
        o)
           REMOVE_ORPHANS="--remove-orphans"
           REMOVE_IMAGES="--rmi all";;
        b)
          BUILD="";;
        t)
          #If BUILD is null, then SKIP_TEST should also be null
          if [ -z "$BUILD" ] ; then
            SKIP_TEST=""
          else
            SKIP_TEST='-DskipTests=true'
          fi
          ;;
        h)
          Usage
          exit;;
        *) # Invalid option
          echo "Error: Invalid option: ${flag}"
          exit;;
    esac
done

FULL_BUILD_COMMAND="$BUILD $SKIP_TEST"

set -e #Fail on error
docker-compose down $REMOVE_IMAGES
eval "$FULL_BUILD_COMMAND"

docker-compose --env-file ~/.secret/env.file --env-file ./ui/.env up -d --build $REMOVE_ORPHANS
WAIT_TIME=1
echo "Waiting for ${WAIT_TIME} seconds for container to come up"
sleep ${WAIT_TIME}

make ft