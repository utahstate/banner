FROM nginx:1.31.5-alpine

ARG ENVIRONMENT=zprod

RUN ash -c 'rm /usr/share/nginx/html/*'
COPY ${ENVIRONMENT}/*.svg ${ENVIRONMENT}/*.jpg /usr/share/nginx/html/img/
COPY default.conf /etc/nginx/conf.d/default.conf

