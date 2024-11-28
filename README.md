# KvStore Service

## Overview

The **KvStore Service** is a simple, persistent key-value store service developed using Spring Boot. It allows you to manage key-value pairs with automatic expiration and file persistence. The data is stored in a JSON file, ensuring that the key-value pairs are maintained even after the application restarts.

This service supports operations such as adding, retrieving, and deleting key-value pairs. Additionally, it provides a batch operation to add multiple entries at once. The keys can also have an expiration time (TTL) after which they are automatically removed.

## Components

### 1. **KvStoreService**

The main service class that handles all key-value store operations:

- **create**: Adds a new key-value pair to the store. If a key already exists, it returns an error message.
- **read**: Retrieves the value associated with a key. It also checks if the key has expired based on the TTL (time-to-live) and removes expired keys.
- **delete**: Deletes a specific key-value pair from the store.
- **batchCreate**: Adds multiple key-value pairs at once. It also checks for size limits and key uniqueness before adding.

### 2. **FileUtil**

A utility class for handling file I/O operations:

- **loadFromFile**: Reads the key-value store data from a JSON file and loads it into memory (map).
- **saveToFile**: Saves the key-value pairs in the store to the JSON file to ensure data persistence.

### 3. **KvEntry**

A model class that represents an individual key-value pair, which may optionally include a TTL (time-to-live) expiration date. 

## Project Structure

The project consists of the following main components:

src ├── main │ └── java │ └── com/example/SpringProject1/Service/KvStoreService.java # Handles key-value store operations │ └── com/example/SpringProject1/FileUtill/FileUtil.java # File operations for saving/loading data ├── resources │ └── application.properties # Configuration file for setting properties


## Project Explanation

- The **KvStoreService** manages the business logic of the key-value store. It ensures the persistence and expiration management of key-value pairs.
- **FileUtil** is responsible for reading and writing data to and from a JSON file, ensuring data persists across application restarts.
- The **KvEntry** class holds each key-value pair and its optional TTL value.
- This service is designed for simplicity and scalability, allowing easy extension for additional features, such as more complex expiration policies or integration with databases.

