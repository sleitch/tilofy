
Tilofy - Test App
-

*NB: Only thing you MUST change is the  path to saved images (see below)*

 *(TODO: I should change that property to be system user.home , then you wouldn't even have to change that).*
 
 
Build the App
-
1. cd to project root directory
2. Open file: */Tilofy/src/main/resources/application.properties* 
3. Change: *resized.images.dir=D:\\tilofy_images* to point to your resized image location.
4. Run command: gradle clean build

Run the App Command line
-
1. cd to lib directory e.g. Tilofy\build\libs
2. java -jar Tilofy_Test-0.0.1-SNAPSHOT.war



Run in Eclipse IDE
-

Otherwise you can just import the gradle project into eclipse and run as...

1. Right click on project  and run on tomcat server. 
2. ...or right click on TilofyApplication.java and run as application


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

