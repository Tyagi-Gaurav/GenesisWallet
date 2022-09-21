FROM nginx
COPY . /etc/nginx

ADD wallet.key /etc/ssl/private/
ADD wallet.crt /etc/ssl/certs/
RUN mkdir -p /etc/keys && echo "Wallet@123" > /etc/keys/fifo

EXPOSE 8080
EXPOSE 443