package com.stream.grpc;

import io.grpc.stub.StreamObserver;
import com.stream.grpc.GRpcServiceOuterClass.DataRequest;
import com.stream.grpc.GRpcServiceOuterClass.DataResponse;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class GRpcServiceImpl extends GRpcServiceGrpc.GRpcServiceImplBase {
    @Override
    public StreamObserver<DataRequest> sendData(final StreamObserver<DataResponse> responseObserver) {
        return new StreamObserver<DataRequest>() {
            int result = 0;
            long startTime = System.nanoTime();

            @Override
            public void onNext(DataRequest request) {
                result += 1;
            }

            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onCompleted() {
                long seconds = NANOSECONDS.toSeconds(System.nanoTime() - startTime);
                String ret = "Received " + Integer.toString(result) + "chunks of 10MB data, Acks!";
                responseObserver.onNext(DataResponse.newBuilder().setAck(ret).setElapsed((int) seconds).build());
                responseObserver.onCompleted();
            }
        };
    }
}