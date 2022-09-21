#!/bin/zsh

openssl genrsa -des3 -out api-gateway/wallet.key 2048
#Pwd: Wallet@123

#Let's create a CSR (wallet.csr) from our existing private key:
#CN: api.wallet.com
openssl req -key api-gateway/wallet.key -new -out api-gateway/wallet.csr

#A self-signed certificate is a certificate that's signed with its own 
#private key. It can be used to encrypt data just as well as CA-signed certificates
openssl x509 -signkey api-gateway/wallet.key -in api-gateway/wallet.csr -req -days 365 -out api-gateway/wallet.crt