package com.stream.grpc;

import io.grpc.stub.StreamObserver;
import com.stream.grpc.GRpcServiceOuterClass.DataRequest;
import com.stream.grpc.GRpcServiceOuterClass.DataResponse;

public class GRpcServiceImpl extends GRpcServiceGrpc.GRpcServiceImplBase {
    @Override
    public StreamObserver<DataRequest> sendData(final StreamObserver<DataResponse> responseObserver) {
        return new StreamObserver<DataRequest>() {
            String result = "";

            @Override
            public void onNext(DataRequest request) {
                result += request.getData().toString();
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(DataResponse.newBuilder().setAck("ack").build());
                responseObserver.onCompleted();
            }
        };
    }
}