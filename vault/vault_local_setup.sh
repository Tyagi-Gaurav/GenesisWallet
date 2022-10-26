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

vault auth enable approle

# create secret path
vault secrets enable -path=database kv-v2

# database-policy.hcl
# Read-only permission on 'database/*' path
tee database-policy.hcl <<"EOF"
path "database/*" {
  capabilities = [ "read" ]
}
EOF

vault policy write database database-policy.hcl

# database-init-token.hcl
# policy for initial token
tee database-init-token.hcl <<"EOF"
path "auth/approle/*" {
  capabilities = [ "create", "read", "update" ]
}
EOF

vault policy write database-init-token database-init-token.hcl

# write approle for pg_service_1 with policy:database and ttl:3h
vault write auth/approle/role/pg_service_1 policies="database" token_ttl=3h

# Store kv data
tee postgres.txt <<"EOF"
{
  "url": "jdbc:postgresql://local.postgres:5432/testUserDB",
  "username": "user",
  "password": "password",
  "port": "5432",
  "host" : "local.postgres"
}
EOF

vault kv put database/postgres/service_1 @postgres.txt

# Generate init token for APP, valid for 3h
TOKEN_OUTPUT=$(vault token create -policy=database-init-token -ttl=3h -format=json)
echo "$TOKEN_OUTPUT"
TOKEN=$(echo "$TOKEN_OUTPUT" | jq '.auth.client_token')
echo "Token: $TOKEN"
mkdir -p /var/public
echo "$TOKEN" > /var/public/database_init_token

jobs -l

tail -f /dev/null