package com.simple.grpc;

import io.grpc.stub.StreamObserver;
import com.simple.grpc.GRpcServiceOuterClass.DataRequest;
import com.simple.grpc.GRpcServiceOuterClass.DataResponse;

public class GRpcServiceImpl extends GRpcServiceGrpc.GRpcServiceImplBase {
    @Override
    public void sendData(DataRequest request,
            StreamObserver<DataResponse> responseObserver) {
        // HelloRequest has toString auto-generated.
        // System.out.println(request);

        // You must use a builder to construct a new Protobuffer object
        DataResponse response = DataResponse.newBuilder()
                .setAck(request.getData().toString()).build();

        // Use responseObserver to send a single response back
        responseObserver.onNext(response);

        // When you are done, you must call onCompleted.
        responseObserver.onCompleted();
    }
}