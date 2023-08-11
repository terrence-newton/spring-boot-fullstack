FROM --platform=linux/arm64/v8 node:19-alpine
ARG api_base_url
WORKDIR /app
COPY package*.json .
RUN npm i --silent
COPY . .
RUN echo "VITE_API_BASE_URL=${api_base_url}" > .env
EXPOSE 5173
CMD ["npm", "run", "dev"]