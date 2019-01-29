package com.simple.grpc;

import java.io.IOException;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.simple.grpc.GRpcServiceImpl;
import com.simple.grpc.GRpcServiceOuterClass.DataRequest;
import com.simple.grpc.GRpcServiceOuterClass.DataResponse;
import com.google.protobuf.ByteString;

public class GRpcClient {
    private static final Logger logger = Logger.getLogger(GRpcClient.class.getName());

    private final ManagedChannel channel;

    private final GRpcServiceGrpc.GRpcServiceBlockingStub blockingStub;

    /** Construct client connecting to gRPC server at {@code host:port}. */
    public GRpcClient(String ipAddress, int port) {
        ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder.forAddress(ipAddress, port).usePlaintext(true);
        System.out.println("Connecting to " + ipAddress + " at port " + port);
        channel = channelBuilder.build();
        blockingStub = GRpcServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }


    public void sendData(byte[] data) {
        ByteString message = ByteString.copyFrom(data);
        DataRequest request = DataRequest.newBuilder().setData(message).build();

        DataResponse response;
        try {
            response = blockingStub.sendData(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Result: " + response.getAck());
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

    public static void main(String[] args) throws Exception, IOException {
        GRpcClient client = new GRpcClient(args[0], 50051);
        float[] time = new float[100];
        float totaltime = 0;
        try {
            // byte[] data = {0x01};
            // client.sendData(data);
            int size = 100;
            byte[] data = {0x01};
            // byte[] data = new byte[10 * 1024 * 1024];
            // for (int i = 0; i < 10 * 1024 * 1024; i++) {
            //     data[i] = 0x01;
            // }
            for (int i = 0; i < size; i++) {
                String stringData = new String(data);
                System.out.println(" [x] Sending Data");
                long start = System.nanoTime();
                client.sendData(data);
                long end = System.nanoTime();
                float duration = end - start;
                time[i] = duration / 1000000000;
                System.out.println("At " + Integer.toString(i) + " " + Float.toString(time[i]));
                totaltime += duration / 1000000000;
                // System.out.println(" [.] Got '" + response + "'");
            }

            System.out.println("Average Time: " + (totaltime / 100) + "seconds");
            float[] sortedTime = doInsertionSort(time);
            System.out.println("10th Percentile: " + sortedTime[9] + "seconds");
            System.out.println("90th Percentile: " + sortedTime[89] + "seconds");
        } finally {
            client.shutdown();
        }
    }
}