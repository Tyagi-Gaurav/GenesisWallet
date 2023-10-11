FROM chonku/java:v0.1.8

WORKDIR /app

COPY ../proto/userservice.proto userservice.proto
COPY package.json package.json
COPY package-lock.json package-lock.json
RUN npm install

COPY public public
COPY views views
COPY app.js app.js
COPY ./startup.sh /startup.sh

CMD [ "node", "app.js" ]

EXPOSE 3000
