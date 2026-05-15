#!/bin/bash

echo "Here"

set -e


mvn clean install

echo "Stopping tomcat"
service tomcat7 stop

echo "Setting up variables"

CATALINA_HOME='/usr/share/tomcat7-codedeploy'
DEPLOY_TO_ROOT='true'
#CONTEXT_PATH='##CONTEXT_PATH##'
SERVER_HTTP_PORT='80'

# JAVA_OPTS='${JAVA_OPTS} -Xms512m -Xmx512m -XX:PermSize=256m -XX:MaxPermSize=512m'

TEMP_STAGING_DIR='/home/work/tracking/web/target'
WAR_STAGED_LOCATION="$TEMP_STAGING_DIR/tracking-rest-app.war"

# In Tomcat, ROOT.war maps to the server root
if [[ "$DEPLOY_TO_ROOT" = 'true' ]]; then
    CONTEXT_PATH='ROOT'
fi

# Remove unpacked application artifacts
if [[ -f $CATALINA_HOME/webapps/$CONTEXT_PATH.war ]]; then
    rm $CATALINA_HOME/webapps/$CONTEXT_PATH.war
fi
if [[ -d $CATALINA_HOME/webapps/$CONTEXT_PATH ]]; then
    rm -rfv $CATALINA_HOME/webapps/$CONTEXT_PATH
fi

echo "Copy project war"

# Copy the WAR file to the webapps directory
cp $WAR_STAGED_LOCATION $CATALINA_HOME/webapps/$CONTEXT_PATH.war

# Configure the Tomcat server HTTP connector
# { which xsltproc; } || { yum install xsltproc; } || { yum install xsltproc; }
# cp $CATALINA_HOME/conf/server.xml $CATALINA_HOME/conf/server.xml.bak
# xsltproc $HTTP_PORT_CONFIG_XSL_LOCATION $CATALINA_HOME/conf/server.xml.bak > $CATALINA_HOME/conf/server.xml

echo "Starting tomcat"
service tomcat7 start

