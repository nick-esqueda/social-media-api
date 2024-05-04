# TODOs

TODO: add GET /users, GET /posts, GET /comments with filtering, sorting, pagination

TODO: regex validation for firstName & lastName

TODO: return 204 No Content when returning empty list for getUsersPosts/Comments?

TODO: postman positive tests: verify created Entity is returned when requesting for all parent's records.

TODO: handle exception where Gender in request doesn't match any Enum value

TODO: handle other HttpMessageNotReadableExceptions (like DateTimeParseException)

TODO: reset password logic

TODO: DELETE /user - requires either JWT blacklisting or switching to session-based auth.

- JWT blacklist logic negates most of the benefits of JWT auth in the first place, since a DB/cache call needs to be made each request.
- however, the JWT blacklist storage would still be less, as it only needs to hold to revoked JWTs until their original expiry time.

TODO: log exceptions only once when they occur (logging aspect causes them to print for each method call)

TODO: username case insensitivity

TODO: use trim() on all user text inputs before persisting.

TODO: log 401s (when request is sent to protected endpoint without Authorization header)

TODO: handle ExpiredJwtException in JwtAuthFilter and return meaningful response message if possible.

TODO: decouple Role and GrantedAuthority (Role implements GrantedAuthority)

- this is coupling the repository layer with the security layer. they should not depend on each other.

TODO: create negative scenario postman API tests

TODO: parse JWT roles properly with a parser.

- check this: io.jsonwebtoken.RequiredTypeException: Cannot convert existing claim value of type 'class java.lang.String' to desired type 'class java.lang.Long'. JJWT only converts simple String, Date, Long, Integer, Short and Byte types automatically. Anything more complex is expected to be already converted to your desired type by the JSON Deserializer implementation. You may specify a custom Deserializer for a JwtParser with the desired conversion configuration via the JwtParserBuilder.deserializeJsonWith() method. See https://github.com/jwtk/jjwt#custom-json-processor for more information. If using Jackson, you can specify custom claim POJO types as described in https://github.com/jwtk/jjwt#json-jackson-custom-types
  at io.jsonwebtoken.impl.DefaultClaims.castClaimValue(DefaultClaims.java:169) ~[jjwt-impl-0.11.5.jar:0.11.5]
  at io.jsonwebtoken.impl.DefaultClaims.get(DefaultClaims.java:152) ~[jjwt-impl-0.11.5.jar:0.11.5]
  at com.nickesqueda.socialmediademo.security.JwtUtils.extractUserId(JwtUtils.java:56) ~[classes/:na]
  at com.nickesqueda.socialmediademo.security.JwtAuthFilter.doFilterInternal(JwtAuthFilter.java:35) ~[classes/:na]

TODO: handle io.jsonwebtoken.ExpiredJwtException (thrown when parsing JWT in JwtFilter)

TODO: logic for creating admin accounts. separate endpoint?

TODO: optimize number of DB queries.

- could use raw SQL with @Query

TODO: see if JPA .save() is necessary when updating (i.e. post.setContent(...))

TODO: JWT object for JWTs? instead of String. may help with serializing/deserializing

TODO: return Location header for POST requests?

# Business Feature Ideas

## Likes (Posts/Comments)

## Followers

## Saved Posts/Comments

## Replies (on comments)

## Notifications

## Post Sharing / Re-posts

## Reactions

- "like", "love", "sad", "angry", "laugh", "confused"
- alternative to likes?

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

## MFA Interdiction for Risky Behavior

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

## Last Login Time

# Tech Feature Ideas

- Request sanitization to encode special chars going into db and decode on the way out

- Password strength validation and feedback on UI

- Pagination

- CSRF protection

- CORS enabled

- Secret Management

- Docker

- Env-specific Configuration

- Swagger API Documentation

- Database migration tool

# Tech Design Notes

- JPA vs. JDBC? maybe fork this repo and create a version with JDBC.