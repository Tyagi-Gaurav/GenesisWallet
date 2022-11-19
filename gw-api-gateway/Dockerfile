FROM nginx

COPY . /etc/nginx

RUN apt-get update && apt-get install -y jq
ADD update_cert.sh /docker-entrypoint.d
RUN chmod +x /docker-entrypoint.d/update_cert.sh && mkdir -p /etc/keys && echo "Wallet@123" > /etc/keys/fifo

EXPOSE 80
EXPOSE 443