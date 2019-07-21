# product-management-system

# Getting Started

-> This is a multi-module maven project with two microservices.

-> first is a reader service which reads the csv from root folder of the project and having name 'dataExample' (this can be improvised having the filepath in yml file. Also upload to S3 and read from there also can be done.). After read it send the data to kafka on topic 'product', Used spring batch as a csv reader.

-> Second microservice is a writter service which listens the data from kafka topic with name 'product' and moves the data as json to mongoDB (using mlab which is a 

DATABASE-AS-A-SERVICE) and this service also provides two end points.

		1) For listing the data.
		2) For listing the statistics.

Set Up:
------- 

-> run docor compose file kafka.yml for bringing up zookeeper and kafka.
    -> if any issues try manually bringing up them manually and also create topic 'product'

-> run the maven.bat file in the project root path to install the two jars and run them.

      -> Make sure maven is set in path. Make sure JAVA_HOME is set as environment variable pointing to jdk root folder.
      
Test:
-----

Once the services are up test with the following end points.

-> http://localhost:8081/pms/readCSV
     
        hit this service will read the csv file and send to message broker on topic 'product'

-> http://localhost:8080/pms/list

        hit this service to fetch all the products uploaded as of date.

->  http://localhost:8080/pms/stats      
      
        hit this service to get the statistics.
        
        
