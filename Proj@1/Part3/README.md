# Run RabbitMq

We need three machines to set up the current experiemnt.
EC2: RabbitMq Broker
Google Cloud: RabbitMqServer
Local Machine: RabbitMqClient

## Compile

```bash
javac -cp amqp-client-5.6.0.jar RabbitMqServer.java RabbitMqClient.java
```

## Run the RabbitMq Broker at EC2

The public IP of EC2: `ip_ec2`

### Add new user

```bash
rabbitmqctl add_user test test
rabbitmqctl set_user_tags test administrator
rabbitmqctl set_permissions -p / test ".*" ".*" ".*"
```

### Start Service

```bash
sudo service rabbitmq-server start
```

### Stop Service

```bash
sudo service rabbitmq-server stop
```

## Run the RabbitMqServer at Google Cloud

```bash
java -cp .:amqp-client-5.6.0.jar:slf4j-api-1.7.25.jar:slf4j-simple-1.7.25.jar RabbitMqServer `ip_ec2`
```

## Run the RabbitMqClient at Local Machine

```bash
java -cp .:amqp-client-5.6.0.jar:slf4j-api-1.7.25.jar:slf4j-simple-1.7.25.jar RabbitMqClient `ip_ec2`
```