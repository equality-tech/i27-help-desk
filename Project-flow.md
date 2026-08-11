## i27helpdesk
* Siva is a developer who is working on the complete product
* And you guys are devops who will make sure this product is launched to public . 


## What is this product all about 
* This is a ticketing system . 
* Whatsapp, mail , phone.... 
* Student who has enrolled , if having any issue will create a ticket. 
* Admin 
    *  tickets view
    * tickets are assigned to support team
* Support
    * View the tickets, that are assigned to them 
    * Work on fixing those 
* Student 
    * They will raise the ticket depending upon the issue they have .


## Product Architecutre:
* THis product is completely broken into microservices 
* For ui we are having `i27helpdesk-ui` written in `nextjs`
* A gateway should be available for commuicating withib this resources
    * `api-gateway` written in `node`
* For login we are having a `auth-service` written in `Java`
* For tickets we are having a `ticket-service` written in `Java`
* For comments we are having a `comment-service` written in `Python`
* For notifications we are having a `notification-service` written in `Python`


## Prerequiste
* In my local machine i should be having 
    * java
        * mvn build tool 
    * python
    * node 

* We should have mysql database.


* UI >  apis > postman > db 




localhost:3000 > http://localhost:3000/auth/login


Database url/host:              136.65.46.225
Database Name:                  helpdesk_dev
Database user:                  helpdesk_user
Database Password:              Gcp@2024


* Database is ready and connected 
* No tables no data inside the database 
* We need to have the schema ready
    * option 1: 
        * code gets deployed the schema will automatically be created . 
    * Option 2 :
        * the schema can be given prior. 
        * we have choosen this optiion. 
* Students, Admin, Support should login 
    * As a admin i wil  login to dashboard and create student logins.
    * but how will a admin login ? 
        * so we can create a admin user id and password direclty  in the database,.  


application team : userid


userid: siva@i27academy.com
password: admin123


3 types of user roles
* USER > Student > Create tickets and add comments. 
* ADMIN > Super access, user creation, tickets assign 
* AGENT > Work on tickets assigned to him . 


* inserting data into roles table 
```sql
INSERT INTO roles  (role_name)
VALUES
    ('USER'),
    ('AGENT'),
    ('ADMIN');
```
```sql
INSERT into users(
    email,
    password_hash,
    full_name,
    status
)
VALUES (
    'siva@i27academy.com',
    '$2a$12$qDxPJt4sQGsHz8NfxCH.sOkPcQTfL/7Ms5B7eFL7wtcF5jaSRbgUG',
    'System Admin',
    'ACTIVE'
);
```

```sql
INSERT into user_roles(
    user_id,
    role_id
)
VALUES (
    (SELECT id FROM users WHERE email = 'siva@i27academy.com' ),
    6
);
```

admin123



UI > AUTH > Database 


http://localhost:3000/auth/login



http://localhost:3000/support/login > http://localhost:8080/auth/login > should go to auth  > database
http://localhost:8080/auth/login


## Execution steps
* UI
```
npm install
npm run dev
```
* gateway
```
npm install
npm run dev
```
* Auth
```
mvn spring-boot:run -Dspring-boot.run.profiles=local
```
* ticket
```
mvn spring-boot:run -Dspring-boot.run.profiles=local
```
* Comment
```
python -m pip install -r requirements.txt
python -m unicorn app.main:app --reload --port 8083
```


UI : 3000 
Gateway: 8080
auth: 8081
ticket: 8082

