import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMqClient implements AutoCloseable {
    private Connection connection;
    private Channel channel;
    private String requestQueueName = "rpc_queue";

    public RabbitMqClient(String ipaddress) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        connectionFactory.setUsername("test");
        connectionFactory.setPassword("test");
        factory.setHost(ipaddress);

        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public String send(String message) throws IOException, InterruptedException {
        final String corrId = UUID.randomUUID().toString();

        String replyQueueName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().correlationId(corrId).replyTo(replyQueueName)
                .build();

        channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));

        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

        String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response.offer(new String(delivery.getBody(), "UTF-8"));
            }
        }, consumerTag -> {
        });

        String result = response.take();
        channel.basicCancel(ctag);
        return result;
    }

    public void close() throws IOException {
        connection.close();
    }

    public static void main(String[] args) {
        try(RabbitMqClient client = new RabbitMqClient(args[0])) {
            int size = 1;
            byte[] data = {0x01};
            for (int i = 0; i< size; i ++) {
                String stringData = new String(data);
                System.out.println(" [x] Sending Data");
                String response = client.send(stringData);
                System.out.println(" [.] Got '" + response + "'");
            }
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}