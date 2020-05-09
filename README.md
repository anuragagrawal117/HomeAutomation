Google Doc: https://docs.google.com/document/d/1WnxEavg0R3NKRZimLPpamYDp2WfAZ_JvFXEIGVr4rXs/edit?usp=sharing

Home Automation System
System to remotely control devices at home using RESTful APIs.
 
Setup Instructions on Mac:
Install Java 8: https://docs.oracle.com/javase/8/docs/technotes/guides/install/mac_jdk.html
Install MongoDB: https://docs.mongodb.com/manual/tutorial/install-mongodb-on-os-x/
Install Apache Maven: http://maven.apache.org/install.html (or simply use maven wrapper mvnw present in the project)
cd into project folder and run:
mvn clean install (./mvnw clean install)
mvn spring-boot:run (./mvnw spring-boot:run). Now the server should be up on default port 8080.
 
System Design:
 
The system mainly has two models: DeviceConfiguration and Device. DeviceConfiguration(AAA Light) represents a type of device and Device(living room light) represents an actual physical instance of that type.
Each DeviceConfiguration has a DeviceBehavior associated which provides implementations to instantiate configuration and execute operations for that configuration.
DeviceConfigurations are instantiated automatically when we start the application. We can use Curl to check the two device configurations as below:
 
	Curl:
 
curl -X GET \
  http://localhost:8080/deviceConfigurations \
  -H 'cache-control: no-cache' \
  -H 'postman-token: ac25a1fc-6901-fb35-cea7-98d0f9f9e908'
 
Response:
 
{
    "_embedded": {
        "deviceConfigurations": [
            {
                "type": "Light_AAA_hg11",
                "description": "Lights can be turned on and off and adjusted for brightness.",
                "parameters": {
                    "max_brightness": 100,
                    "min_brightness": 0
                },
                "hardware_configuration": {
                    "model": "hg11",
                    "manufacturer": "AAA"
                },
                "default_attributes": {
                    "brightness": 60,
                    "on": false
                },
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/deviceConfigurations/5eb4dfca0229e04cf439e3c9"
                    },
                    "deviceConfiguration": {
                        "href": "http://localhost:8080/deviceConfigurations/5eb4dfca0229e04cf439e3c9"
                    }
                }
            },
            {
                "type": "Fan_SCC_492134",
                "description": "Fans can be turned on and off and have speed settings",
                "parameters": {
                    "available_speeds": [
                        "S1",
                        "S2",
                        "S3",
                        "S4",
                        "S5"
                    ]
                },
                "hardware_configuration": {
                    "model": "492134",
                    "manufacturer": "SCC"
                },
                "default_attributes": {
                    "speed": "S3",
                    "on": false
                },
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/deviceConfigurations/5eb4dfca0229e04cf439e3ca"
                    },
                    "deviceConfiguration": {
                        "href": "http://localhost:8080/deviceConfigurations/5eb4dfca0229e04cf439e3ca"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/deviceConfigurations{?page,size,sort}",
            "templated": true
        },
        "profile": {
            "href": "http://localhost:8080/profile/deviceConfigurations"
        },
        "search": {
            "href": "http://localhost:8080/deviceConfigurations/search"
        }
    },
    "page": {
        "size": 20,
        "totalElements": 2,
        "totalPages": 1,
        "number": 0
    }
}
 
 
Now we will create devices for the two configurations:
 
Curl:
 
curl -X POST \
  http://localhost:8080/devices \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: c962db46-5990-4413-d0f0-22fdd2e689b5' \
  -d '{
	"device_conf_type": "Fan_SCC_492134",
	"user_id": "UI01",
	"status": 1,
	"connection_id": "CI01",
	"user_provided_description": "Living room fan"
}'
 
	Response:
 
	{
    "status": "ONLINE",
    "attributes": {
        "speed": "S3",
        "on": false
    },
    "device_conf_type": "Fan_SCC_492134",
    "user_id": "UI01",
    "connection_id": "CI01",
    "user_provided_description": "Living room fan"
}
 
Curl:
 
curl -X POST \
  http://localhost:8080/devices \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: e8583251-6d78-80c2-0a69-18e540dee6fe' \
  -d '{
	"device_conf_type": "Fan_SCC_492134",
	"user_id": "UI01",
	"status": 1,
	"connection_id": "CI04",
	"user_provided_description": "Drawing room fan"
}'
 
