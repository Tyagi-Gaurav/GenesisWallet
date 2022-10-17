#!/bin/zsh

openssl genrsa -des3 -out api-gatewaylocal_/wallet.key 2048
#Pwd: Wallet@123

#Let's create a CSR (wallet.csr) from our existing private key:
#CN: api.wallet.com
#openssl req -key api-gateway/wallet.key -new -out api-gateway/wallet.csr
openssl req -new -key api-gateway/local_wallet.key -out api-gateway/local_wallet.csr -config api-gateway/local_server_cert.cnf

#A self-signed certificate is a certificate that's signed with its own 
#private key. It can be used to encrypt data just as well as CA-signed certificates
openssl x509 -signkey api-gateway/local_wallet.key -in api-gateway/local_wallet.csr -req -days 365 -out api-gateway/local_wallet.crt

#Check DNS in CSR
openssl req -noout -text -in api-gateway/local_wallet.csr | grep DNS

#Create KeyStore
keytool -keystore ftkeystore -genkey -alias client -keyalg RSA