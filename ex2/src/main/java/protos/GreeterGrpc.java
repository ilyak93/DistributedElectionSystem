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
    comments = "Source: Vote.proto")
public final class GreeterGrpc {

  private GreeterGrpc() {}

  public static final String SERVICE_NAME = "protos.Greeter";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<protos.Vote.VoteRequest,
      protos.Vote.VoteReply> getReceiveVoteMethod;

  public static io.grpc.MethodDescriptor<protos.Vote.VoteRequest,
      protos.Vote.VoteReply> getReceiveVoteMethod() {
    io.grpc.MethodDescriptor<protos.Vote.VoteRequest, protos.Vote.VoteReply> getReceiveVoteMethod;
    if ((getReceiveVoteMethod = GreeterGrpc.getReceiveVoteMethod) == null) {
      synchronized (GreeterGrpc.class) {
        if ((getReceiveVoteMethod = GreeterGrpc.getReceiveVoteMethod) == null) {
          GreeterGrpc.getReceiveVoteMethod = getReceiveVoteMethod = 
              io.grpc.MethodDescriptor.<protos.Vote.VoteRequest, protos.Vote.VoteReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protos.Greeter", "ReceiveVote"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.Vote.VoteRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.Vote.VoteReply.getDefaultInstance()))
                  .setSchemaDescriptor(new GreeterMethodDescriptorSupplier("ReceiveVote"))
                  .build();
          }
        }
     }
     return getReceiveVoteMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.Vote.StartElectionsRequest,
      protos.Vote.StartElectionsReply> getReceiveStartElectionsMethod;

  public static io.grpc.MethodDescriptor<protos.Vote.StartElectionsRequest,
      protos.Vote.StartElectionsReply> getReceiveStartElectionsMethod() {
    io.grpc.MethodDescriptor<protos.Vote.StartElectionsRequest, protos.Vote.StartElectionsReply> getReceiveStartElectionsMethod;
    if ((getReceiveStartElectionsMethod = GreeterGrpc.getReceiveStartElectionsMethod) == null) {
      synchronized (GreeterGrpc.class) {
        if ((getReceiveStartElectionsMethod = GreeterGrpc.getReceiveStartElectionsMethod) == null) {
          GreeterGrpc.getReceiveStartElectionsMethod = getReceiveStartElectionsMethod = 
              io.grpc.MethodDescriptor.<protos.Vote.StartElectionsRequest, protos.Vote.StartElectionsReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protos.Greeter", "ReceiveStartElections"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.Vote.StartElectionsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.Vote.StartElectionsReply.getDefaultInstance()))
                  .setSchemaDescriptor(new GreeterMethodDescriptorSupplier("ReceiveStartElections"))
                  .build();
          }
        }
     }
     return getReceiveStartElectionsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.Vote.EndElectionsRequest,
      protos.Vote.EndElectionsReply> getReceiveEndElectionsMethod;

  public static io.grpc.MethodDescriptor<protos.Vote.EndElectionsRequest,
      protos.Vote.EndElectionsReply> getReceiveEndElectionsMethod() {
    io.grpc.MethodDescriptor<protos.Vote.EndElectionsRequest, protos.Vote.EndElectionsReply> getReceiveEndElectionsMethod;
    if ((getReceiveEndElectionsMethod = GreeterGrpc.getReceiveEndElectionsMethod) == null) {
      synchronized (GreeterGrpc.class) {
        if ((getReceiveEndElectionsMethod = GreeterGrpc.getReceiveEndElectionsMethod) == null) {
          GreeterGrpc.getReceiveEndElectionsMethod = getReceiveEndElectionsMethod = 
              io.grpc.MethodDescriptor.<protos.Vote.EndElectionsRequest, protos.Vote.EndElectionsReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protos.Greeter", "ReceiveEndElections"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.Vote.EndElectionsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.Vote.EndElectionsReply.getDefaultInstance()))
                  .setSchemaDescriptor(new GreeterMethodDescriptorSupplier("ReceiveEndElections"))
                  .build();
          }
        }
     }
     return getReceiveEndElectionsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.Vote.VotesCountForPartyRequest,
      protos.Vote.VotesCountForPartyReply> getReceiveStatusMethod;

  public static io.grpc.MethodDescriptor<protos.Vote.VotesCountForPartyRequest,
      protos.Vote.VotesCountForPartyReply> getReceiveStatusMethod() {
    io.grpc.MethodDescriptor<protos.Vote.VotesCountForPartyRequest, protos.Vote.VotesCountForPartyReply> getReceiveStatusMethod;
    if ((getReceiveStatusMethod = GreeterGrpc.getReceiveStatusMethod) == null) {
      synchronized (GreeterGrpc.class) {
        if ((getReceiveStatusMethod = GreeterGrpc.getReceiveStatusMethod) == null) {
          GreeterGrpc.getReceiveStatusMethod = getReceiveStatusMethod = 
              io.grpc.MethodDescriptor.<protos.Vote.VotesCountForPartyRequest, protos.Vote.VotesCountForPartyReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "protos.Greeter", "ReceiveStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.Vote.VotesCountForPartyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.Vote.VotesCountForPartyReply.getDefaultInstance()))
                  .setSchemaDescriptor(new GreeterMethodDescriptorSupplier("ReceiveStatus"))
                  .build();
          }
        }
     }
     return getReceiveStatusMethod;
  }

  private static volatile io.grpc.MethodDescriptor<protos.Vote.VotesCountForPartyRequest,
      protos.Vote.VotesCountForPartyReply> getReciveVotesCountMethod;

  public static io.grpc.MethodDescriptor<protos.Vote.VotesCountForPartyRequest,
      protos.Vote.VotesCountForPartyReply> getReciveVotesCountMethod() {
    io.grpc.MethodDescriptor<protos.Vote.VotesCountForPartyRequest, protos.Vote.VotesCountForPartyReply> getReciveVotesCountMethod;
    if ((getReciveVotesCountMethod = GreeterGrpc.getReciveVotesCountMethod) == null) {
      synchronized (GreeterGrpc.class) {
        if ((getReciveVotesCountMethod = GreeterGrpc.getReciveVotesCountMethod) == null) {
          GreeterGrpc.getReciveVotesCountMethod = getReciveVotesCountMethod = 
              io.grpc.MethodDescriptor.<protos.Vote.VotesCountForPartyRequest, protos.Vote.VotesCountForPartyReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "protos.Greeter", "ReciveVotesCount"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.Vote.VotesCountForPartyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  protos.Vote.VotesCountForPartyReply.getDefaultInstance()))
                  .setSchemaDescriptor(new GreeterMethodDescriptorSupplier("ReciveVotesCount"))
                  .build();
          }
        }
     }
     return getReciveVotesCountMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static GreeterStub newStub(io.grpc.Channel channel) {
    return new GreeterStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static GreeterBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new GreeterBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static GreeterFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new GreeterFutureStub(channel);
  }

  /**
   */
  public static abstract class GreeterImplBase implements io.grpc.BindableService {

    /**
     */
    public void receiveVote(protos.Vote.VoteRequest request,
        io.grpc.stub.StreamObserver<protos.Vote.VoteReply> responseObserver) {
      asyncUnimplementedUnaryCall(getReceiveVoteMethod(), responseObserver);
    }

    /**
     */
    public void receiveStartElections(protos.Vote.StartElectionsRequest request,
        io.grpc.stub.StreamObserver<protos.Vote.StartElectionsReply> responseObserver) {
      asyncUnimplementedUnaryCall(getReceiveStartElectionsMethod(), responseObserver);
    }

    /**
     */
    public void receiveEndElections(protos.Vote.EndElectionsRequest request,
        io.grpc.stub.StreamObserver<protos.Vote.EndElectionsReply> responseObserver) {
      asyncUnimplementedUnaryCall(getReceiveEndElectionsMethod(), responseObserver);
    }

    /**
     */
    public void receiveStatus(protos.Vote.VotesCountForPartyRequest request,
        io.grpc.stub.StreamObserver<protos.Vote.VotesCountForPartyReply> responseObserver) {
      asyncUnimplementedUnaryCall(getReceiveStatusMethod(), responseObserver);
    }

    /**
     */
    public void reciveVotesCount(protos.Vote.VotesCountForPartyRequest request,
        io.grpc.stub.StreamObserver<protos.Vote.VotesCountForPartyReply> responseObserver) {
      asyncUnimplementedUnaryCall(getReciveVotesCountMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getReceiveVoteMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                protos.Vote.VoteRequest,
                protos.Vote.VoteReply>(
                  this, METHODID_RECEIVE_VOTE)))
          .addMethod(
            getReceiveStartElectionsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                protos.Vote.StartElectionsRequest,
                protos.Vote.StartElectionsReply>(
                  this, METHODID_RECEIVE_START_ELECTIONS)))
          .addMethod(
            getReceiveEndElectionsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                protos.Vote.EndElectionsRequest,
                protos.Vote.EndElectionsReply>(
                  this, METHODID_RECEIVE_END_ELECTIONS)))
          .addMethod(
            getReceiveStatusMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                protos.Vote.VotesCountForPartyRequest,
                protos.Vote.VotesCountForPartyReply>(
                  this, METHODID_RECEIVE_STATUS)))
          .addMethod(
            getReciveVotesCountMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                protos.Vote.VotesCountForPartyRequest,
                protos.Vote.VotesCountForPartyReply>(
                  this, METHODID_RECIVE_VOTES_COUNT)))
          .build();
    }
  }

  /**
   */
  public static final class GreeterStub extends io.grpc.stub.AbstractStub<GreeterStub> {
    private GreeterStub(io.grpc.Channel channel) {
      super(channel);
    }

    private GreeterStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GreeterStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new GreeterStub(channel, callOptions);
    }

    /**
     */
    public void receiveVote(protos.Vote.VoteRequest request,
        io.grpc.stub.StreamObserver<protos.Vote.VoteReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getReceiveVoteMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void receiveStartElections(protos.Vote.StartElectionsRequest request,
        io.grpc.stub.StreamObserver<protos.Vote.StartElectionsReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getReceiveStartElectionsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void receiveEndElections(protos.Vote.EndElectionsRequest request,
        io.grpc.stub.StreamObserver<protos.Vote.EndElectionsReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getReceiveEndElectionsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void receiveStatus(protos.Vote.VotesCountForPartyRequest request,
        io.grpc.stub.StreamObserver<protos.Vote.VotesCountForPartyReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getReceiveStatusMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void reciveVotesCount(protos.Vote.VotesCountForPartyRequest request,
        io.grpc.stub.StreamObserver<protos.Vote.VotesCountForPartyReply> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getReciveVotesCountMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class GreeterBlockingStub extends io.grpc.stub.AbstractStub<GreeterBlockingStub> {
    private GreeterBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private GreeterBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GreeterBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new GreeterBlockingStub(channel, callOptions);
    }

    /**
     */
    public protos.Vote.VoteReply receiveVote(protos.Vote.VoteRequest request) {
      return blockingUnaryCall(
          getChannel(), getReceiveVoteMethod(), getCallOptions(), request);
    }

    /**
     */
    public protos.Vote.StartElectionsReply receiveStartElections(protos.Vote.StartElectionsRequest request) {
      return blockingUnaryCall(
          getChannel(), getReceiveStartElectionsMethod(), getCallOptions(), request);
    }

    /**
     */
    public protos.Vote.EndElectionsReply receiveEndElections(protos.Vote.EndElectionsRequest request) {
      return blockingUnaryCall(
          getChannel(), getReceiveEndElectionsMethod(), getCallOptions(), request);
    }

    /**
     */
    public protos.Vote.VotesCountForPartyReply receiveStatus(protos.Vote.VotesCountForPartyRequest request) {
      return blockingUnaryCall(
          getChannel(), getReceiveStatusMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<protos.Vote.VotesCountForPartyReply> reciveVotesCount(
        protos.Vote.VotesCountForPartyRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getReciveVotesCountMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class GreeterFutureStub extends io.grpc.stub.AbstractStub<GreeterFutureStub> {
    private GreeterFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private GreeterFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GreeterFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new GreeterFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.Vote.VoteReply> receiveVote(
        protos.Vote.VoteRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getReceiveVoteMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.Vote.StartElectionsReply> receiveStartElections(
        protos.Vote.StartElectionsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getReceiveStartElectionsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.Vote.EndElectionsReply> receiveEndElections(
        protos.Vote.EndElectionsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getReceiveEndElectionsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<protos.Vote.VotesCountForPartyReply> receiveStatus(
        protos.Vote.VotesCountForPartyRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getReceiveStatusMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_RECEIVE_VOTE = 0;
  private static final int METHODID_RECEIVE_START_ELECTIONS = 1;
  private static final int METHODID_RECEIVE_END_ELECTIONS = 2;
  private static final int METHODID_RECEIVE_STATUS = 3;
  private static final int METHODID_RECIVE_VOTES_COUNT = 4;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final GreeterImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(GreeterImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_RECEIVE_VOTE:
          serviceImpl.receiveVote((protos.Vote.VoteRequest) request,
              (io.grpc.stub.StreamObserver<protos.Vote.VoteReply>) responseObserver);
          break;
        case METHODID_RECEIVE_START_ELECTIONS:
          serviceImpl.receiveStartElections((protos.Vote.StartElectionsRequest) request,
              (io.grpc.stub.StreamObserver<protos.Vote.StartElectionsReply>) responseObserver);
          break;
        case METHODID_RECEIVE_END_ELECTIONS:
          serviceImpl.receiveEndElections((protos.Vote.EndElectionsRequest) request,
              (io.grpc.stub.StreamObserver<protos.Vote.EndElectionsReply>) responseObserver);
          break;
        case METHODID_RECEIVE_STATUS:
          serviceImpl.receiveStatus((protos.Vote.VotesCountForPartyRequest) request,
              (io.grpc.stub.StreamObserver<protos.Vote.VotesCountForPartyReply>) responseObserver);
          break;
        case METHODID_RECIVE_VOTES_COUNT:
          serviceImpl.reciveVotesCount((protos.Vote.VotesCountForPartyRequest) request,
              (io.grpc.stub.StreamObserver<protos.Vote.VotesCountForPartyReply>) responseObserver);
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

  private static abstract class GreeterBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    GreeterBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return protos.Vote.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Greeter");
    }
  }

  private static final class GreeterFileDescriptorSupplier
      extends GreeterBaseDescriptorSupplier {
    GreeterFileDescriptorSupplier() {}
  }

  private static final class GreeterMethodDescriptorSupplier
      extends GreeterBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    GreeterMethodDescriptorSupplier(String methodName) {
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
      synchronized (GreeterGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new GreeterFileDescriptorSupplier())
              .addMethod(getReceiveVoteMethod())
              .addMethod(getReceiveStartElectionsMethod())
              .addMethod(getReceiveEndElectionsMethod())
              .addMethod(getReceiveStatusMethod())
              .addMethod(getReciveVotesCountMethod())
              .build();
        }
      }
    }
    return result;
  }
}
