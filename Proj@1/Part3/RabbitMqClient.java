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
        factory.setUsername("test");
        factory.setPassword("test");
        factory.setHost(ipaddress);
        factory.setPort(5672);

        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public static float[] doInsertionSort(float[] input) {
        float temp;
        for (int i = 1; i < input.length; i++) {
            for (int j = i; j > 0; j--) {
                if (input[j] < input[j - 1]) {
                    temp = input[j];
                    input[j] = input[j - 1];
                    input[j - 1] = temp;
                }
            }
        }
        return input;
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

    public static void main(String[] args) throws Exception {
        float[] time = new float[100];
        float totaltime = 0;
        try(RabbitMqClient client = new RabbitMqClient(args[0])) {
            int size = 100;
            byte[] data = {0x01};
            // byte[] data = new byte[10 * 1024 * 1024];
            // for (int i = 0; i < 10 * 1024 * 1024; i++) {
            //     data[i] = 0x01;
            // }
            for (int i = 0; i< size; i ++) {
                String stringData = new String(data);
                System.out.println(" [x] Sending Data");
                long start = System.nanoTime();
                String response = client.send(stringData);
                long end = System.nanoTime();
                float duration = end - start;
                time[i] = duration / 1000000000;
                totaltime += duration / 1000000000;
                System.out.println(" [.] Got '" + response + "'");
            }

        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("Average Time: " + (totaltime / 100) + "seconds");
        float[] sortedTime = doInsertionSort(time);
        System.out.println("10th Percentile: " + sortedTime[9] + "seconds");
        System.out.println("90th Percentile: " + sortedTime[89] + "seconds");
    }
}