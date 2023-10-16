FROM chonku/nodejs:v0.1.8

WORKDIR /app

COPY package.json package.json
COPY package-lock.json package-lock.json
RUN npm install

COPY public public
COPY views views
COPY app_oauth2.js app.js
COPY ./startup.sh /startup.sh
ENV PROTO_PATH=/app/proto/

CMD [ "node", "app.js" ]

EXPOSE 3000
