# Notes

## JWT Terminology

### Subject/Principal/User

These are hierarchical in the way that genus, species and individual are hierarchical.

Subject - In a security context, a subject is any entity that requests access to an object. These are generic terms used to denote the thing requesting access and the thing the request is made against. When you log onto an application you are the subject and the application is the object. When someone knocks on your door the visitor is the subject requesting access and your home is the object access is requested of.

Principal - A subset of subject that is represented by an account, role or other unique identifier. When we get to the level of implementation details, principals are the unique keys we use in access control lists. They may represent human users, automation, applications, connections, etc.

User - A subset of principal usually referring to a human operator. The distinction is blurring over time because the words "user" or "user ID" are commonly interchanged with "account". However, when you need to make the distinction between the broad class of things that are principals and the subset of these that are interactive operators driving transactions in a non-deterministic fashion, "user" is the right word.

## Instructions for Running Locally

- install & setup mysql

- create a database (name example: social_media_demo_db)

- make sure user in connection string has privileges granted on the db

- run the application so the db update occurs (tables created automatically)

- verify seed data was inserted into db (refer to DatabaseSeeder.java CommandLineRunner)