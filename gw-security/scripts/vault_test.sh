#!/bin/sh

VAULT_TOKEN=root
VAULT_ADDRESS=localhost:8200

#PRIVATE_KEY=$(curl -s --header "X-Vault-Token: ${VAULT_TOKEN}" \
#    --request POST \
#    --data "{\"common_name\": \"${CERT_COMMON_NAME}\", \"ttl\": \"1h\"}" \
#    http://"${VAULT_ADDRESS}"/v1/pki_int/issue/"${VAULT_ROLE_NAME}" | jq '.data.private_key')

PRIVATE_KEY=$(curl -s --header "X-Vault-Token: ${VAULT_TOKEN}" \
    --request GET \
    http://"${VAULT_ADDRESS}"/v1/api_gateway/data/pk | jq '.data.data.private_key')

CERT=$(curl -s --header "X-Vault-Token: ${VAULT_TOKEN}" \
    --request GET \
    http://"${VAULT_ADDRESS}"/v1/api_gateway/data/cert | jq '.data.data.cert')

echo "$PRIVATE_KEY"
echo "$CERT"

