## Optimized build

mvn -T 1C --batch-mode clean package
-Dmaven.wagon.http.ssl.insecure=true
-Dmaven.wagon.http.ssl.allowall=true
-Dmaven.wagon.http.ssl.ignore.validity.dates=true
-Dmaven.test.skip=true -Dmaven.javadoc.skip=true -B
-Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn


## Swagger

url: http://localhost:8081/swagger-ui/index.html