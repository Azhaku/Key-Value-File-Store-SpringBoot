# Key-Value Data Store

## Overview

This project implements a **Key-Value Data Store** service with the ability to persist data and support optional expiration (TTL). Built using **Spring Boot**, the service supports CRUD operations (Create, Read, Update, Delete), batch inserting key-value pairs, and TTL (Time-To-Live) functionality. The data is persisted in a JSON file, and expired entries are automatically removed based on TTL.

## Features

- **Create**: Add a new key-value pair with an optional TTL (time-to-live).
- **Read**: Retrieve the value associated with a given key.
- **Delete**: Remove a key-value pair from the store.
- **Batch Create**: Add multiple key-value pairs in a single request.
- **TTL Support**: Automatically remove key-value pairs once their TTL expires.
- **Persistence**: Key-value pairs are saved to and loaded from a JSON file to persist data across application restarts.

## Setup and Running the Project

### Prerequisites

- **Java 17+**
- **Maven**

### Clone the Repository

To get started, clone the repository to your local machine:

### Clone the repository

git clone https://github.com/Azhaku/Key-Value-File-Store-SpringBoot.git





### Build and Run

1. **Build the project**:

`mvn clean install`

2. **Run the application**:

`mvn spring-boot:run`


The application will run on `http://localhost:8100`

### Testing the API

You can test the following API endpoints using **Postman** or **curl**:

- **Create Key-Value Pair**: `POST /create?key=key1&value=value1&ttl=60`
- **Read Key-Value Pair**: `GET /read?key=key1`
- **Delete Key-Value Pair**: `DELETE /delete?key=key1`
- **Batch Create Key-Value Pairs**: `POST /batch-create` with a JSON body containing multiple key-value pairs.
 
### For Exterbal API tesing 

##### Create a key-value pair
curl -X POST "http://localhost:8100/create?key=key1&value=value1&ttl=60"

##### Read a key-value pair
curl "http://localhost:8100/read?key=key1"

##### Delete a key-value pair
curl -X DELETE "http://localhost:8100/delete?key=key1"

##### batch create
curl -X POST "http://localhost:8100/batch-create" -H "Content-Type: application/json" -d '{
  "key1": {"value": "value1", "ttl": 60},
  "key2": {"value": "value2", "ttl": 120}
}'




 
# Design Decisions
- **Persistence**: Data is saved in a JSON file for easy storage and retrieval using Gson.
- **TTL**: Key-value pairs have an optional TTL, and expired entries are automatically removed.
- **Concurrency**: Uses ConcurrentHashMap for thread-safe in-memory storage.
- **Logging**: Uses SLF4J and Logback for logging.




### Dependencies and Limitations
  
## Dependencies:

- Spring Boot
- Gson (for JSON serialization)
- SLF4J and Logback (for logging)

## Limitations
- ##### File Permissions:
  Ensure the application has the appropriate write permissions for the kvstore.json file.
- ##File Path:
   The default file path for the data store is kvstore.json, but this can be configured in application.properties.
- ## Concurrency Control:
  The application uses ConcurrentHashMap for in-memory storage. However, it may not fully cover all potential race conditions in more complex scenarios.

Operating System Compatibility: Works on Windows, Linux, and macOS.

- Ensure the application has write permissions for the kvstore.json file.
- File paths: The default file path is kvstore.json, configurable via application.properties.
