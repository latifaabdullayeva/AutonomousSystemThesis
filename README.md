# AutonomousSystemThesis
Master Thesis Project

* The paper related to the project is at [GitHub](https://github.com/latifaabdullayeva/Thesis)

# Setting up the server.
To set up the Spring server fo the first time perform the following instructions:
* Install JDK version 1.8.
* Install Maven. We used "brew" package manager on the macOS:
```
    brew install maven
```


# Setting up the database
To set up the server for the first time perform the following steps:
* Download pgAdmin tool EDB installer from the link:
    
    https://www.enterprisedb.com/downloads/postgres-postgresql-downloads.
    
    You can choose the alternative methods on how to install the postgres.
* When installing the database, specify the superusername and the password
    * Default port is 5432
    * Default hostname is localhost
* When connected to the postgresql server in pgAdmin, create a new database with 'autonomousSystem' name.
    The database name must match to the name specified in 'application.properties' file of a server.
    When you choose a custom name for a database, specify it in a server configuration file.
* Specify the database username and password in 'application.properties'.

