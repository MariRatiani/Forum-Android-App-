[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/N3RLIwPA)

General Schemas for persistence

users
├── userId1
│   ├── displayname: "userone"
│   ├── username: "User1"
│   ├── image: "http://example.com/user1.jpg"
│   ├── posts: [reference(posts/postId1), reference(posts/postId2)]
├── userId2
│   ├── username: "User2"
│   ├── image: "http://example.com/user2.jpg"
│   ├── posts: [reference(posts/postId3)]

posts
├── postId1
│   ├── userId: reference(users/userId1)
│   ├── text: "This is a post"
│   ├── title: "A title"
│   ├── images: ["http://example.com/image1.jpg"]
│   ├── link: "http://example.com"
│   ├── category: reference(categories/categoryId1)
│   ├── subcategory: reference(categories/categoryId1/subcategories/subcategoryId1)
│   ├── createdAt: timestamp
│   ├── replies
│   │   ├── replyId1
│   │   │   ├── userId: reference(users/userId2)
│   │   │   ├── authorName: "User2"
│   │   │   ├── text: "This is a reply"
│   │   │   ├── createdAt: timestamp
├── postId2
│   ├── userId: reference(users/userId1)
│   ├── text: "This is another post"
│   ├── images: []
│   ├── links: []
│   ├── category: reference(categories/categoryId2)
│   ├── subcategory: reference(categories/categoryId2/subcategories/subcategoryId3)
│   ├── createdAt: timestamp

categories
├── categoryId1
│   ├── name: "General"
│   ├── subcategories
│   │   ├── subcategoryId1
│   │   │   ├── name: "Announcements"
│   │   ├── subcategoryId2
│   │   │   ├── name: "Introductions"
├── categoryId2
│   ├── name: "Technology"
│   ├── subcategories
│   │   ├── subcategoryId3
│   │   │   ├── name: "Software"
│   │   ├── subcategoryId4
│   │   │   ├── name: "Hardware"
