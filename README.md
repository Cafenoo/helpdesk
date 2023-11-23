# About

A web application where users may submit ticket to let engineers fix
specific issues

### Accounts

In order to use any functionality you have to be logged in, the users:

#### Role 'Employee': 
`user1_mogilev@yopmail.com` \
`P@ssword1` 

`user2_mogilev@yopmail.com` \
`P@ssword1`

#### Role 'Manager': 
`manager1_mogilev@yopmail.com` \
`P@ssword1` 

`manager2_mogilev@yopmail.com` \
`P@ssword1`

#### Role 'Engineer': 

`engineer1_mogilev@yopmail.com` \
`P@ssword1` 

`engineer2_mogilev@yopmail.com` \
`P@ssword1`

# How to run

If you just want to run a project its sufficient to use `docker-compose up` in project root and it's gonna manage everything

### Environment variables

If you want email sending to work you have to specify \
`EMAIL_USERNAME` \
`EMAIL_TOKEN` 

If those are absent the app will still work, but sending any emails won't

Default provider is gmail, if you need to change it specify \
`EMAIL_HOST` \
`EMAIL_PORT`

## How to run backend from cmd:

Prerequisites: maven v3.8.1, java 11

1. Open project root folder 
2. Open cmd in project root directory
3. Execute `mvn package`
4. Execute `java -jar target/helpdesk.jar` 

Backend server port: `8080`

## How to run frontend:

Prerequisites: node.js v14.21.3

1. Open project root folder
2. Open cmd in project root directory
3. Execute `cd java-learn-app-main`
4. Execute `npm install`
5. Execute `npm start`

Front-end port: `3000`
