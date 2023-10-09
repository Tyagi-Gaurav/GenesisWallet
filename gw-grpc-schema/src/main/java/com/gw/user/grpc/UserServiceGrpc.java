package com.gw.user.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.50.2)",
    comments = "Source: userservice.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class UserServiceGrpc {

  private UserServiceGrpc() {}

  public static final String SERVICE_NAME = "com.gw.user.grpc.UserService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.gw.user.grpc.FetchUserDetailsByUserNameGrpcRequestDTO,
      com.gw.user.grpc.UserDetailsGrpcResponseDTO> getFetchUsersByUserNameMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "fetchUsersByUserName",
      requestType = com.gw.user.grpc.FetchUserDetailsByUserNameGrpcRequestDTO.class,
      responseType = com.gw.user.grpc.UserDetailsGrpcResponseDTO.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.gw.user.grpc.FetchUserDetailsByUserNameGrpcRequestDTO,
      com.gw.user.grpc.UserDetailsGrpcResponseDTO> getFetchUsersByUserNameMethod() {
    io.grpc.MethodDescriptor<com.gw.user.grpc.FetchUserDetailsByUserNameGrpcRequestDTO, com.gw.user.grpc.UserDetailsGrpcResponseDTO> getFetchUsersByUserNameMethod;
    if ((getFetchUsersByUserNameMethod = UserServiceGrpc.getFetchUsersByUserNameMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getFetchUsersByUserNameMethod = UserServiceGrpc.getFetchUsersByUserNameMethod) == null) {
          UserServiceGrpc.getFetchUsersByUserNameMethod = getFetchUsersByUserNameMethod =
              io.grpc.MethodDescriptor.<com.gw.user.grpc.FetchUserDetailsByUserNameGrpcRequestDTO, com.gw.user.grpc.UserDetailsGrpcResponseDTO>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "fetchUsersByUserName"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.gw.user.grpc.FetchUserDetailsByUserNameGrpcRequestDTO.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.gw.user.grpc.UserDetailsGrpcResponseDTO.getDefaultInstance()))
              .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("fetchUsersByUserName"))
              .build();
        }
      }
    }
    return getFetchUsersByUserNameMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.gw.user.grpc.UserAuthRequestDTO,
      com.gw.user.grpc.UserAuthResponseDTO> getAuthenticateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "authenticate",
      requestType = com.gw.user.grpc.UserAuthRequestDTO.class,
      responseType = com.gw.user.grpc.UserAuthResponseDTO.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.gw.user.grpc.UserAuthRequestDTO,
      com.gw.user.grpc.UserAuthResponseDTO> getAuthenticateMethod() {
    io.grpc.MethodDescriptor<com.gw.user.grpc.UserAuthRequestDTO, com.gw.user.grpc.UserAuthResponseDTO> getAuthenticateMethod;
    if ((getAuthenticateMethod = UserServiceGrpc.getAuthenticateMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getAuthenticateMethod = UserServiceGrpc.getAuthenticateMethod) == null) {
          UserServiceGrpc.getAuthenticateMethod = getAuthenticateMethod =
              io.grpc.MethodDescriptor.<com.gw.user.grpc.UserAuthRequestDTO, com.gw.user.grpc.UserAuthResponseDTO>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "authenticate"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.gw.user.grpc.UserAuthRequestDTO.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.gw.user.grpc.UserAuthResponseDTO.getDefaultInstance()))
              .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("authenticate"))
              .build();
        }
      }
    }
    return getAuthenticateMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.gw.user.grpc.UserCreateGrpcRequestDTO,
      com.gw.user.grpc.UserCreateGrpcResponseDTO> getCreateUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "createUser",
      requestType = com.gw.user.grpc.UserCreateGrpcRequestDTO.class,
      responseType = com.gw.user.grpc.UserCreateGrpcResponseDTO.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.gw.user.grpc.UserCreateGrpcRequestDTO,
      com.gw.user.grpc.UserCreateGrpcResponseDTO> getCreateUserMethod() {
    io.grpc.MethodDescriptor<com.gw.user.grpc.UserCreateGrpcRequestDTO, com.gw.user.grpc.UserCreateGrpcResponseDTO> getCreateUserMethod;
    if ((getCreateUserMethod = UserServiceGrpc.getCreateUserMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getCreateUserMethod = UserServiceGrpc.getCreateUserMethod) == null) {
          UserServiceGrpc.getCreateUserMethod = getCreateUserMethod =
              io.grpc.MethodDescriptor.<com.gw.user.grpc.UserCreateGrpcRequestDTO, com.gw.user.grpc.UserCreateGrpcResponseDTO>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "createUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.gw.user.grpc.UserCreateGrpcRequestDTO.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.gw.user.grpc.UserCreateGrpcResponseDTO.getDefaultInstance()))
              .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("createUser"))
              .build();
        }
      }
    }
    return getCreateUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.gw.user.grpc.ExternalUserCreateGrpcRequestDTO,
      com.gw.user.grpc.ExternalUserCreateGrpcResponseDTO> getCreateExternalUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "createExternalUser",
      requestType = com.gw.user.grpc.ExternalUserCreateGrpcRequestDTO.class,
      responseType = com.gw.user.grpc.ExternalUserCreateGrpcResponseDTO.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.gw.user.grpc.ExternalUserCreateGrpcRequestDTO,
      com.gw.user.grpc.ExternalUserCreateGrpcResponseDTO> getCreateExternalUserMethod() {
    io.grpc.MethodDescriptor<com.gw.user.grpc.ExternalUserCreateGrpcRequestDTO, com.gw.user.grpc.ExternalUserCreateGrpcResponseDTO> getCreateExternalUserMethod;
    if ((getCreateExternalUserMethod = UserServiceGrpc.getCreateExternalUserMethod) == null) {
      synchronized (UserServiceGrpc.class) {
        if ((getCreateExternalUserMethod = UserServiceGrpc.getCreateExternalUserMethod) == null) {
          UserServiceGrpc.getCreateExternalUserMethod = getCreateExternalUserMethod =
              io.grpc.MethodDescriptor.<com.gw.user.grpc.ExternalUserCreateGrpcRequestDTO, com.gw.user.grpc.ExternalUserCreateGrpcResponseDTO>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "createExternalUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.gw.user.grpc.ExternalUserCreateGrpcRequestDTO.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.gw.user.grpc.ExternalUserCreateGrpcResponseDTO.getDefaultInstance()))
              .setSchemaDescriptor(new UserServiceMethodDescriptorSupplier("createExternalUser"))
              .build();
        }
      }
    }
    return getCreateExternalUserMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static UserServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserServiceStub>() {
        @java.lang.Override
        public UserServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserServiceStub(channel, callOptions);
        }
      };
    return UserServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static UserServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserServiceBlockingStub>() {
        @java.lang.Override
        public UserServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserServiceBlockingStub(channel, callOptions);
        }
      };
    return UserServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static UserServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UserServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UserServiceFutureStub>() {
        @java.lang.Override
        public UserServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UserServiceFutureStub(channel, callOptions);
        }
      };
    return UserServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class UserServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void fetchUsersByUserName(com.gw.user.grpc.FetchUserDetailsByUserNameGrpcRequestDTO request,
        io.grpc.stub.StreamObserver<com.gw.user.grpc.UserDetailsGrpcResponseDTO> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getFetchUsersByUserNameMethod(), responseObserver);
    }

    /**
     */
    public void authenticate(com.gw.user.grpc.UserAuthRequestDTO request,
        io.grpc.stub.StreamObserver<com.gw.user.grpc.UserAuthResponseDTO> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAuthenticateMethod(), responseObserver);
    }

    /**
     */
    public void createUser(com.gw.user.grpc.UserCreateGrpcRequestDTO request,
        io.grpc.stub.StreamObserver<com.gw.user.grpc.UserCreateGrpcResponseDTO> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateUserMethod(), responseObserver);
    }

    /**
     */
    public void createExternalUser(com.gw.user.grpc.ExternalUserCreateGrpcRequestDTO request,
        io.grpc.stub.StreamObserver<com.gw.user.grpc.ExternalUserCreateGrpcResponseDTO> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateExternalUserMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getFetchUsersByUserNameMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.gw.user.grpc.FetchUserDetailsByUserNameGrpcRequestDTO,
                com.gw.user.grpc.UserDetailsGrpcResponseDTO>(
                  this, METHODID_FETCH_USERS_BY_USER_NAME)))
          .addMethod(
            getAuthenticateMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.gw.user.grpc.UserAuthRequestDTO,
                com.gw.user.grpc.UserAuthResponseDTO>(
                  this, METHODID_AUTHENTICATE)))
          .addMethod(
            getCreateUserMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.gw.user.grpc.UserCreateGrpcRequestDTO,
                com.gw.user.grpc.UserCreateGrpcResponseDTO>(
                  this, METHODID_CREATE_USER)))
          .addMethod(
            getCreateExternalUserMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.gw.user.grpc.ExternalUserCreateGrpcRequestDTO,
                com.gw.user.grpc.ExternalUserCreateGrpcResponseDTO>(
                  this, METHODID_CREATE_EXTERNAL_USER)))
          .build();
    }
  }

  /**
   */
  public static final class UserServiceStub extends io.grpc.stub.AbstractAsyncStub<UserServiceStub> {
    private UserServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserServiceStub(channel, callOptions);
    }

    /**
     */
    public void fetchUsersByUserName(com.gw.user.grpc.FetchUserDetailsByUserNameGrpcRequestDTO request,
        io.grpc.stub.StreamObserver<com.gw.user.grpc.UserDetailsGrpcResponseDTO> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getFetchUsersByUserNameMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void authenticate(com.gw.user.grpc.UserAuthRequestDTO request,
        io.grpc.stub.StreamObserver<com.gw.user.grpc.UserAuthResponseDTO> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAuthenticateMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void createUser(com.gw.user.grpc.UserCreateGrpcRequestDTO request,
        io.grpc.stub.StreamObserver<com.gw.user.grpc.UserCreateGrpcResponseDTO> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void createExternalUser(com.gw.user.grpc.ExternalUserCreateGrpcRequestDTO request,
        io.grpc.stub.StreamObserver<com.gw.user.grpc.ExternalUserCreateGrpcResponseDTO> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateExternalUserMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class UserServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<UserServiceBlockingStub> {
    private UserServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.gw.user.grpc.UserDetailsGrpcResponseDTO fetchUsersByUserName(com.gw.user.grpc.FetchUserDetailsByUserNameGrpcRequestDTO request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFetchUsersByUserNameMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.gw.user.grpc.UserAuthResponseDTO authenticate(com.gw.user.grpc.UserAuthRequestDTO request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAuthenticateMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.gw.user.grpc.UserCreateGrpcResponseDTO createUser(com.gw.user.grpc.UserCreateGrpcRequestDTO request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.gw.user.grpc.ExternalUserCreateGrpcResponseDTO createExternalUser(com.gw.user.grpc.ExternalUserCreateGrpcRequestDTO request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateExternalUserMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class UserServiceFutureStub extends io.grpc.stub.AbstractFutureStub<UserServiceFutureStub> {
    private UserServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UserServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UserServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.gw.user.grpc.UserDetailsGrpcResponseDTO> fetchUsersByUserName(
        com.gw.user.grpc.FetchUserDetailsByUserNameGrpcRequestDTO request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getFetchUsersByUserNameMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.gw.user.grpc.UserAuthResponseDTO> authenticate(
        com.gw.user.grpc.UserAuthRequestDTO request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAuthenticateMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.gw.user.grpc.UserCreateGrpcResponseDTO> createUser(
        com.gw.user.grpc.UserCreateGrpcRequestDTO request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateUserMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.gw.user.grpc.ExternalUserCreateGrpcResponseDTO> createExternalUser(
        com.gw.user.grpc.ExternalUserCreateGrpcRequestDTO request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateExternalUserMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_FETCH_USERS_BY_USER_NAME = 0;
  private static final int METHODID_AUTHENTICATE = 1;
  private static final int METHODID_CREATE_USER = 2;
  private static final int METHODID_CREATE_EXTERNAL_USER = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final UserServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(UserServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_FETCH_USERS_BY_USER_NAME:
          serviceImpl.fetchUsersByUserName((com.gw.user.grpc.FetchUserDetailsByUserNameGrpcRequestDTO) request,
              (io.grpc.stub.StreamObserver<com.gw.user.grpc.UserDetailsGrpcResponseDTO>) responseObserver);
          break;
        case METHODID_AUTHENTICATE:
          serviceImpl.authenticate((com.gw.user.grpc.UserAuthRequestDTO) request,
              (io.grpc.stub.StreamObserver<com.gw.user.grpc.UserAuthResponseDTO>) responseObserver);
          break;
        case METHODID_CREATE_USER:
          serviceImpl.createUser((com.gw.user.grpc.UserCreateGrpcRequestDTO) request,
              (io.grpc.stub.StreamObserver<com.gw.user.grpc.UserCreateGrpcResponseDTO>) responseObserver);
          break;
        case METHODID_CREATE_EXTERNAL_USER:
          serviceImpl.createExternalUser((com.gw.user.grpc.ExternalUserCreateGrpcRequestDTO) request,
              (io.grpc.stub.StreamObserver<com.gw.user.grpc.ExternalUserCreateGrpcResponseDTO>) responseObserver);
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

  private static abstract class UserServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    UserServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.gw.user.grpc.UserManagementGrpc.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("UserService");
    }
  }

  private static final class UserServiceFileDescriptorSupplier
      extends UserServiceBaseDescriptorSupplier {
    UserServiceFileDescriptorSupplier() {}
  }

  private static final class UserServiceMethodDescriptorSupplier
      extends UserServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    UserServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (UserServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new UserServiceFileDescriptorSupplier())
              .addMethod(getFetchUsersByUserNameMethod())
              .addMethod(getAuthenticateMethod())
              .addMethod(getCreateUserMethod())
              .addMethod(getCreateExternalUserMethod())
              .build();
        }
      }
    }
    return result;
  }
}
