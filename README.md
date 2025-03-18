# **Secret Santa Generator**

This project automates the Secret Santa assignment process for employees, ensuring unique pairings while avoiding duplicate assignments from the previous year. Built using **Java Spring Boot**, it provides a REST API to upload employee data and generate assignments.

## **Features**
- **CSV Upload**: Upload a CSV file containing employee details.
- **Unique Assignments**: Automatically generates unique Secret Santa assignments.
- **Avoid Repetition**: Takes previous year’s assignments into account to prevent repeat pairings.
- **Modular Design**: Backend implemented with Java (Spring Boot).
- **Error Handling**: Handles common errors like empty or invalid CSV files.
- **Testing & Documentation**: Includes tests and well-documented code.

### **Prerequisites**
Ensure you have the following installed:

- **Java 17+**
- **Maven**
- **Docker (Optional for containerized deployment)**

## **Getting Started**
## **Setup and Installation**
1. Clone the repository:
   ```sh
   git clone https://github.com/CharumathiRajaram/Secret_Santa_Generator.git
   cd Secret_Santa_Generator
   ```
2. Build the project:
   ```sh
   mvn clean install
   ```
3. Run the application:
   ```sh
   mvn spring-boot:run
   ```

The application will be available at `http://localhost:8080`.

---

## **API Endpoints**


### **1. Generate Secret Santa Assignments**

```
POST /secret_santa/assign
```

- Accepts `multipart/form-data` with a file.
- Upload a CSV file containing employee details and last year's assignments(optional)
- Generates new assignments based on the uploaded file.
- Returns the generated Secret Santa assignments as a CSV file.

## **Logging**

Logging is enabled using **SLF4J with Logback**. Logs are written to `logs/app.log`.

**Example Log Messages:**

```log
2025-03-19T00:09:15.544+05:30  INFO 7552 --- [secret_santa_api_service] [nio-8080-exec-2] .s.s.c.SecretSantaGeneratorAPIController : Received request to assign Secret Santa
2025-03-19T00:34:29.849+05:30 ERROR 23296 --- [secret_santa_api_service] [nio-8080-exec-4] .s.s.c.SecretSantaGeneratorAPIController : Unexpected Error: Failed to assign Secret Santa due to constraints. Please check input.
```

### **Configuration**

Modify `src/main/resources/application.properties`:

```properties
logging.file.name=logs/app.log
logging.level.root=INFO
```

---

## **Exception Handling**

Custom exception handling is implemented using **@ControllerAdvice**.

Example:

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<String> handleFileProcessingException(FileProcessingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File Processing Error: " + ex.getMessage());
    }
}
```

---

## **Docker Deployment**

You can deploy the application using Docker or Docker Compose.
### **1. Build Docker Image**

```sh
docker build -t secret-santa .
```

### **2. Run Container**

```sh
docker run -p 8080:8080 secret-santa
```
---

## **Using Docker Compose**
### **Build and Start Services**
```sh
docker-compose up --build
```
### **Run in Detached Mode**
```sh
docker-compose up -d --build
```
### **Stop and Remove Services**
```sh
docker-compose down
```
---

## **Contributing**

Feel free to contribute by opening an issue or submitting a pull request.Please make sure to follow the project’s code conventions and include tests for any new features or bug fixes.

## **License**

This project is licensed under the MIT License.

