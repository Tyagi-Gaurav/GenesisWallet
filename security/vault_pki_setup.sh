#!/bin/sh

#Cert Policies setup for different services
vault secrets enable -path=api_gateway kv-v2

# Read-only permission on 'webserver/*' path
tee webserver-policy.hcl <<"EOF"
path "api_gateway/*" {
  capabilities = [ "read" ]
}
EOF

# Create the policy named webserver using contents from file
vault policy write webserver webserver-policy.hcl

vault write auth/approle/role/api_gateway policies="webserver" token_ttl=3h

vault kv put api_gateway/pk private_key=@local_wallet.key
vault kv put api_gateway/cert cert=@local_wallet.crt
