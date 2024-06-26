# Notes

## JWT Terminology

### Subject/Principal/User

These are hierarchical in the way that genus, species and individual are hierarchical.

Subject - In a security context, a subject is any entity that requests access to an object. These are generic terms used to denote the thing requesting access and the thing the request is made against. When you log onto an application you are the subject and the application is the object. When someone knocks on your door the visitor is the subject requesting access and your home is the object access is requested of.

Principal - A subset of subject that is represented by an account, role or other unique identifier. When we get to the level of implementation details, principals are the unique keys we use in access control lists. They may represent human users, automation, applications, connections, etc.

User - A subset of principal usually referring to a human operator. The distinction is blurring over time because the words "user" or "user ID" are commonly interchanged with "account". However, when you need to make the distinction between the broad class of things that are principals and the subset of these that are interactive operators driving transactions in a non-deterministic fashion, "user" is the right word.

## DTOs for API Contract 

Use DTOs to serialize/deserialize request and response bodies.

DTOs allow you to decouple your API contract from your database schema, providing a more resilient and maintainable solution. This allows for separation of concerns and prevents tight coupling between layers.

## Instructions for Running Locally

- install & setup mysql

- create a database (name example: social_media_demo_db)

- make sure user in connection string has privileges granted on the db

- run the application so the db update occurs (tables created automatically)

- verify seed data was inserted into db (refer to DatabaseSeeder.java CommandLineRunner)

## Misc Notes about This API

- "updating" an Entity with the same exact content does not change the updatedAt time.

## Starting docker containers individually

docker network create network-test1 (run once)

docker run --name mysql-test -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password1234 -e MYSQL_USER=social_media_demo_user -e MYSQL_PASSWORD=password5678 -e MYSQL_DATABASE=social_media_demo -d mysql

docker run --name social-media-demo-test3 --network=network-test1 -p 8080:8080 -d social-media-starter:test3

## Decision to use MockMvc for integration tests instead of TestRestTemplate

Using MockMvc tests means a test server doesn't start up, which means faster test execution.
The only downside is the servlet layer is bypassed. This should be tested with external functional tests, however.
Can handle the auth/Spring Security layer easier. Don't need to make /auth/login calls for each test method.

@Transactional doesn't working when server starts up:
@Transactional on the test method do not control the transactions on the server.
this is because the test method and server are running on different threads.
this means those transactions will not rollback.

@Transactional is needed in order to have a fresh DB state for each test method.
It manages each test method as a transaction, and rolls back the DB changes once the test is done.
