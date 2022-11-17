#!/bin/sh

sudo yum update -y
sudo yum install -y jq yum-utils curl
sudo yum-config-manager --add-repo https://rpm.releases.hashicorp.com/AmazonLinux/hashicorp.repo
sudo yum install -y vault

export VAULT_ADDR='http://127.0.0.1:8200'
export VAULT_API_ADDR='http://127.0.0.1:8200'
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

#!/bin/sh

#Auth Policies setup for B2B services

# create secret path
vault secrets enable -path=database kv-v2

# database-policy.hcl
# Read-only permission on 'database/*' path
sudo tee database-policy.hcl <<"EOF"
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
sudo tee postgres.txt <<"EOF"
{
  "username": "${DB_USER}",
  "password": "${DB_PASSWORD}",
  "port": "${DB_PORT}",
  "host" : "${DB_HOST}"
}
EOF

# Store database payload in the key
vault kv put database/postgres/user_service @postgres.txt

#Cert Policies setup for different services
#vault secrets enable -path=api_gateway kv-v2

# Read-only permission on 'webserver/*' path
#tee webserver-policy.hcl <<"EOF"
#path "api_gateway/*" {
#  capabilities = [ "read" ]
#}
#EOF

# Create the policy named webserver using contents from file
#vault policy write webserver webserver-policy.hcl

#vault write auth/approle/role/api_gateway policies="webserver" token_ttl=3h

#vault kv put api_gateway/pk private_key=@local_wallet.key
#vault kv put api_gateway/cert cert=@local_wallet.crt

jobs -l

tail -f /dev/null