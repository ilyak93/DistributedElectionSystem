package protos;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.13.1)",
    comments = "Source: Paxos.proto")
public final class PaxosGreeterGrpc {

  private PaxosGreeterGrpc() {}

  public static final String SERVICE_NAME = "protos.PaxosGreeter";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<protos.Paxos.Init,
      protos.Paxos.Init> getUponReceivingInitMethod;

  public static io.grpc.MethodDescriptor<protos.Paxos.Init,
      protos.Paxos.Init> getUponReceivingInitMethod() {
    io.grpc.MethodDescriptor<protos.Paxos.Init, protos.Paxos.Init> getUponReceivingInitMethod;
    if ((getUponReceivingInitMethod = PaxosGreeterGrpc.getUponReceivingInitMethod) == null) {
      synchronized (PaxosGreeterGrpc.class) {
        if ((getUponReceivingInitMethod = PaxosGreeterGrpc.getUponReceivingInitMethod) == null) {
          PaxosGreeterGrpc.getUponReceivingInitMethod = getUponReceivingInitMethod = 
              io.grpc.MethodDescriptor.<protos.Paxos.Init, protos.Paxos.Init>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protos.PaxosGreeter", "UponReceivingInit"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.Paxos.Init.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.Paxos.Init.getDefaultInstance()))
                  .setSchemaDescriptor(new PaxosGreeterMethodDescriptorSupplier("UponReceivingInit"))
                  .build();
          }
        }
     }
     return getUponReceivingInitMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.Paxos.Prepare,
      protos.Paxos.Promise> getUponReceivingPrepareMethod;

  public static io.grpc.MethodDescriptor<protos.Paxos.Prepare,
      protos.Paxos.Promise> getUponReceivingPrepareMethod() {
    io.grpc.MethodDescriptor<protos.Paxos.Prepare, protos.Paxos.Promise> getUponReceivingPrepareMethod;
    if ((getUponReceivingPrepareMethod = PaxosGreeterGrpc.getUponReceivingPrepareMethod) == null) {
      synchronized (PaxosGreeterGrpc.class) {
        if ((getUponReceivingPrepareMethod = PaxosGreeterGrpc.getUponReceivingPrepareMethod) == null) {
          PaxosGreeterGrpc.getUponReceivingPrepareMethod = getUponReceivingPrepareMethod = 
              io.grpc.MethodDescriptor.<protos.Paxos.Prepare, protos.Paxos.Promise>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protos.PaxosGreeter", "UponReceivingPrepare"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.Paxos.Prepare.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.Paxos.Promise.getDefaultInstance()))
                  .setSchemaDescriptor(new PaxosGreeterMethodDescriptorSupplier("UponReceivingPrepare"))
                  .build();
          }
        }
     }
     return getUponReceivingPrepareMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.Paxos.Accept,
      protos.Paxos.Accepted> getUponReceivingAcceptMethod;

  public static io.grpc.MethodDescriptor<protos.Paxos.Accept,
      protos.Paxos.Accepted> getUponReceivingAcceptMethod() {
    io.grpc.MethodDescriptor<protos.Paxos.Accept, protos.Paxos.Accepted> getUponReceivingAcceptMethod;
    if ((getUponReceivingAcceptMethod = PaxosGreeterGrpc.getUponReceivingAcceptMethod) == null) {
      synchronized (PaxosGreeterGrpc.class) {
        if ((getUponReceivingAcceptMethod = PaxosGreeterGrpc.getUponReceivingAcceptMethod) == null) {
          PaxosGreeterGrpc.getUponReceivingAcceptMethod = getUponReceivingAcceptMethod = 
              io.grpc.MethodDescriptor.<protos.Paxos.Accept, protos.Paxos.Accepted>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protos.PaxosGreeter", "UponReceivingAccept"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.Paxos.Accept.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.Paxos.Accepted.getDefaultInstance()))
                  .setSchemaDescriptor(new PaxosGreeterMethodDescriptorSupplier("UponReceivingAccept"))
                  .build();
          }
        }
     }
     return getUponReceivingAcceptMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PaxosGreeterStub newStub(io.grpc.Channel channel) {
    return new PaxosGreeterStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PaxosGreeterBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new PaxosGreeterBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PaxosGreeterFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new PaxosGreeterFutureStub(channel);
  }

  /**
   */
  public static abstract class PaxosGreeterImplBase implements io.grpc.BindableService {

    /**
     */
    public void uponReceivingInit(protos.Paxos.Init request,
        io.grpc.stub.StreamObserver<protos.Paxos.Init> responseObserver) {
      asyncUnimplementedUnaryCall(getUponReceivingInitMethod(), responseObserver);
    }

    /**
     */
    public void uponReceivingPrepare(protos.Paxos.Prepare request,
        io.grpc.stub.StreamObserver<protos.Paxos.Promise> responseObserver) {
      asyncUnimplementedUnaryCall(getUponReceivingPrepareMethod(), responseObserver);
    }

    /**
     */
    public void uponReceivingAccept(protos.Paxos.Accept request,
        io.grpc.stub.StreamObserver<protos.Paxos.Accepted> responseObserver) {
      asyncUnimplementedUnaryCall(getUponReceivingAcceptMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getUponReceivingInitMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                protos.Paxos.Init,
                protos.Paxos.Init>(
                  this, METHODID_UPON_RECEIVING_INIT)))
          .addMethod(
            getUponReceivingPrepareMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                protos.Paxos.Prepare,
                protos.Paxos.Promise>(
                  this, METHODID_UPON_RECEIVING_PREPARE)))
          .addMethod(
            getUponReceivingAcceptMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                protos.Paxos.Accept,
                protos.Paxos.Accepted>(
                  this, METHODID_UPON_RECEIVING_ACCEPT)))
          .build();
    }
  }

