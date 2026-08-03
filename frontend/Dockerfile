FROM nginx:1.25-alpine

# Remove página padrão do Nginx
RUN rm -rf /usr/share/nginx/html/*

# Copia os arquivos estáticos do frontend
COPY public/ /usr/share/nginx/html/

# Copia configuração do Nginx com Proxy Reverso e Security Headers
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
