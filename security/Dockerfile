FROM vault:1.12.0

ADD vault_auth_setup.sh ./
ADD vault_pki_setup.sh ./
ADD vault_all_setup.sh ./
ADD local/local_wallet.crt ./
ADD local/local_wallet.key ./
ADD config.hcl ./

# Install Curl
RUN apk add --no-cache curl

# Install jq
ADD https://github.com/stedolan/jq/releases/download/jq-1.6/jq-linux64 /usr/bin/jq
RUN chmod 755 /usr/bin/jq && chmod 755 vault_*.sh

EXPOSE 8200
ENTRYPOINT ["./vault_all_setup.sh"]
