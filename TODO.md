# Random TODOs

TODO: add created_at & updated_at columns

TODO: replace Entities with DTOs in controller (decouple API contract and persistence layer)

TODO: posts/comments can only be created/updated/deleted by the owner (currently auth'd user must have same userId)

- "throw new UnauthorizedOperationException" in service class. catch with @ControllerAdvice and return 401.

TODO: custom RecordNotFoundException- should be able to pass in entity type & id #

TODO: change ints to Longs for ids

TODO: return proper response codes / ResponseEntities from controller endpoints

TODO: "getCommentsByPostId", "getCommentsByUserId" - these are more idiomatic (instead of "getPostsComments")

TODO: add user bio, email, other data

TODO: endpoint to update user info (separate from register user. should go in UserController/Service)

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

# Tech Feature Ideas

- Custom exception handling

- @ControllerAdvice for exception handling

- Request validation

- CSRF protection

- Secret Management

- Docker

- Env-specific Configuration

- Swagger API Documentation

# Tech Design Notes

