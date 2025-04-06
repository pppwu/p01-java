# Local Deployment Guide

This project contains a Spring Boot backend (Java) and a Vue.js frontend. Below are the steps for local deployment.

## Directory Structure

```
store/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/pta/store/StoreApplication.java  # Java backend entry point
│   │   ├── resources/
│   │   │   └── static                               # Static files generated from the build
│   │   └── vue/                                     # Vue frontend source code
```

## 1. Backend (Java) Deployment

a. **Install Java (JDK) and Maven**  
   Make sure Java 21 or higher and Maven are installed.

b. **Build and Run**  
   Navigate to the Java backend directory and run:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   The backend will run on http://localhost:8080.
   

## 2. Frontend (Vue) Deployment

a. **Install Node.js and npm**
   Install Node.js (v23+) and npm.
b. **Install Dependencies and Build**
   Navigate to the Vue directory and run:
   ```bash
   npm install
   npm run build
   ````
c. Serve Static Files

   The frontend will be available on http://localhost:8080.

   (The frontend static files (built with Vue.js) are served by the backend through the HomeController, which directly serves the index.html page. Please make sure backend service is running by CLI `mvn spring-boot:run`)

## 3. Common Issues
a. Backend Not Starting
b. Ensure Java and Maven are installed properly and check the configurations.

c. Frontend Build Errors
   Delete node_modules and reinstall:

   ```bash
   rm -rf node_modules
   npm install
   ```

   For any issues, please refer to the documentation or contact the development team.







