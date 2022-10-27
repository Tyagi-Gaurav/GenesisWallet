FROM vault:1.12.0

ADD vault_local_setup.sh ./
ADD config.hcl ./

# Install Curl
RUN apk add --no-cache curl

# Install jq
ADD https://github.com/stedolan/jq/releases/download/jq-1.6/jq-linux64 /usr/bin/jq
RUN chmod 755 /usr/bin/jq && chmod 755 vault_local_setup.sh

EXPOSE 8200
ENTRYPOINT ["./vault_local_setup.sh"]
