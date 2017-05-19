# Knowledge Grid — Python Execution Stack

## Quick Start

### Download and run an executable binary or war file

Download the latest release from https://github.com/kgrid/python-execution-stack/releases

Launch the executable jar (running on port 8080 by default):

```bash
./python-execution-stack-0.5.4.jar
```
or (in Windows, for example)
```bash
java -jar python-execution-stack-0.5.4.jar
```

To add a library URL (optional; see Configuration below):
```bash
./python-execution-stack-0.5.4.jar --library.url=https://kgrid.med.umich.edu/library
```

To change the port (optional; see Configuration below):
```bash
./python-execution-stack-0.5.4.jar --server.port=9090
```

For war file deployment, see your container  instructions, e.g., for Tomcat, just copy to `[/path/to/tomcat/home]/webapps`

#### Test using a built in object

The activator ships with a couple of simple, built-in knowledge objects for testing. You can see the list of built-in objects by going to `http://localhost:8080/shelf`.

You can try out the "Prescription Counter":

```bash
POST /knowledgeObject/ark:/prescription/counter/result HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Accept-Encoding: application/json
```
with body: `{"DrugIDs":"101 204 708 406 190"}`

For example, using `curl`:

```curl
curl --request POST \
  --url http://localhost:8080/knowledgeObject/ark:/prescription/counter/result \
  --header 'accept: application/json' \
  --header 'content-type: application/json' \
  --data ' {"DrugIDs":"101 204 708 406 190"}'
```

#### Adding an object

