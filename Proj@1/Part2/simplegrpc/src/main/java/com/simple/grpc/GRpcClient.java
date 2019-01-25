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