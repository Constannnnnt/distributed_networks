package com.stream.grpc;

import java.io.IOException;

import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.concurrent.CountDownLatch;

import com.stream.grpc.GRpcServiceImpl;
import com.stream.grpc.GRpcServiceOuterClass.DataRequest;
import com.stream.grpc.GRpcServiceOuterClass.DataResponse;
import com.google.protobuf.ByteString;

public class GRpcClient {
    private static final Logger logger = Logger.getLogger(GRpcClient.class.getName());

    private final ManagedChannel channel;

    private final GRpcServiceGrpc.GRpcServiceStub stub;

    /** Construct client connecting to gRPC server at {@code host:port}. */
    public GRpcClient(String ipAddress, int port) {
        ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder.forAddress(ipAddress, port).usePlaintext(true)
                .maxInboundMessageSize(12 * 1024 * 1024);
        System.out.println("Connecting to " + ipAddress + " at port " + port);
        channel = channelBuilder.build();
        stub = GRpcServiceGrpc.newStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
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


    public void sendData(byte[] data) {

        final CountDownLatch latch = new CountDownLatch(1);

        float[] time = new float[100];
        float totaltime = 0;
        int size = 100;
        StreamObserver<DataResponse> responseObserver = new StreamObserver<DataResponse>() {
            @Override
            public void onNext(DataResponse response) {
                System.out.println(response.getAck());
                System.out.println("Run 100 experiemtns " + Integer.toString(response.getElapsed()) + " seconds");
            }

            public void onError(Throwable t) {
                System.out.println("OnError");
                t.printStackTrace();
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        };

        StreamObserver<DataRequest> requestObserver = stub.sendData(responseObserver);

        try {
            for (int k = 0; k < size; k++) {
                System.out.println(" [x] Sending " + Integer.toString(k+1) + "th Data");
                long start = System.nanoTime();
                ByteString byteData = null;
                byteData = ByteString.copyFrom(data);
                DataRequest request = DataRequest.newBuilder().setData(byteData).build();
                requestObserver.onNext(request);

                if (latch.getCount() == 0) {
                    // RPC completed or errored before we finished sending.
                    // Sending further requests won't error, but they will just be thrown away.
                    return;
                }
            }
        } catch (RuntimeException r) {
            requestObserver.onError(r);
            throw r;
        }

        requestObserver.onCompleted();

        try {
            latch.await(10, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
        }
            // Receiving happens asynchronously

            // long end = System.nanoTime();
            // float duration = end - start;
            // time[k] = duration / 1000000000;
            // totaltime += duration / 1000000000;
        // System.out.println("Average Time: " + (totaltime / 100) + "seconds");
        // float[] sortedTime = doInsertionSort(time);
        // System.out.println("10th Percentile: " + sortedTime[9] + "seconds");
        // System.out.println("90th Percentile: " + sortedTime[89] + "seconds");

    }

    public static void main(String[] args) throws Exception, IOException {
        GRpcClient client = new GRpcClient(args[0], 50051);
        byte[] data = new byte[10 * 1024 * 1024];
        for (int i = 0; i < 10 * 1024 * 1024; i++) {
            data[i] = 0x01;
        }

        client.sendData(data);
    }
}