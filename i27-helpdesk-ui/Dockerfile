FROM node:18-alpine as builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY .  .
ARG NEXT_PUBLIC_API_BASE_URL
ENV  NEXT_PUBLIC_API_BASE_URL=${NEXT_PUBLIC_API_BASE_URL}
RUN npm run build

FROM node:18-alpine
COPY --from=builder /app ./
EXPOSE 3000
CMD [ "npm", "start" ]