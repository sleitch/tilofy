
Tilofy - Test App
-


Build the App
-
1. cd to project root directory
2. Run command: gradle clean build

Build the App
-
1. cd to lib directory e.g. Tilofy\build\libs
2. java -jar Tilofy_Test-0.0.1-SNAPSHOT.war



Run in Eclipse IDE
-

Otherwise you can just import the gradle project into eclipse and run as...

1. RIght click on projecyt and project and run on tomcat server. 
2. Right click on TilofyApplication.java and run as application


Configuration
-

Configuration file is: */Tilofy/src/main/resources/application.properties*

To change location of new resized images edit the property:

resized.images.dir=D:\\tilofy_images

