FROM nginxinc/nginx-unprivileged:1.25-alpine

# O usuário nginx existe na imagem unprivileged
USER nginx

# Copia os arquivos estáticos do frontend
COPY public/ /usr/share/nginx/html/

# Copia configuração do Nginx com Proxy Reverso e Security Headers
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 8080

CMD ["nginx", "-g", "daemon off;"]
