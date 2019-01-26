package com.stream.grpc;

import io.grpc.stub.StreamObserver;
import com.stream.grpc.GRpcServiceOuterClass.DataRequest;
import com.stream.grpc.GRpcServiceOuterClass.DataResponse;

public class GRpcServiceImpl extends GRpcServiceGrpc.GRpcServiceImplBase {
    @Override
    public StreamObserver<DataRequest> sendData(final StreamObserver<DataResponse> responseObserver) {
        // we create the requestObserver that we'll return in this function
        StreamObserver<DataRequest> requestObserver = new StreamObserver<DataRequest>() {
            String result = "";

            @Override
            public void onNext(DataRequest r) {
                result = "ack";
                System.out.println("ack");
            }

            @Override
            public void onError(Throwable r) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(DataResponse.newBuilder()
                    .setAck(result)
                    .build()
                );

                System.out.println("gRpc Server Acks");

                responseObserver.onCompleted();
            }
        };
        return requestObserver;
    }
}