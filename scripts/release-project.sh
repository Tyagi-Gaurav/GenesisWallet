#!/usr/bin/env bash

NEW_TAG=$(git describe --tags --abbrev=0 | awk -F. -v OFS=. 'NF==1{print ++$NF}; NF>1{if(length($NF+1)>length($NF))$(NF-1)++; $NF=sprintf("%0*d", length($NF), ($NF+1)%(10^length($NF))); print}')
echo "Next release tag is $NEW_TAG"
echo "OWNER is ${OWNER}"
echo "REPO is ${REPO}"

BODY="{\"tag_name\":\"v${NEW_TAG}\",\"target_commitish\":\"master\",\"name\":\"v${NEW_TAG}\",\"draft\":false,\"prerelease\":false,\"generate_release_notes\":true}"
curl -L \
  -X POST \
  -H "Accept: application/vnd.github+json" \
  -H "Authorization: Bearer ${CUSTOM_TOKEN}"\
  -H "X-GitHub-Api-Version: 2022-11-28" \
  https://api.github.com/repos/"${OWNER}"/"${REPO}"/releases \
  -d "${BODY}"

gh release create v"$NEW_TAG" --generate-notes