# Api Key Manager API REST 

API Rest para la administración de keys para el uso en apis

## Docker Compose

// para levantar el entorno
$ docker-compose up

// para aplicar los cambios realizados 
$ docker-compose up --build

// para lanzar en modo debug
$ docker-compose -f docker-compose-dev.yml up 

// lanzar en modo debug y aplicar cambios relaizados
$ docker-compose -f docker-compose-dev.yml up --build

En modo debug quedará a la espera hasta que lancemos en eclipse para ello 

However, the application will not fully load until Eclipse’s remote debugger attaches to the application. To start remote debugging you click on Run > Debug Configurations …

Select Remote Java Application then press the new button to create a configuration. In the Debug Configurations panel, you give the configuration a name, select the AtSea project and set the connection properties for host and the port to 5005. Click Apply > Debug.  

The appserver will start up.

https://www.docker.com/blog/spring-boot-development-docker/
 
## Sonarqube

./mvnw clean install -DskipTests

./mvnw sonar:sonar
