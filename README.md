# neo4j-exercise
This is a Neo4j Exercise


To Build:  mvn clean package


To run: target/bin/webapp (unix),  target/bin/webapp.bat (windows)


To configure database

Set environment variable neo4j_exercise_url = url to neo4j server and database (bolt protocol)

Set environment variable neo4j_exercise_user = username to connect to neo4j server 

Set environment variable neo4j_exercise_password = password to connect to neo4j server


API usage:

Create employee - POST to {url}/api/employees/employee/{id}/{name} - Where {url} is the url where the running API instance resides, {id} is the employee id, and {name} is the name of the employee


Get all employees GET request to {url}/api/employees/all - Where {url} is the url where the running API instance resides
