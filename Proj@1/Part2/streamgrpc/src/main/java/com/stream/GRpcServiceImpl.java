package com.stream.grpc;

import io.grpc.stub.StreamObserver;
import com.stream.grpc.GRpcServiceOuterClass.DataRequest;
import com.stream.grpc.GRpcServiceOuterClass.DataResponse;

public class GRpcServiceImpl extends GRpcServiceGrpc.GRpcServiceImplBase {
    @Override
    public StreamObserver<DataRequest> sendData(final StreamObserver<DataResponse> responseObserver) {
        return new StreamObserver<DataRequest>() {
            int result = 0;

            @Override
            public void onNext(DataRequest request) {
                result += 1;
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
                String ret = "Received " + str(result) + " data, Acks!";
                responseObserver.onNext(DataResponse.newBuilder(ret).setAck().build());
                responseObserver.onCompleted();
            }
        };
    }
}