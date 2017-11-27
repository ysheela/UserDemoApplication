# UserDemoApplication


UserDemo Application is a sample applicaton that would allow users to add name and email address. It will also give the options to edit and delete teh attributes of a given user. It uses the spring boot framework with mongoDb as the database back end.

# How to build and run the project
To build, or run project `java` and `Maven` should be installed.

From the root folder of the project just run command: 

```bash
./mvnw spring-boot:run
```

If there is an error finding the MavenWrapperMain when running the above command, then please run the following command at the root folder prior to the above command.

```bash
mvn -N io.takari:maven:wrapper
```

## How to work the application
After application is started open the following into any browser [http://localhost:8095/](http://localhost:8095)

Application also provides `REST` API for retrieving all users or user by `userID`.  
To retrieve all users:
[http://localhost:8095/user/all](http://localhost:8095/user/all)   
To retrieve user by ID:   
[http://localhost:8095/user/user_id](http://localhost:8095/user/user_id)   
`user_id` should be replaced by real `ID` from the main page.

## Application Features
This application demonstrates:

* A complete application with ability to add, update and delete users with mongo as te database backend.
* Using AngularJS to access a backend RESTful service
* Techniques for showing multiple views of data.
* Custom Filter for filtering data
* Form validation using AngularJS
* A custom routing mechanism that allows a controller & template to be downloaded dynamically "on the fly"
