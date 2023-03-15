FROM nginx

COPY . /etc/nginx

RUN apt-get update && apt-get install -y jq && mkdir -p /etc/nginx/templates
COPY api_backends.conf.template /etc/nginx/templates

EXPOSE 80
EXPOSE 443