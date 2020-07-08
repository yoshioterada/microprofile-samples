# mongodb-quickstart project


## How to confirm the App


```
├── README.md
├── mongodb-quickstart.iml
├── pom.xml
└── src
    ├── main
    │   ├── docker
    │   │   ├── Dockerfile.jvm
    │   │   └── Dockerfile.native
    │   ├── java
    │   │   └── com
    │   │       └── yoshio3
    │   │           └── mongodb
    │   │               ├── Fruit.java
    │   │               ├── FruitResource.java
    │   │               ├── FruitService.java
    │   │               ├── ReactiveFruitResource.java
    │   │               └── ReactiveFruitService.java
    │   └── resources
    │       ├── META-INF
    │       │   └── resources
    │       │       └── index.html
    │       └── application.properties
    └── test
        └── java
            └── com
                └── yoshio3
                    └── mongodb
                        ├── FruitResourceTest.java
                        └── NativeFruitResourceIT.java

```

## Create CosmosDB

```bash
export COSMOSDB_NAME="yoshio3-cosmosdb"
export COSMOSDB_RES_GRP="CosmosDB"
export COSMOSDB_LOCATION="japaneast"
```

```azurecli
az cosmosdb create -n $COSMOSDB_NAME -g $COSMOSDB_RES_GRP --kind=MongoDB --locations regionName=$COSMOSDB_LOCATION failoverPriority=0 isZoneRedundant=False --enable-multiple-write-locations=False 
```

### Get the Connection String for CosmosDB

```azurecli
az cosmosdb keys list \
	-n $COSMOSDB_NAME \
	-g $COSMOSDB_RES_GRP \
	--type connection-strings \
	--query connectionStrings[0].connectionString \
	-o tsv
```

### Modified the Connection String on properties file (application.properties). 

```
# Configuration file
# key = value
quarkus.mongodb.connection-string=mongodb://yoshio3-cosmosdb:**************==@yoshio3-cosmosdb.mongo.cosmos.azure.com:10255/?ssl=true&replicaSet=globaldb
```

### Re-Build the Project

```bash
mvn clean package
```

### Run Application

```bash
java -jar target/mongodb-quickstart-1.0-SNAPSHOT-runner.jar 
```

or 

```bash
mvn quarkus:dev
```



## Confirm 

### Add Fruit  

```bash
curl -X POST \
	-H "Content-Type: application/json" \
	-d '{"name": "Peach","description": "The Color is Pink"}' \
	http://localhost:8080/fruits
```

### Get All of Fruits**  

```bash
curl http://localhost:8080/fruits
```


# Following is Original README.md

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `mongodb-quickstart-1.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/mongodb-quickstart-1.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/mongodb-quickstart-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.
