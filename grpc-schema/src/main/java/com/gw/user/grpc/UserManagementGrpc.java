// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: UserService.proto

package com.gw.user.grpc;

public final class UserManagementGrpc {
  private UserManagementGrpc() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_gw_user_grpc_UserCreateGrpcRequestDTO_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_gw_user_grpc_UserCreateGrpcRequestDTO_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_gw_user_grpc_FetchUserDetailsByIdGrpcRequestDTO_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_gw_user_grpc_FetchUserDetailsByIdGrpcRequestDTO_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_gw_user_grpc_UserDetailsGrpcResponseDTO_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_gw_user_grpc_UserDetailsGrpcResponseDTO_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\021UserService.proto\022\020com.gw.user.grpc\032\033g" +
      "oogle/protobuf/empty.proto\"\267\001\n\030UserCreat" +
      "eGrpcRequestDTO\022\020\n\010userName\030\001 \001(\t\022\020\n\010pas" +
      "sword\030\002 \001(\t\022\021\n\tfirstName\030\003 \001(\t\022\020\n\010lastNa" +
      "me\030\004 \001(\t\022\023\n\013dateOfBirth\030\005 \001(\t\022(\n\006gender\030" +
      "\006 \001(\0162\030.com.gw.user.grpc.Gender\022\023\n\013homeC" +
      "ountry\030\007 \001(\t\"0\n\"FetchUserDetailsByIdGrpc" +
      "RequestDTO\022\n\n\002id\030\001 \001(\t\"\323\001\n\032UserDetailsGr" +
      "pcResponseDTO\022\020\n\010userName\030\001 \001(\t\022\020\n\010passw" +
      "ord\030\002 \001(\t\022\021\n\tfirstName\030\003 \001(\t\022\020\n\010lastName" +
      "\030\004 \001(\t\022\014\n\004role\030\005 \001(\t\022\n\n\002id\030\006 \001(\t\022\023\n\013date" +
      "OfBirth\030\007 \001(\t\022(\n\006gender\030\010 \001(\0162\030.com.gw.u" +
      "ser.grpc.Gender\022\023\n\013homeCountry\030\t \001(\t*V\n\006" +
      "Gender\022\026\n\022GENDER_UNSPECIFIED\020\000\022\017\n\013GENDER" +
      "_MALE\020\001\022\021\n\rGENDER_FEMALE\020\002\022\020\n\014GENDER_OTH" +
      "ER\020\0032\325\001\n\013UserService\022t\n\016fetchUsersById\0224" +
      ".com.gw.user.grpc.FetchUserDetailsByIdGr" +
      "pcRequestDTO\032,.com.gw.user.grpc.UserDeta" +
      "ilsGrpcResponseDTO\022P\n\ncreateUser\022*.com.g" +
      "w.user.grpc.UserCreateGrpcRequestDTO\032\026.g" +
      "oogle.protobuf.EmptyB\026B\022UserManagementGr" +
      "pcP\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.google.protobuf.EmptyProto.getDescriptor(),
        });
    internal_static_com_gw_user_grpc_UserCreateGrpcRequestDTO_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_gw_user_grpc_UserCreateGrpcRequestDTO_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_gw_user_grpc_UserCreateGrpcRequestDTO_descriptor,
        new java.lang.String[] { "UserName", "Password", "FirstName", "LastName", "DateOfBirth", "Gender", "HomeCountry", });
    internal_static_com_gw_user_grpc_FetchUserDetailsByIdGrpcRequestDTO_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_com_gw_user_grpc_FetchUserDetailsByIdGrpcRequestDTO_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_gw_user_grpc_FetchUserDetailsByIdGrpcRequestDTO_descriptor,
        new java.lang.String[] { "Id", });
    internal_static_com_gw_user_grpc_UserDetailsGrpcResponseDTO_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_com_gw_user_grpc_UserDetailsGrpcResponseDTO_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_gw_user_grpc_UserDetailsGrpcResponseDTO_descriptor,
        new java.lang.String[] { "UserName", "Password", "FirstName", "LastName", "Role", "Id", "DateOfBirth", "Gender", "HomeCountry", });
    com.google.protobuf.EmptyProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
