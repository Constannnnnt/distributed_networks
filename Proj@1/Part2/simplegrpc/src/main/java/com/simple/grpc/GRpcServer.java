package com.simple.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.netty.NettyServerBuilder;

import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.InetSocketAddress;

import java.io.IOException;
import java.util.logging.Logger;

import com.simple.grpc.GRpcServiceImpl;
import com.simple.grpc.GRpcServiceOuterClass.DataRequest;
import com.simple.grpc.GRpcServiceOuterClass.DataResponse;

public class GRpcServer {
    private Server server;
    private static final Logger logger = Logger.getLogger(GRpcServer.class.getName());

    private void start() throws IOException {
        /* The port on which the server should run */
        int port = 50051;
        server = NettyServerBuilder.forAddress(new InetSocketAddress(InetAddress.getLocalHost(), port)).maxInboundMessageSize(12 * 1024 * 1024).addService(new GRpcServiceImpl()).build().start();

        logger.info("Server started, listening on " + InetAddress.getLocalHost() + " at port " + port);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("Shutting down gRPC server since JVM is shutting down");
                GRpcServer.this.stop();
                System.err.println("Server shut down");
            }
        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon
     * threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws Exception {
        final GRpcServer server = new GRpcServer();
        server.start();
        server.blockUntilShutdown();
    }
}