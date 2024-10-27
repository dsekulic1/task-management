# Task Management API with Notifications
## Overview
This project is a RESTful API for a task management system developed using Spring Boot. It allows users to create, update, delete, and manage tasks. It also has a notification system based on task importance and status, as well as scheduled jobs to do regular checks.
___

## Features
- Task CRUD operations: creating, reading, updating, deleting, and marking tasks as completed.
- Task Assignment Notification: Send email notifications whenever tasks are assigned.
- Scheduled Job (Cron): Notify users according to job priority and status.
- Configurable Notifications: Admins can specify which task statuses should generate notifications.
- Sorting and Filtering: Sort tasks by priority or due date, then filter by status.
- Pagination: Handle large datasets efficiently during task retrieval.

## Technologies Used
- Spring Boot Framework: For building the API.
- JPA/Hibernate: For Data persistence.
- H2 Database: In-memory database.
- Spring Mail: Send email notifications.
- Spring Scheduler: Periodic job execution.
- Lombok: Reduce boilerplate code.
- OpenAPI 3.0/Swagger: For generating interactive API documentation.
- JUnit and Mockito: Unit testing for services and repositories.

## Authentication
- Basic authentication is implemented using Spring Security.

## Endpoints

| HTTP Method | Endpoint                            | Description                                            |
|-------------|-------------------------------------|--------------------------------------------------------|
| POST        | /api/v1/tasks                       | Create a new task                                      |
| GET         | /api/v1/tasks                       | Retrieve all tasks with optional filtering and sorting |
| PUT         | /api/v1/tasks/{id}                  | Update a specific task                                 |
| DELETE      | /api/v1/tasks/{id}                  | Delete a specific task                                 |
| PATCH       | /api/v1/tasks/{id}/complete         | Mark a specific task as completed                      |
| POST        | /api/v1/config/status-notifications | Configure which statuses trigger notifications         |

___
## How to Run the Project
1. Clone the repository:
   ```bash
   git clone https://github.com/dsekulic1/task-management.git
   ```

2. Change into the project directory:
   ```bash
   cd task-management-api
   ```
3. Build the Project: Ensure Maven is installed
   ```bash
   ./mvnw clean install or ./mvnw clean package
   ```

___
## Email Configuration 
Add email server configuration in `application.yml`:

```yaml
spring:
  mail:
    username: your-email@example.com
    password: your-app-password
     # Instructions for creating an app password
    note: "If you don't have an app password, visit: https://support.google.com/mail/answer/185833?hl=en for more information on how to create one."
```

## Notification Configuration
Use the `/config/status-notifications` endpoint to update which task statuses should trigger notifications. By default, it is set to trigger for all statuses except 'completed'.

```yaml
task:
   notification:
      low: ${NOTIFICATION_LOW:300000}   # Low priority notification interval (5 minutes)
      medium: ${NOTIFICATION_MEDIUM:180000} # Medium priority notification interval (3 minutes)
      high: ${NOTIFICATION_HIGH:60000}   # High priority notification interval (1 minute)
```

## Task Management Docker Configuration
To configure the task management in your Docker environment, add the following environment variables to your `docker-compose.yaml` file:

   ```yaml
   services:
      taskmanagement:
         ports:
         - "8282:8282"                   # The application will be available on port 8282
         environment:
            - MAIL_USERNAME=your-email@example.com
            - MAIL_PASSWORD=your-app-password
            # Adjust the following settings if needed
            - NOTIFICATION_LOW=300000       # Low priority notification interval (in milliseconds)
            - NOTIFICATION_MEDIUM=180000    # Medium priority notification interval (in milliseconds)
            - NOTIFICATION_HIGH=60000       # High priority notification interval (in milliseconds)
   ```

After updating the docker-compose.yaml file, you can build and start your services using the following commands:
```bash
  docker-compose build
  docker-compose up
```

---
## Design Decision
- Clean Architecture: The project is structured with separate layers for controllers, services, and repositories.
- DTOs for Data Transfer: Ensures encapsulation and clarity in data exchanges. These DTOs are generated using OpenAPI 3.0.
- Exception Handling: Meaningful error responses to ensure clarity for API consumers.

--- 
## Tests
Add email server configuration in `application-test.yml`:

```yaml
spring:
  mail:
    username: your-email@example.com
    password: your-app-password
     # Instructions for creating an app password
    note: "If you don't have an app password, visit: https://support.google.com/mail/answer/185833?hl=en for more information on how to create one."
```
Unit tests are written for the service and repository layers using JUnit and Mockito. Run the tests with:
```bash
./mvnw test
```

--- 
## Future Improvements
- Database migration
- Additional Scheduler: Introduce an additional scheduler to automatically mark task statuses as Overdue when their due dates expire.