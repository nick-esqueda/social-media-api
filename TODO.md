# TODOs

"Post" to "PostEntity" and etc. for all entities

- to differentiate better between Entities, DTOs, and other POJOs with other purposes.

TODO: add created_at & updated_at columns

TODO: AOP logging

TODO: custom RecordNotFoundException- should be able to pass in entity type & id #

TODO: create GET /user, PUT /user, DELETE /user

TODO: change ints to Longs for ids

TODO: return proper response codes / ResponseEntities from controller endpoints

TODO: "getCommentsByPostId", "getCommentsByUserId" - these are more idiomatic (instead of "getPostsComments")

TODO: add user bio, email, other data

TODO: endpoint to update user info (separate from register user. should go in UserController/Service)

TODO: handle io.jsonwebtoken.ExpiredJwtException (thrown when parsing JWT in JwtFilter)

TODO: logic for creating admin accounts. separate endpoint?

TODO: optimize number of DB queries.

TODO: see if JPA .save() is necessary when updating (i.e. postEntity.setContent(...))

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

like a chart of likes over time, most popular posts, follower chart, etc.

## Business Account

## 2FA & 2FA Enrollment

## Admin can delete other user's posts

- if the currently authenticated user is either an Admin or the author of the Post, the Post can be deleted.

- Admins should still not be allowed to update other user's posts.

## Post author can delete comments on the post

- if the currently authenticated user is the owner of either the Post or the Comment, the Comment can be deleted.

- Post authors should still not be allowed to update other user's comments on their Posts.

## Banned Users

- admins can ban users. set a flag on the user Entity to banned=true

## Blocked Users

- Users can block other users from seeing or interacting with their account.

## Report Posts/Comments

- Users can report other posts or comments. If a post/comment is reported enough, it should be hidden or deleted.

## Profanity Filter 

- User can set a flag to mask expletives

## Parental Accounts

- User can be a "parent" of another user. Parents can change and disable settings on the "child" account (like Profanity Filter)

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

