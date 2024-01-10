# cartCrud Project

This is the cartCrud project, a Java-based application. Below are the instructions on how to build and run this application using Docker.

## Prerequisites

Before you begin, ensure you have the following installed on your system:
- Docker
- Git (optional, for cloning the repository)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Clone the Repository (Optional)

If you haven't already, clone the project repository to your local machine:

```bash
git clone https://github.com/BiplopDey/cartCrud.git
cd cartCrud
```
### Running the Docker Container
Run the Docker image, the following command from the root directory of the project:
```bash
docker build -t cartcrud:latest .
docker run -p 8080:8080 cartcrud:latest
```

This command runs the cartcrud:latest image in a new container. It maps port 8080 of the container to port 8080 on your host machine, allowing you to access the application via localhost:8080.
