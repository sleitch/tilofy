
Tilofy - Test App
-

*NB: Only thing you MUST change is the  path to saved images (see below)*

*NB: Intellij - you might have to "update" the dependencies if you see this message:*

The following repositories used in your gradle projects were not indexed yet:
            http://repo1.maven.org/maven2
            If you want to use dependency completion for these repositories artifacts,
            Open Repositories List, select required repositories and press "Update" button





Configure Temp file Directory
-
1. cd to project root directory
2. Open file: */Tilofy/src/main/resources/application.properties* 
3. Change: *resized.images.dir=D:\\tilofy_images* to point to your resized image location.
4. Note: this directory will be available to the WEB context to allow images to displayed on web page


Build/Run the App Command line
-
1. gradle clean build
1. cd to lib directory e.g. Tilofy\build\libs
2. java -jar Tilofy_Test-0.0.1-SNAPSHOT.war



Run in IDE (Intellij)
-

1. ...or right click on TilofyApplication.java and run as application


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

