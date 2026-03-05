FROM nginx:alpine

# Copia todos los archivos del frontend al directorio de nginx
COPY . /usr/share/nginx/html

# Copia configuración personalizada de nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 5500