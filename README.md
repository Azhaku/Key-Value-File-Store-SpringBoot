# Key-Value Data Store

## Overview

This project implements a **Key-Value Data Store** service with the ability to persist data and support optional expiration (TTL). Built using **Spring Boot**, the service allows for operations such as creating, reading, deleting, and batch inserting key-value pairs. It uses a JSON file for data persistence and handles TTL to automatically remove expired entries.

## Features

- **Create**: Add a new key-value pair with an optional TTL (time-to-live).
- **Read**: Retrieve the value for a given key.
- **Delete**: Remove a key-value pair.
- **Batch Create**: Add multiple key-value pairs in one operation.
- **TTL Support**: Key-value pairs can expire after a defined TTL.
- **Persistence**: Data is saved to and loaded from a JSON file, ensuring persistence across application restarts.






## Setup and Running the Project

### Prerequisites

- **Java 8+**
- **Maven**

### Clone the repository

git clone https://github.com/yourusername/kvstore-project.git cd kvstore-project

markdown
Copy code



### Build and Run

1. **Build the project**:

mvn clean install

markdown
Copy code

2. **Run the application**:

mvn spring-boot:run

bash
Copy code

The application will run on `http://localhost:8100`

### Testing the API

You can test the following API endpoints using **Postman** or **curl**:

- **Create Key-Value Pair**: `POST /create?key=key1&value=value1&ttl=60`
- **Read Key-Value Pair**: `GET /read?key=key1`
- **Delete Key-Value Pair**: `DELETE /delete?key=key1`
- **Batch Create Key-Value Pairs**: `POST /batch-create` with a JSON body containing multiple key-value pairs.
- 
### Example Commands

# Create a key-value pair
curl -X POST "http://localhost:8100/create?key=key1&value=value1&ttl=60"

# Read a key-value pair
curl "http://localhost:8100/read?key=key1"

# Delete a key-value pair
 curl -X DELETE "http://localhost:8100/delete?key=testKey"




 
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

Operating System Compatibility: Works on Windows, Linux, and macOS.

- Ensure the application has write permissions for the kvstore.json file.
- File paths: The default file path is kvstore.json, configurable via application.properties.
