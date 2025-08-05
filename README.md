# Software Architectures and Platforms - Assignment 1

## Overview

This project is part of the Software Architectures and Platforms course (academic year 2024–2025). It involves the development of a prototype for the "EBike application" demo, transitioning from a Big Ball of Mud (BBoM) to a structured client-server architecture. The backend is designed using layered and microkernel architectural styles.

### Objectives

- **Client-Server Architecture** -> implement a frontend and backend to facilitate interaction between remote users and administrators
- **Backend Design** -> adopt layered and microkernel architecture styles for the backend
- **Functionalities** ->
  - User and administrator authentication
  - Real-time tracking of e-bike locations
  - Management of user profiles and ride histories
  - Administrative features for monitoring and analytics.

## Project Structure

The project is organized as follows:


├── database/ # Database schema and migration files
├── src/ # Source code for backend and frontend
│ ├── backend/ # Backend implementation
│ └── frontend/ # Frontend implementation
├── target/ # Compiled classes and build outputs
├── .gitignore # Git ignore file
├── pom.xml # Maven project configuration
└── README.md # Project documentation


## Prerequisites

Ensure you have the following installed:

- Java 17 or higher
- Maven 3.8.1 or higher
- Git
- Docker (optional, for containerized deployment)

## Setup and Installation

### 1. Clone the Repository

```bash
git clone https://github.com/FabioNotaro2001/SoftwareArchitecturesAndPlatform-Assignment1.git
cd SoftwareArchitecturesAndPlatform-Assignment1

2. Build the Project
mvn clean install


3. Run the Application

To start the backend:
mvn spring-boot:run

The backend will be accessible at http://localhost:8080.

To start the frontend:
cd src/frontend
npm install
npm start

The frontend will be accessible at http://localhost:3000.
4. Optional: Docker Deployment

To build and run the application using Docker:
docker-compose up --build

This will set up both the backend and frontend services.
Usage

    Frontend: Access the application through your web browser at http://localhost:3000.

    Backend API: The backend provides RESTful APIs accessible at http://localhost:8080/api.

Testing

Unit and integration tests are located in the src/test directory. To run the tests:
mvn test

Contributing

Contributions are welcome! Please fork the repository, create a new branch, and submit a pull request with your changes.
License

This project is licensed under the MIT License - see the LICENSE file for details.
Acknowledgements

    Spring Boot for backend development.

    React for frontend development.

    Docker for containerization.

    Maven for project management.
Contact

For any questions or feedback, please contact Fabio Notaro.
