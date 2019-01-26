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

        final CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<DataResponse> responseObserver = new StreamObserver<DataResponse>() {
            @Override
            public void onNext(DataResponse response) {
                System.out.println(response.getAck());
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
            ByteString byteData = null;
            byteData = ByteString.copyFrom(data);
            DataRequest request = DataRequest.newBuilder().setData(byteData).build();
            for (int i = 0; i < 100; i++) {
                requestObserver.onNext(request);
            }
            if (latch.getCount() == 0) {
                // RPC completed or errored before we finished sending.
                // Sending further requests won't error, but they will just be thrown away.
                return;
            }
        } catch (RuntimeException r) {
            requestObserver.onError(r);
            throw r;
        }

        requestObserver.onCompleted();

        // for (byte d : data) {
        //     ByteString message = ByteString.copyFrom(data);
        //     DataRequest request = DataRequest.newBuilder().setData(message).build();
        //     requestStreamObserver.onNext(DataRequest.newBuilder().sendData(request).build());
        // }

    }

    public static void main(String[] args) throws Exception, IOException {
        GRpcClient client = new GRpcClient(args[0], 50051);
        byte[] data = new byte[100000];
        for (int i = 0; i < 100000; i++) {
            data[i] = 0x01;
        }
        client.sendData(data);
    }
}