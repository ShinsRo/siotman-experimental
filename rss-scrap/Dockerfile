FROM node:19.2-alpine

WORKDIR /Users/ian/Projects/siotman/rss-scrap

COPY package*.json ./

RUN npm install --only=production

COPY ./src ./src

CMD npm start