Response:
 
{
    "id": "5eb6fd5510f25433bad899f8",
    "status": "ONLINE",
    "attributes": {
        "speed": "S3",
        "on": false
    },
    "device_conf_type": "Fan_SCC_492134",
    "user_id": "UI01",
    "connection_id": "CI04",
    "user_provided_description": "Drawing room fan"
}
 
	Curl:
 
	curl -X POST \
  http://localhost:8080/devices \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 5145cbd8-fe86-f7a5-8949-8af370a385d4' \
  -d '{
	"device_conf_type": "Light_AAA_hg11",
	"user_id": "UI01",
	"status": 1,
	"connection_id": "CI02",
	"user_provided_description": "Bedroom light"
}'
 
Response:
 
{
    "id": "5eb67d70dc8faa2560494655",
    "status": "ONLINE",
    "attributes": {
        "brightness": 60,
        "on": false
    },
    "device_conf_type": "Light_AAA_hg11",
    "user_id": "UI01",
    "connection_id": "CI02",
    "user_provided_description": "Bedroom light"
}
 
 
The devices take the default attributes from the configurations on instantiation. Later we will execute operations on the devices which will alter the device attributes(states) and return performed actions.
We can perform CRUD on the devices:
	Curl to get all devices:
 
	curl -X GET \
  http://localhost:8080/devices \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 75970728-ebd7-bc24-4e53-8dbedc1a170d'
 
Curl to get one device:
 
curl -X GET \
  http://localhost:8080/devices/5eb67d70dc8faa2560494655 \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 04881f95-86bd-b7b3-5a8a-58b34cec7c10'
 
Curl to update device:
 
curl -X PUT \
  http://localhost:8080/devices/5eb5d5e7837457244db668be \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 814c9dac-c39a-6a0b-fbf1-6ac41683d01f' \
  -d '{
	"device_conf_type": "Fan_SCC_492134",
	"user_id": "UI01",
	"status": "ONLINE",
	"connection_id": "CI01",
	"user_provided_description": "Living room fan2"
}'
 
Curl to delete device:
 
curl -X DELETE \
  http://localhost:8080/devices/5eb5d5e7837457244db668be \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 4844f25b-4273-1faa-d53a-03c9c836178a'
 
We will execute operations on the devices which will alter the device attributes(states) and return performed actions:
 
	Curl to set fan speed:
 
	curl -X POST \
  http://localhost:8080/devices/5eb6fd5510f25433bad899f8/execute \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: ffd7f3c8-dad1-399c-fa74-52c36fff4d2f' \
  -d '{
	"type": "SetFanSpeed",
	"params": {
		"speed": "S5"
	}
}'
 
Response:
 
[
    "fan switched On",
    "fan speed changed to S5"
]
 
Curl to switch off fan:
 
curl -X POST \
  http://localhost:8080/devices/5eb6fd5510f25433bad899f8/execute \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 1f4945c9-aa31-017e-9489-804caa31d228' \
  -d '{
	"type": "OnOff",
	"params": {
		"on": false
	}
}'
 
Response:
 
[
    "fan switched Off"
]
 
Curl to set light brightness:
 
curl -X POST \
  http://localhost:8080/devices/5eb67d70dc8faa2560494655/execute \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: e44b2ec1-b94a-2a49-2a33-5419f3b32842' \
  -d '{
	"type": "SetBrightness",
	"params": {
		"brightness": 30
	}
}'
 
Response:
 
[
    "light brightness changed to 30"
]
 
Curl to switch off light:
 
curl -X POST \
  http://localhost:8080/devices/5eb67d70dc8faa2560494655/execute \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: e3fad1f6-9b08-36cc-9094-d830880ca6c9' \
  -d '{
	"type": "OnOff",
	"params": {
		"on": false
	}
}'
 
Response:
 
[
    "light switched Off"
]
 
 
Why MongoDB?
 
MongoDB is a NoSQL database which uses JSON-like documents with schema. It is an object-oriented, simple, dynamic, and scalable NoSQL database.
Our models DeviceConfigurations and Devices are highly dynamic in nature and do not require relationships or joins.
System may become write-heavy due to operations required, another reason to go for NoSQL. 
