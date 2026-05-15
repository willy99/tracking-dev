##tracking-rest
==============

A restful tracking server is a JEE application

##Getting the code
You can clone the latest code onto your local machine with the credentials provided you by owner


##Building the application
Tracking uses Maven (http://maven.apache.org/) for building and packaging the project for deployment.
    mvn --version
    
To build the Web Application make sure you are in the project directory and type the following command:
    mvn clean install
    
This will create the deployable tracking.war file

##Deploying to Tomcat (Contiuous Integration/Development/Local)

Either using the Tomcat Deployment Manager UI or copying the WAR into the Tomcat webapps directory should be sufficent
to deploy onto Tomcat. If using Eclipse please refer to the instructions above about Building in Eclipse


## NOTE  DEV Enviroment args

web: java $JAVA_OPTS -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -jar target/dependency/webapp-runner.jar --port $PORT web/target/tracking-rest-app.war
worker: sh target/bin/webapp

