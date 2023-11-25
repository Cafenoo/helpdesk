FROM node:14.21.3
WORKDIR /workspace
COPY public/ /workspace/public
COPY src/ /workspace/src
COPY package.json /workspace/
RUN npm install
EXPOSE 3000
CMD ["npm", "start"]
