FROM nginx
COPY . /etc/nginx

ADD local_wallet.key /etc/ssl/private/wallet.key
ADD local_wallet.crt /etc/ssl/certs/wallet.crt
RUN mkdir -p /etc/keys && echo "Wallet@123" > /etc/keys/fifo

EXPOSE 8080
EXPOSE 443