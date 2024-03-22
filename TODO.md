# TODOs

TODO: decouple Role and GrantedAuthority (Role implements GrantedAuthority)

- this is coupling the repository layer with the security layer. they should not depend on each other.

TODO: create positive scenario postman API tests

TODO: create negative scenario postman API tests

TODO: add created_at & updated_at columns

TODO: AOP logging

TODO: add @NotNull to parameters

TODO: parse JWT roles properly with a parser.

- check this: io.jsonwebtoken.RequiredTypeException: Cannot convert existing claim value of type 'class java.lang.String' to desired type 'class java.lang.Long'. JJWT only converts simple String, Date, Long, Integer, Short and Byte types automatically. Anything more complex is expected to be already converted to your desired type by the JSON Deserializer implementation. You may specify a custom Deserializer for a JwtParser with the desired conversion configuration via the JwtParserBuilder.deserializeJsonWith() method. See https://github.com/jwtk/jjwt#custom-json-processor for more information. If using Jackson, you can specify custom claim POJO types as described in https://github.com/jwtk/jjwt#json-jackson-custom-types
  at io.jsonwebtoken.impl.DefaultClaims.castClaimValue(DefaultClaims.java:169) ~[jjwt-impl-0.11.5.jar:0.11.5]
  at io.jsonwebtoken.impl.DefaultClaims.get(DefaultClaims.java:152) ~[jjwt-impl-0.11.5.jar:0.11.5]
  at com.nickesqueda.socialmediademo.security.JwtUtils.extractUserId(JwtUtils.java:56) ~[classes/:na]
  at com.nickesqueda.socialmediademo.security.JwtAuthFilter.doFilterInternal(JwtAuthFilter.java:35) ~[classes/:na]

TODO: create GET /user, PUT /user, DELETE /user

TODO: set the currently authenticated UserEntity to be accessed anywhere (like the Authentication object) 

- could also set the "username" of the Authentication object to be the Entity id

- NOTE: UserEntity.equals is not passing when object is cast from Object to UserEntity, even though values are same.

TODO: return proper response codes / ResponseEntities from controller endpoints

TODO: "getCommentsByPostId", "getCommentsByUserId" - these are more idiomatic (instead of "getPostsComments")

TODO: add user bio, email, other data

TODO: endpoint to update user info (separate from register user. should go in UserController/Service)

TODO: handle io.jsonwebtoken.ExpiredJwtException (thrown when parsing JWT in JwtFilter)

TODO: logic for creating admin accounts. separate endpoint?

TODO: optimize number of DB queries.

- could use raw SQL with @Query

TODO: see if JPA .save() is necessary when updating (i.e. post.setContent(...))

TODO: use id field as the jwt principal/subject. this will allow for the below method-level auth:

- example: @PreAuthorize("#username == authentication.principal.username")
- more: https://www.baeldung.com/spring-security-method-security

TODO: JWT object for JWTs? instead of String. may help with serializing/deserializing

TODO: return Location header for POST requests?

# Business Feature Ideas

## Likes

## Followers

## Public/Private Posts

## Disable Comments

allow user to disable/enable comments on a post, even after the post has been live.

option to delete all comments on the post when disabling comments after post is live, or keep them.

- this allows the comments to "re-appear" if user decides to re-enable comments later.
- also allows for a "reset" functionality (similar to deleting all comments)

## Post Statistics

like a chart of likes over time, most popular postEntities, follower chart, etc.

## Business Account

## 2FA & 2FA Enrollment

## Admin Delete

- if the currently authenticated user is either an Admin or the author of the Post, the Post can be deleted.

- Admins should still not be allowed to update other user's postEntities.

## Post author can delete comments on the post

- if the currently authenticated user is the owner of either the Post or the Comment, the Comment can be deleted.

- Post authors should still not be allowed to update other user's comments on their Posts.

## Banned Users

- admins can ban users. set a flag on the user Entity to banned=true

## Blocked Users

- Users can block other users from seeing or interacting with their account.

## Report Posts/Comments

- Users can report other postEntities or comments. If a post/comment is reported enough, it should be hidden or deleted.

## Profanity Filter 

- User can set a flag to mask expletives

## Parental Accounts

- User can be a "parent" of another user. Parents can change and disable settings on the "child" account (like Profanity Filter)

## Scheduled Posts/Comments

- User can choose to post a Post or Comment at a certain time in the future.

## Direct Messages

## Post Sharing / Re-posts

# Tech Feature Ideas

- Custom exception handling

- @ControllerAdvice for exception handling

- Request validation

- Pagination

- CSRF protection

- CORS enabled

- Secret Management

- Docker

- Env-specific Configuration

- Swagger API Documentation

# Tech Design Notes

- JPA vs. JDBC? maybe fork this repo and create a version with JDBC.