
# Etapa 1: Construir la aplicación Angular
FROM node:20 AS build

# Definir el directorio de trabajo en el contenedor
WORKDIR /usr/src/app

# Copiar el package.json y el package-lock.json
COPY package*.json ./

# Instalar las dependencias de la aplicación
RUN npm install

# Copiar todo el código fuente al contenedor
COPY . .

# Construir la aplicación Angular
RUN npm run build --prod

# Etapa 2: Servir la aplicación Angular con nginx
FROM nginx:alpine


RUN rm /usr/share/nginx/html/*
# Copiar los archivos construidos desde la etapa anterior al directorio de nginx
COPY --from=build /usr/src/app/dist/reserva-hoteles/browser/ /usr/share/nginx/html/
COPY --from=build /usr/src/app/dist/reserva-hoteles/server/ /usr/share/nginx/html/
COPY --from=build /usr/src/app/dist/reserva-hoteles/ /usr/share/nginx/html/

# Copiar el archivo de configuración de Nginx personalizado al contenedor
COPY nginx.conf /etc/nginx/nginx.conf

# Exponer el puerto 80
EXPOSE 80

# Ejecutar nginx en primer plano
CMD ["nginx", "-g", "daemon off;"]


