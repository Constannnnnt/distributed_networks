## Run GRpcServer
mvn -DskipTests package exec:java -Dexec.mainClass=com.stream.grpc.GRpcServer -q

After Running the Server, Mark down the IP address `ipaddress` where the server is running

## Run GRpcClient
mvn -DskipTests package exec:java -Dexec.mainClass=com.stream.grpc.GRpcClient -Dexec.args="ipaddress" -q

## Note
The port is specified as `50051` in the current implementation.