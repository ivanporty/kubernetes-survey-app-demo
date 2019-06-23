FROM node:10-alpine
COPY package*.json ./
RUN npm install
COPY *.js .
ENTRYPOINT ["node", "server.js"]