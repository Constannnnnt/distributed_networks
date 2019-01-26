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
        ManagedChannelBuilder<?> channelBuilder = ManagedChannelBuilder.forAddress(ipAddress, port).usePlaintext(true);
        System.out.println("Connecting to " + ipAddress + " at port " + port);
        channel = channelBuilder.build();
        stub = GRpcServiceGrpc.newStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }


    public void sendData(byte[] data) {

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<DataRequest> requestStreamObserver = stub
            .sendData(new StreamObserver<DataResponse>() {
                @Override
                public void onNext(DataResponse value) {
                    // We get a response from the server
                    System.out.println("Received a response from the server: " + value.getAck());
                    System.out.println(value.getAck());
                    // On next will be called only once
                }

                @Override
                public void onError(Throwable t) {
                    // we get an error from server
                }

                @Override
                public void onCompleted() {
                    // The server is done sending us data
                    // onCompleted will be called right after onNext()
                    System.out.println("Server Acks");
                    channel.shutdownNow();
                }
            });
        ByteString byteData = null;
        byteData = ByteString.copyFrom(data);
        DataRequest request = DataRequest.newBuilder().setData(byteData).build();
        System.out.println("before sending request");
        requestStreamObserver.onNext(request);
        System.out.println("Sent request");
        requestStreamObserver.onCompleted();
        System.out.println("Complete request");

        // for (byte d : data) {
        //     ByteString message = ByteString.copyFrom(data);
        //     DataRequest request = DataRequest.newBuilder().setData(message).build();
        //     requestStreamObserver.onNext(DataRequest.newBuilder().sendData(request).build());
        // }

    }

    public static void main(String[] args) throws Exception, IOException {
        GRpcClient client = new GRpcClient(args[0], 50051);
        try {
            byte[] data = {0x01};
            client.sendData(data);
        } finally {
            client.shutdown();
        }
    }
}