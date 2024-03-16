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
