# Features

## Auth

### Register

Allows a visitor to create a user account using credentials they provide. 

MVP: store hashed pwd in a User table.

`POST /api/auth/register`

### Login

Authenticate a user to log them into their account.

`POST /api/auth/login`

## Users

!! differentiate btwn register/login and account creation.

UI page 1 = username/pwd -> POST /register

UI page 2 = account details -> PUT /users/1

### Get

Get profile info for a user.

If userId == currentUser and already auth'd, then provide all information. Otherwise, give only some info.

Should not return all user's posts/resources. 
Have separate api to fetch that info.

`GET /api/users/{userId}`

### Modify

Update user's profile info.

userId must == current logged-in user.

`PUT /api/users/{userId}`

### Delete

Delete all user's account info and login info - everything.

userId must == current logged-in user.

`DELETE /api/users/{userId}`

## Posts

### Create

User creates a new post.

`POST /api/users/{userId}/posts`

### Get

Get all of a user's posts.

`GET /api/users/{userId}/posts`

Get a specific post.

`GET /api/posts/{postId}`

### Modify

Edit a posts content or other attributes.

`PUT /api/posts/{postId}`

### Delete

Delete a post from the database.

`DELETE /api/posts/{postId}`

Delete all of a user's posts.

`DELETE /api/users/{userId}/posts`

## Comments

### Create

Create a new comment on a post.

Take the currently authenticated user as the userId.

`POST /api/posts/{postId}/comments`

### Get

Get all the comments on a post.

`GET /api/posts/{postId}/comments`

Get all of a user's comments.

`GET /api/users/{userId}/comments`

Get a specific comment.

`GET /api/comments/{commentId}`

### Modify

Update the content of a comment, or other attributes.

`PUT /api/comments/{commentId}`

### Delete

Delete a comment.

`DELETE /api/comments/[commentId}`

Delete all comments on a post.

Request must come from the author of the post.

`DELETE /api/posts/[postId}/comments`

Delete all of a user's comments.

userId and currently authenticated user must match.

`DELETE /api/users/{userId}/comments`

## TODO

TODO: add created_at & updated_at columns

## Feature Ideas

### Likes 

### Followers

### Public/Private Posts

### Disable Comments

should allow user to disable/enable comments on a post, even after the post has been live.

option to delete all comments on the post when disabling comments after post is live, or keep them.

- this allows the comments to "re-appear" if user decides to re-enable comments later.
- also allows for a "reset" functionality (similar to deleting all comments)

### Post Statistics

like a chart of likes over time, most popular posts, follower chart, etc.

### Business Account

### 2FA & 2FA Enrollment


# Design Notes

which endpoints should be exposed to public? need to think about how data will be sent as well.

send every comment along with the the whole post?
no that wouldn't make sense...

how would twitter do it?

make each ui component request each resource?
that way, if only showing the post container,
then you ony retrieve posts without their comments.
the comments container can asynchronously fetch comments
for that post.

maybe posts can have some "preview" comments - only the first couple of comments as a preview.