  /**
   */
  public static final class PaxosGreeterStub extends io.grpc.stub.AbstractStub<PaxosGreeterStub> {
    private PaxosGreeterStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PaxosGreeterStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PaxosGreeterStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PaxosGreeterStub(channel, callOptions);
    }

    /**
     */
    public void uponReceivingInit(protos.Paxos.Init request,
        io.grpc.stub.StreamObserver<protos.Paxos.Init> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUponReceivingInitMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void uponReceivingPrepare(protos.Paxos.Prepare request,
        io.grpc.stub.StreamObserver<protos.Paxos.Promise> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUponReceivingPrepareMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void uponReceivingAccept(protos.Paxos.Accept request,
        io.grpc.stub.StreamObserver<protos.Paxos.Accepted> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUponReceivingAcceptMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class PaxosGreeterBlockingStub extends io.grpc.stub.AbstractStub<PaxosGreeterBlockingStub> {
    private PaxosGreeterBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PaxosGreeterBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PaxosGreeterBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PaxosGreeterBlockingStub(channel, callOptions);
    }

    /**
     */
    public protos.Paxos.Init uponReceivingInit(protos.Paxos.Init request) {
      return blockingUnaryCall(
          getChannel(), getUponReceivingInitMethod(), getCallOptions(), request);
    }

    /**
     */
    public protos.Paxos.Promise uponReceivingPrepare(protos.Paxos.Prepare request) {
      return blockingUnaryCall(
          getChannel(), getUponReceivingPrepareMethod(), getCallOptions(), request);
    }

    /**
     */
    public protos.Paxos.Accepted uponReceivingAccept(protos.Paxos.Accept request) {
      return blockingUnaryCall(
          getChannel(), getUponReceivingAcceptMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class PaxosGreeterFutureStub extends io.grpc.stub.AbstractStub<PaxosGreeterFutureStub> {
    private PaxosGreeterFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PaxosGreeterFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PaxosGreeterFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PaxosGreeterFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.Paxos.Init> uponReceivingInit(
        protos.Paxos.Init request) {
      return futureUnaryCall(
          getChannel().newCall(getUponReceivingInitMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.Paxos.Promise> uponReceivingPrepare(
        protos.Paxos.Prepare request) {
      return futureUnaryCall(
          getChannel().newCall(getUponReceivingPrepareMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.Paxos.Accepted> uponReceivingAccept(
        protos.Paxos.Accept request) {
      return futureUnaryCall(
          getChannel().newCall(getUponReceivingAcceptMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_UPON_RECEIVING_INIT = 0;
  private static final int METHODID_UPON_RECEIVING_PREPARE = 1;
  private static final int METHODID_UPON_RECEIVING_ACCEPT = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final PaxosGreeterImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(PaxosGreeterImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_UPON_RECEIVING_INIT:
          serviceImpl.uponReceivingInit((protos.Paxos.Init) request,
              (io.grpc.stub.StreamObserver<protos.Paxos.Init>) responseObserver);
          break;
        case METHODID_UPON_RECEIVING_PREPARE:
          serviceImpl.uponReceivingPrepare((protos.Paxos.Prepare) request,
              (io.grpc.stub.StreamObserver<protos.Paxos.Promise>) responseObserver);
          break;
        case METHODID_UPON_RECEIVING_ACCEPT:
          serviceImpl.uponReceivingAccept((protos.Paxos.Accept) request,
              (io.grpc.stub.StreamObserver<protos.Paxos.Accepted>) responseObserver);
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

  private static abstract class PaxosGreeterBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    PaxosGreeterBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return protos.Paxos.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("PaxosGreeter");
    }
  }

  private static final class PaxosGreeterFileDescriptorSupplier
      extends PaxosGreeterBaseDescriptorSupplier {
    PaxosGreeterFileDescriptorSupplier() {}
  }

  private static final class PaxosGreeterMethodDescriptorSupplier
      extends PaxosGreeterBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    PaxosGreeterMethodDescriptorSupplier(String methodName) {
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
      synchronized (PaxosGreeterGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new PaxosGreeterFileDescriptorSupplier())
              .addMethod(getUponReceivingInitMethod())
              .addMethod(getUponReceivingPrepareMethod())
              .addMethod(getUponReceivingAcceptMethod())
              .build();
        }
      }
    }
    return result;
  }
}
