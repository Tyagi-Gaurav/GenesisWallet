FROM chonku/nodejs:v0.1.8

ENV OAUTH_CLIENT_ID_GITHUB=$OAUTH_CLIENT_ID_GITHUB
ENV OAUTH_CLIENT_SECRET_GOOGLE=$OAUTH_CLIENT_SECRET_GOOGLE

WORKDIR /app

COPY package.json package.json
COPY package-lock.json package-lock.json
RUN npm install

COPY public public
COPY views views
COPY app_oauth2.js app.js
COPY ./startup.sh /startup.sh
ENV PROTO_PATH=/app/proto/UserService.proto

CMD [ "node", "app.js" ]

EXPOSE 3000
