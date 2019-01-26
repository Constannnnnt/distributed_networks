## Run GRpcServer
mvn -DskipTests package exec:java -Dexec.mainClass=com.simple.grpc.GRpcServer

After Running the Server, Mark down the IP address `ipaddress` where the server is running

## Run GRpcClient
mvn -DskipTests package exec:java -Dexec.mainClass=com.simple.grpc.GRpcClient -Dexec.args="ipaddress"

## Note
The port is specified as `50051` in the current implementation.