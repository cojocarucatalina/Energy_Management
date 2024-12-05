# FROM node:22 as builder
# WORKDIR /app
# COPY package*.json /app/
# RUN npm install --f
# COPY ./ /app/
# RUN npm run build
# CMD ["npm", "start"]


FROM node:22.11.0 AS builder
WORKDIR /app
COPY package*.json ./
RUN npm install --legacy-peer-deps
COPY . .
CMD ["npm", "start"]
