UI 
    ui is written nextj 
    UI is running at port number: 3000
    how to start this app: 
        npm install 
        npm run dev 
    In ui i need to create a file called .env.local and need to place Endpoint of my api gateway
    ```NEXT_PUBLIC_API_BASE_URL=http://localhost:8080```
Gateway
    gateway is written in NodeJS
    gateway is running at port number: 8080
    how to start this app: 
        npm install 
        npm run dev 
    in gateway i need to create a file called .env.local
    in this file i need to place environmental variables for all other api end points along with jwt secret
    ```
        SERVER_PORT=8080
    UI_ORIGIN=http://localhost:3000

    AUTH_SERVICE_URL=http://localhost:8081
    TICKET_SERVICE_URL=http://localhost:8082
    COMMENT_SERVICE_URL=http://localhost:8083
    ATTACHMENT_SERVICE_URL=http://localhost:8084

    JWT_SECRET=i27academy-secret-key-which-is-32chars
    ```
Auth 
    auth is written in java
    auth is running at port number: 8081
    how to start this app: 
        mvn spring-boot:run -Dspring-boot.run.profiles=local
    In auth i need to create a file called application-local.yaml under resources in src folder , keep the below details
    ```yaml
    server:
    port: 8081
    spring:
    datasource:
        url: jdbc:mysql://136.65.46.225:3306/helpdesk_dev
        username: helpdesk_user
        password: Gcp@2024
    jwt:
    secret: i27academy-secret-key-which-is-32chars
    expiryMillis: 3600000
    ```
Ticket 
    ticket is written is java 
    ticket is running at port number: 8082
    how to start this app: 
        mvn spring-boot:run -Dspring-boot.run.profiles=local
    In ticekt i need to create a file called application-local.yaml under resources in src folder , keep the below details
    ```yaml
    server:
    port: 8082
    spring:
    datasource:
        url: jdbc:mysql://136.65.46.225:3306/helpdesk_dev
        username: helpdesk_user
        password: Gcp@2024
    jwt:
    secret: i27academy-secret-key-which-is-32chars
    expiryMillis: 3600000
    ```
Comment 
    comment is written in python 
    comment is running at port number: 8083
    how to start this app:
        python -m pip install -r requirements.txt
        python -m unicorn app.main:app --reload --port 8083
    in python i need to create a file called .env.local and need to mention these details
    ```yaml
DB_USER=helpdesk_user
DB_PASSWORD=Gcp@2024
DB_HOST=136.65.46.225
DB_PORT=3306
DB_NAME=helpdesk_dev

NOTIFICATION_URL=http://localhost:8084/notifications/event
TICKET_SERVICE_URL=http://localhost:8082/tickets
    ```
Notification 
    comment is written in python 
    comment is running at port number: 8084
    how to start this app:
        python -m pip install -r requirements.txt
        python -m unicorn app.main:app --reload --port 8084
    