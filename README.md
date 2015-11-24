
Tilofy - Test App
-


Build the App
-
1. cd to project root directory
2. Run command: gradle clean build

Run the App Command line
-
1. cd to lib directory e.g. Tilofy\build\libs
2. java -jar Tilofy_Test-0.0.1-SNAPSHOT.war



Run in Eclipse IDE
-

Otherwise you can just import the gradle project into eclipse and run as...

1. RIght click on projecyt and project and run on tomcat server. 
2. Right click on TilofyApplication.java and run as application


Test The App With Angular Page
-

1. Goto http://localhost:8080/index.html for a proof pof concept test page.

Test The App with JUnit
-

1. Run the ApplicationTest.java test cases.

Configuration
-

Configuration file is: */Tilofy/src/main/resources/application.properties*

To change location of new resized images edit the property:

resized.images.dir=D:\\tilofy_images

