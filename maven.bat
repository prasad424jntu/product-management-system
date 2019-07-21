call mvn clean package -DskipTests
start java -jar product-csv-reader/target/product-csv-reader-0.0.1-SNAPSHOT.jar
start java -jar product-csv-writter/target/product-csv-writter-0.0.1-SNAPSHOT.jar