To add an object, use an HTTP PUT request (we use [Postman](https://www.getpostman.com/)):

```bash
PUT /shelf/ark:/hello/world HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Accept-Encoding: text/plain
```

with the knowledge object in the body:

```json
{
"metadata": {
   "title": "Hello World",
   "description": "Test object",
   "published": false
   },
   "uri": "ark:/hello/world",
"payload": {
   "content": "def execute(inputs):\n    name = inputs[\"name\"]\n    return \"Hello, \" + name\n\n#print execute({\"name\":\"Jerry\"})\n",
   "engineType": "Python",
   "functionName": "execute"
   },
"inputMessage": "<rdf:RDF xmlns:ot=\"http://uofm.org/objectteller/#\"\n         xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">\n    <rdf:Description rdf:about=\"http://uofm.org/objectteller/inputMessage\">\n        <ot:noofparams>1</ot:noofparams>\n        <ot:params>\n            <rdf:Seq>\n                <rdf:li>name</rdf:li>\n            </rdf:Seq>\n        </ot:params>\n    </rdf:Description>\n    <rdf:Description rdf:about=\"http://uofm.org/objectteller/bame/\">\n        <ot:datatype>STRING</ot:datatype>\n    </rdf:Description>\n</rdf:RDF>\n",
"outputMessage": "<rdf:RDF xmlns:ot=\"http://uofm.org/objectteller/\"\n  xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">\n  <rdf:Description rdf:about=\"http://uofm.org/objectteller/outputMessage\">\n    <ot:returntype>STRING</ot:returntype>\n  </rdf:Description>\n</rdf:RDF>\n"
}
```
The newly added knowledge object can be viewed at: http://localhost:8080/shelf/ark:/hello/world. 

You can test the object by `POST`ing a name to http://localhost:8080/knowledgeObject/ark:/hello/world/result. 

```curl
curl --request POST \
  --url http://localhost:8080/knowledgeObject/ark:/hello/world/result \
  --header 'content-type: application/json' \
  --header 'accept: application/json' \
  --data '{"name": "Ralph"}'
```

For more information, see the Kgrid [Authoring Manual](https://kgrid.gitbooks.io/authoring-ii/) and [Activation Manual](https://kgrid.gitbooks.io/execution-manual/)

#### Using your own shelf
To specify the location of the shelf (optional):
```bash
./python-execution-stack-0.5.4.jar --stack.shelf.path=/kgrid/python-execution-stack
```
To specify the shelf name (optional):
```bash
./python-execution-stack-0.5.4.jar --stack.shelf.name=shelf
```

The default shelf is in the java temp directory in a folder called 'shelf'. 
If knowledge object files are already in the shelf path in a folder with the shelf name 
they will be loaded onto the shelf automatically.

You can keep different shelves and point to them when starting up the activator. 
Shelf location can even be on a shared drive.

### To build from source code

    git clone https://github.com/kgrid/python-execution-stack.git

    cd python-execution-stack

### Build and deploy the execution stack as an executable jar file:

```bash
mvn clean package -Dpackaging=jar
./target/python-execution-stack-0.5.4.jar
```
    
### Build and deploy a standard war file

```bash
mvn clean package    # builds a .war file by default
mvn tomcat7:deploy   # tomcat server settings taken from ~/.m2/settings.xml 
```

## Configuration

### Default configuration

```properties
# Use this as a model for externally supplied properties/config
# see: https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html

# set profile based on system environment variable ('env' or 'ENV')
# if it exists -> loads application-${env}.properties from spring.config.location directory, or classpath by default
# spring.profiles.active=test

# server port if different from default (8080) - only for executable jar
#server.port=8080

# Context path - only for executable jar
#server.contextPath=/

# Optional - absolute path (if external library)
#library.url=http://dlhs-fedora-dev-a.umms.med.umich.edu:8080/ObjectTeller

# Default shelf location is current directory, location must be readable by process user, e.g. 'tomcat'
#stack.shelf.path=${java.io.tmpdir}

#shelf name if other than 'shelf'
#stack.shelf.name=shelf

# Disable JMX export of all endpoints or set unique-names-true
# if deploying multiple instances in the same JVM
#endpoints.jmx.unique-names=true
#endpoints.jmx.enabled=false
```

### Setting up external configuration 

> These are some example configurations. There are many many ways to configure the activator at the operating system or container level.
Consult your OS documentation, container documentation, or cloud provider documentation.
>
> For an overview of configuring Spring Boot applications see the [Spring Boot external configuration page](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html)

#### Configuring an Activator deployed as a service

Add a app.service file in a place owned by the user you want to run as (e.g. `/var/kgrid`):

```properties
# tomcat.service
  
# Systemd unit file for tomcat
# From https://www.digitalocean.com/community/tutorials/how-to-install-apache-tomcat-8-on-centos-7
[Unit]
Description=Apache Tomcat Web Application Container
After=syslog.target network.target
  
[Service]
Type=forking
  
Environment=JAVA_HOME=/usr/lib/jvm/jre
Environment=CATALINA_PID=/opt/tomcat/temp/tomcat.pid
Environment=CATALINA_HOME=/opt/tomcat
Environment=CATALINA_BASE=/opt/tomcat
Environment='CATALINA_OPTS=-Xms512M -Xmx1024M -server -XX:+UseParallelGC'
Environment='JAVA_OPTS=-Djava.awt.headless=true -Djava.security.egd=file:/dev/./urandom'
 
ExecStart=/opt/tomcat/bin/startup.sh
ExecStop=/bin/kill -15 $MAINPID
 
User=tomcat
Group=tomcat
UMask=0007
RestartSec=10
Restart=always
 
[Install]
WantedBy=multi-user.target
```
and add an `application.properties` file in the `config` subdirectory `/var/kgrid/config`. See the [Spring docs](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html) for more options.

```properties
# application.properties
#Use this as a model for externally supplied properties/config
 
# server port if different from default (8080) - only for executable jar
#server.port=8080
 
# Context path - only for executable jar
server.contextPath=/stack
 
# Optional - absolute path (if external library)
library.url=http://library.kgrid.org
 
stack.shelf.path=/var/kgrid/stack
```

#### Configuring an Activator deployed in tomcat

Basically, for per context configuration, add `stack.xml` file to `$CATALINA_BASE/conf/[enginename]/[hostname]/$APP.xml` (e.g. `/opt/tomcat/conf/Catalina/localhost/stack.xml`) for an app deployed at context `/stack` with the following contents.

```xml
<!-- stack.xml -->
 
<Context reloadable="true">
    <Parameter name="spring.config.location" value="/var/kgrid/config/stack/"/>
</Context>
```
Or, set an environment variable, `SPRING_CONFIG_LOCATION=/var/kgrid/config/stack/`. See the Stack Overflow question, [Externalizing Tomcat Webapp Config from War File](http://stackoverflow.com/questions/13956651/externalizing-tomcat-webapp-config-from-war-file) for other options.


Then add an application.properties file in `/var/kgrid/config/stack/` like this:

```properties
# application.properties
#Use this as a model for externally supplied properties/config
 
# server port if different from default (8080) - only for executable jar
#server.port=8080
 
# Context path - only for executable jar
server.contextPath=/stack
 
# Optional - absolute path (if external library)
library.url=http://library.kgrid.org
 
# the shelf will be at /var/kgrid/stack/shelf, tomcat user will new r+w to ../stack to create/update the shelf
stack.shelf.path=/var/kgrid/stack
```

Release version (milestone releases) are available here: https://github.com/kgrid/python-execution-stack/releases


![under construction](https://camo.githubusercontent.com/4a7cf94aedbd23c13cc2d75fdc3b2af5c816c208/687474703a2f2f7374617469632e646967672e636f6d2f7374617469632f696d616765732f6469676765722e676966)
