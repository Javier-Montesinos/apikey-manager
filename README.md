# Api Key Manager API REST 

API Rest para la administración de keys para el uso en una API REST

## Run con Docker Compose

Ejecutar para levantar el entorno el siguiente comando desde consola:

```bash
$ docker-compose up
```

Para aplicar los cambios realizados y lanzar los contenedores
 
```bash
$ docker-compose up --build
```

### Acceso al contenedor de MySQL

Para poder acceder al contenedor de MySQL y verificar datos sobre la BD:

```bash
$ docker exec -it apikey-manager_db_1 mysql -urestapikeys_user  -p
$ use restapikeys
$ select * from apikeys;
```

## Debug con Docker Compose

Para lanzar en modo debug

```bash
$ docker-compose -f docker-compose-dev.yml up 
```

Lanzar en modo debug y aplicar cambios relaizados

```bash
$ docker-compose -f docker-compose-dev.yml up --build
```

En modo debug quedará a la espera hasta que lancemos en Eclipse el modo debug, para ello seguir los siguientes pasos obtenidos de [Spring Boot Development Docker](https://www.docker.com/blog/spring-boot-development-docker/):


>However, the application will not fully load until Eclipse’s remote debugger attaches to the application. To start remote debugging you click on Run > Debug Configurations …

>Select Remote Java Application then press the new button to create a configuration. In the Debug Configurations panel, you give the configuration a name, select the AtSea project and set the connection properties for host and the port to 5005. Click Apply > Debug.  

> The appserver will start up.

## Testing

Definida clase *ApiKeyRepositoryTest* para recoger test de la capa de datos, tan solo se prueban los dos métodos definidos. Ejecutar mediante:

```bash
$ ./mvnw test -Dtest=ApiKeyRepositoryTest
```

Se ha añadido dependencia de h2 para ejecutar los test de la capa de datos, definida configuraicón en */src/test/resources/application.properties*

También se contempla en la clase *ApiKeyServiceTest* tests de la capa de servicio mediante el uso de Mockito para la inyección de la dependencia *ApiKeyRepository* del servicio *ApiKeyService*. Ejecutar mediante:

```bash
$ ./mvnw test -Dtest=ApiKeySerivceTest
```

### Testing desde Eclipse o STS

También se puede ejecutar desde Eclipse seleccionando Run as a Junit Test sobre cada una de las clases de test.
 
## Sonarqube

Build del código saltando los tests.

```bash
./mvnw clean install -DskipTests
```

Ejecutar tareas de sonar mediante Maven.

```bash
./mvnw sonar:sonar
```