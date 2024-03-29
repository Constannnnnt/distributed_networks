import com.rabbitmq.client.*;
import java.lang.RuntimeException;

public class RabbitMqServer {
    private static final String RPC_QUEUE_NAME = "rpc_queue";
    public static void main(String[] args) throws Exception {
        System.out.println("Start Server");
        ConnectionFactory factory = new ConnectionFactory();
        System.out.println("Connection to the Rabbit Broker at " + args[0]);
        factory.setUsername("test");
        factory.setPassword("test");
        factory.setHost(args[0]); // Specifiy the address of the Broker
        factory.setPort(5672);

        try(Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
            channel.queuePurge(RPC_QUEUE_NAME);

            channel.basicQos(1);

            System.out.println("[x] Awaiting RPC requests");

            Object monitor = new Object();

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(delivery.getProperties().getCorrelationId())
                    .build();

                String response = "";

                try {
                    String message = new String(delivery.getBody(), "UTF-8");

                } catch (RuntimeException e) {
                    System.out.println("[.] " + e.toString());
                } finally {
                    response = "Received the Ack";
                    channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProps, response.getBytes("UTF-8"));
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                    synchronized (monitor) {
                        monitor.notify();
                    }
                }
            };

            channel.basicConsume(RPC_QUEUE_NAME, false, deliverCallback, (consumerTag -> {}));
            // Wait and be prepared to consume the message from RPC client.
            while (true) {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}