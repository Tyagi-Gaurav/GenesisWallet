#!/bin/sh

VAULT_TOKEN=root
VAULT_ADDRESS=local.vault:8200

echo "Waiting for 10 seconds before downloading cert and private key"
sleep 10

RAW_PRIVATE_KEY=$(curl -s --header "X-Vault-Token: ${VAULT_TOKEN}" \
    --request GET \
    http://"${VAULT_ADDRESS}"/v1/api_gateway/data/pk | jq )

echo "RAW_PRIVATE_KEY : ${RAW_PRIVATE_KEY}"

PRIVATE_KEY=$(curl -s --header "X-Vault-Token: ${VAULT_TOKEN}" \
    --request GET \
    http://"${VAULT_ADDRESS}"/v1/api_gateway/data/pk | jq '.data.data.private_key')

RAW_CERT=$(curl -s --header "X-Vault-Token: ${VAULT_TOKEN}" \
    --request GET \
    http://"${VAULT_ADDRESS}"/v1/api_gateway/data/cert | jq )

echo "RAW_CERT: ${RAW_CERT}"

CERT=$(curl -s --header "X-Vault-Token: ${VAULT_TOKEN}" \
    --request GET \
    http://"${VAULT_ADDRESS}"/v1/api_gateway/data/cert | jq '.data.data.cert')

echo "Writing certs to /etc/ssl/certs/wallet.crt. $(echo "$CERT" | tr -d '"')"
echo "$CERT" | tr -d '"' > /etc/ssl/certs/wallet.crt
echo "Writing private key to /etc/ssl/private/wallet.key $(echo "$PRIVATE_KEY" | tr -d '"')"
echo "$PRIVATE_KEY" | tr -d '"' > /etc/ssl/private/wallet.key

#Verify certs and private_key
#openssl x509 -noout -modulus -in /etc/ssl/certs/wallet.crt | openssl md5
#openssl rsa -noout -modulus -in /etc/ssl/private/wallet.key | openssl md5