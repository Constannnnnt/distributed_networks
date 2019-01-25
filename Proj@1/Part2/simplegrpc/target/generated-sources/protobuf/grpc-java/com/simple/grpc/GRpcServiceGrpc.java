package com.simple.grpc;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 * <pre>
 * Defining a Service, a Service can have multiple RPC operations
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.7.0)",
    comments = "Source: GRpcService.proto")
public final class GRpcServiceGrpc {

  private GRpcServiceGrpc() {}

  public static final String SERVICE_NAME = "com.simple.grpc.GRpcService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.simple.grpc.GRpcServiceOuterClass.DataRequest,
      com.simple.grpc.GRpcServiceOuterClass.DataResponse> METHOD_SEND_DATA =
      io.grpc.MethodDescriptor.<com.simple.grpc.GRpcServiceOuterClass.DataRequest, com.simple.grpc.GRpcServiceOuterClass.DataResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.simple.grpc.GRpcService", "sendData"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.simple.grpc.GRpcServiceOuterClass.DataRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.simple.grpc.GRpcServiceOuterClass.DataResponse.getDefaultInstance()))
          .setSchemaDescriptor(new GRpcServiceMethodDescriptorSupplier("sendData"))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static GRpcServiceStub newStub(io.grpc.Channel channel) {
    return new GRpcServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static GRpcServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new GRpcServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static GRpcServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new GRpcServiceFutureStub(channel);
  }

  /**
   * <pre>
   * Defining a Service, a Service can have multiple RPC operations
   * </pre>
   */
  public static abstract class GRpcServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Define a RPC operation
     * </pre>
     */
    public void sendData(com.simple.grpc.GRpcServiceOuterClass.DataRequest request,
        io.grpc.stub.StreamObserver<com.simple.grpc.GRpcServiceOuterClass.DataResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_SEND_DATA, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_SEND_DATA,
            asyncUnaryCall(
              new MethodHandlers<
                com.simple.grpc.GRpcServiceOuterClass.DataRequest,
                com.simple.grpc.GRpcServiceOuterClass.DataResponse>(
                  this, METHODID_SEND_DATA)))
          .build();
    }
  }

  /**
   * <pre>
   * Defining a Service, a Service can have multiple RPC operations
   * </pre>
   */
  public static final class GRpcServiceStub extends io.grpc.stub.AbstractStub<GRpcServiceStub> {
    private GRpcServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private GRpcServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GRpcServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new GRpcServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Define a RPC operation
     * </pre>
     */
    public void sendData(com.simple.grpc.GRpcServiceOuterClass.DataRequest request,
        io.grpc.stub.StreamObserver<com.simple.grpc.GRpcServiceOuterClass.DataResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_SEND_DATA, getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * Defining a Service, a Service can have multiple RPC operations
   * </pre>
   */
  public static final class GRpcServiceBlockingStub extends io.grpc.stub.AbstractStub<GRpcServiceBlockingStub> {
    private GRpcServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private GRpcServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GRpcServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new GRpcServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Define a RPC operation
     * </pre>
     */
    public com.simple.grpc.GRpcServiceOuterClass.DataResponse sendData(com.simple.grpc.GRpcServiceOuterClass.DataRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_SEND_DATA, getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * Defining a Service, a Service can have multiple RPC operations
   * </pre>
   */
  public static final class GRpcServiceFutureStub extends io.grpc.stub.AbstractStub<GRpcServiceFutureStub> {
    private GRpcServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private GRpcServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GRpcServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new GRpcServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Define a RPC operation
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.simple.grpc.GRpcServiceOuterClass.DataResponse> sendData(
        com.simple.grpc.GRpcServiceOuterClass.DataRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_SEND_DATA, getCallOptions()), request);
    }
  }

  private static final int METHODID_SEND_DATA = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final GRpcServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(GRpcServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEND_DATA:
          serviceImpl.sendData((com.simple.grpc.GRpcServiceOuterClass.DataRequest) request,
              (io.grpc.stub.StreamObserver<com.simple.grpc.GRpcServiceOuterClass.DataResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class GRpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    GRpcServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.simple.grpc.GRpcServiceOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("GRpcService");
    }
  }

  private static final class GRpcServiceFileDescriptorSupplier
      extends GRpcServiceBaseDescriptorSupplier {
    GRpcServiceFileDescriptorSupplier() {}
  }

  private static final class GRpcServiceMethodDescriptorSupplier
      extends GRpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    GRpcServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (GRpcServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new GRpcServiceFileDescriptorSupplier())
              .addMethod(METHOD_SEND_DATA)
              .build();
        }
      }
    }
    return result;
  }
}
