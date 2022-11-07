#!/bin/sh

#Auth Policies setup for B2B services

# create secret path
vault secrets enable -path=database kv-v2

# database-policy.hcl
# Read-only permission on 'database/*' path
tee database-policy.hcl <<"EOF"
path "database/*" {
  capabilities = [ "read" ]
}
EOF

# Create the policy named database using contents from file
vault policy write database database-policy.hcl

# app-init-token.hcl
# policy for initial token
#tee app-init-token.hcl <<"EOF"
#path "auth/approle/*" {
#  capabilities = [ "create", "read", "update" ]
#}
#EOF

# write approle for user_service with policy:database, jwt and ttl:3h
vault write auth/approle/role/user_service policies="database" token_ttl=3h

# Store kv data for user service database
tee postgres.txt <<"EOF"
{
  "username": "user",
  "password": "password",
  "port": "5432",
  "host" : "local.postgres"
}
EOF

# Store database payload in the key
vault kv put database/postgres/user_service @postgres.txt

## Generate init token for APP, valid for 3h
#TOKEN_OUTPUT=$(vault token create -policy=app-init-token -ttl=3h -format=json)
#echo "$TOKEN_OUTPUT"
#TOKEN=$(echo "$TOKEN_OUTPUT" | jq '.auth.client_token')
#echo "Token: $TOKEN"
#mkdir -p /var/public
#echo "$TOKEN" > /var/public/app_init_token