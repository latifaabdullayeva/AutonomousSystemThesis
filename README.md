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

# Setting up the Philips Hue bridge
To set up the Philips Hue bridge for the first time perform the the following steps:
* Visit the following website to get the internal ip address of the Philips Hue bridge:

https://discovery.meethue.com

* Visit the following website by using the above-mentioned ip address: 
```
https://<bridgeipaddress>/debug/clip.html
``` 
* Create a randomly generated username for your philips hue bridge by:
    * changing the URL to api/
    * inserting a Body such as "devicetype":"hueApp" 
    * pressing the POST button to create a new device
* The response that you will get is depicted in Figure A.1 in Appendix section of the thesis.
* Press the round button on your hue bridge device and then press the POST
button again on the website.
* As a response you will get a username that Philips Hue bridge created for you.

# Starting the server
To run the server for the first time perform the following steps:
* Start the server from AutonomousSystemThesis/server folder with the following command and the hibernate will generate all necessary tables for the system:
```
mvn spring-boot:run -e -X -Dspring-boot.run.arguments=–hueUsername=<hue_username>,–hueIPAddress=<hue_ip_address>,– musicFolderPath=<music_folder_path>
```
* Start the server broadcast with the following command:
```
dns-sd -R mythesis _socialiot._tcp local 8080
```
When running the server for the second time, you do not need to perform steps for setting up the database.

# Setting up the client applications
To set up the client application for the first time perform the following steps:
* Turn on the Bluetooth and the location on your tablet.
* Start the tablet application by running MyTabletApp.
* Grant the required permission for the tablet application.
* Register a tablet, a lamp, and the speakers in the system by following the instructions on the screen.
* Turn on the Bluetooth and the location on your smartphone.
* Start the mascot application by running MyMascotApp.
* Grant the required permission for the mascot application.
* Register your mascot in the system by following the instructions on the screen.
