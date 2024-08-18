This is the Backend of the reverse recipe search project.

It provides a REST-Api for reading, creating, changing and deleting data for recipes, users, products and surveys.

### Starting the project
*you need to be in the root of project for the commands to work*

To start the project locally simply run:
```bash
docker compose up -d
```

If you want to (re)build the containers run:
```bash
docker compose up --build -d
```

If instances of the containers are currently running and you whish to rebuild and restart them:
```bash
docker compose down && docker compose up --build -d
```

### Running tests
To run all tests use the already provided `test` goal form maven:
```bash
mvn test
```

To clean build all modules use:
```bash
mvn clean install
```

### login to a specific containers bash
list all running containers
```bash
docker ps
```

execute `/bin/bash` in the container
```bash
docker exec -it <id> bash
```
