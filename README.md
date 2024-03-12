## Overview  
This project repository has 4 sub projects.  
The three sub projects are 'MSA_REF_BIZ_ORDER', 'MSA_REF_BIZ_PAYMENT', 'MSA_REF_BIZ_STOCK' and 'MSA_REF_SAGA'  

## Java and Gradle installation  
Download jdk 17 and register bin path - https://www.azul.com/  
Download gradle 7.6 and register bin path - https://gradle.org/releases/  

## Kafka installation and configuration
Make directory C:\Project\CAA\ENV  
Download kafka 3.6.1 - https://downloads.apache.org/kafka/3.6.1/kafka_2.13-3.6.1.tgz in the made directory  
Change the directory name to shorter as below    
C:\Project\CAA\ENV\kafka361   
Configuration kafka log directory and zookeper data directory    
Open "C:\Project\CAA\ENV\kafka361\config\server.properties" file     
Modify or add(if not exist) "log.dirs=C://Project//CAA//ENV//kafka361//logs"    
Open "C:\Project\CAA\ENV\kafka361\config\zookeeper.properties" file   
Modify or add(if not exist) "dataDir=C://Project//CAA//ENV//kafka361//zookeeper-data"    

## Zookeeper and Kafka Run   
C:\Project\CAA\ENV\kafka361\bin\windows> .\zookeeper-server-start.bat ..\\..\config\zookeeper.properties  
C:\Project\CAA\ENV\kafka361\bin\windows> .\kafka-server-start.bat ..\\..\config\server.properties  

## Project Directory and Git clone  
Make directory C:\Project\CAA  
C:\Project\CAA> git clone https://github.com/architectstory/MSA_REF_PATTERN_SAGA.git 

# How to build and run application? (Order Microservice)  
C:\Project\CAA\MSA_REF_PATTERN_SAGA> .\gradlew.bat -p MSA_REF_BIZ_ORDER clean build  
C:\Project\CAA\MSA_REF_PATTERN_SAGA\MSA_REF_BIZ_ORDER\build\libs> ls  
C:\Project\CAA\MSA_REF_PATTERN_SAGA\MSA_REF_BIZ_ORDER\build\libs> java -jar MSA_REF_BIZ_ORDER.jar  
connect to http://localhost:8081/swagger-ui.html on screen.

# How to build and run application? (Payment Microservice)    
C:\Project\CAA\MSA_REF_PATTERN_SAGA> .\gradlew.bat -p MSA_REF_BIZ_PAYMENT clean build  
C:\Project\CAA\MSA_REF_PATTERN_SAGA\MSA_REF_BIZ_PAYMENT\build\libs> ls  
C:\Project\CAA\MSA_REF_PATTERN_SAGA\MSA_REF_BIZ_PAYMENT\build\libs> java -jar MSA_REF_BIZ_PAYMENT.jar  
connect to http://localhost:8082/swagger-ui.html on screen.  

# How to build and run application? (Stock Microservice)    
C:\Project\CAA\MSA_REF_PATTERN_SAGA> .\gradlew.bat -p MSA_REF_BIZ_STOCK clean build  
C:\Project\CAA\MSA_REF_PATTERN_SAGA\MSA_REF_BIZ_STOCK\build\libs> ls  
C:\Project\CAA\MSA_REF_PATTERN_SAGA\MSA_REF_BIZ_STOCK\build\libs> java -jar MSA_REF_BIZ_STOCK.jar  
connect to http://localhost:8083/swagger-ui.html on screen.
