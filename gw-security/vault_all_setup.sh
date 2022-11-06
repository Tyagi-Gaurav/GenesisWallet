#!/bin/sh

export VAULT_ADDR='http://127.0.0.1:8200'
export VAULT_API_ADDR='http://local.vault:8200'
echo "Starting Vault Server"
vault server -dev -dev-root-token-id="root" -dev-listen-address="0.0.0.0:8200" &
PID="$!"
echo "Waiting for Vault with pid...$PID"
while [ "$(curl --insecure $VAULT_ADDR/v1/sys/health | jq '.initialized')" != "true" ]
do
    echo 'Vault is Initializing...'
    sleep 2
done

echo "Vault Started."

vault auth enable approle #enables an auth method at /approle

./vault_auth_setup.sh
./vault_pki_setup.sh

jobs -l

tail -f /dev/null