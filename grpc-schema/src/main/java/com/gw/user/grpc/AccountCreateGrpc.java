// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: UserService.proto

package com.gw.user.grpc;

public final class AccountCreateGrpc {
  private AccountCreateGrpc() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_gw_user_grpc_AccountCreateGrpcRequestDTO_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_gw_user_grpc_AccountCreateGrpcRequestDTO_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_gw_user_grpc_FetchUserDetailsByIdGrpcRequestDTO_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_gw_user_grpc_FetchUserDetailsByIdGrpcRequestDTO_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_gw_user_grpc_FetchUserDetailsByNameGrpcRequestDTO_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_gw_user_grpc_FetchUserDetailsByNameGrpcRequestDTO_fieldAccessorTable;
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
      "oogle/protobuf/empty.proto\"\256\001\n\033AccountCr" +
      "eateGrpcRequestDTO\022\020\n\010userName\030\001 \001(\t\022\020\n\010" +
      "password\030\002 \001(\t\022\021\n\tfirstName\030\003 \001(\t\022\020\n\010las" +
      "tName\030\004 \001(\t\022\014\n\004role\030\005 \001(\t\022\023\n\013dateOfBirth" +
      "\030\006 \001(\t\022\016\n\006gender\030\007 \001(\t\022\023\n\013homeCountry\030\010 " +
      "\001(\t\"0\n\"FetchUserDetailsByIdGrpcRequestDT" +
      "O\022\n\n\002id\030\001 \001(\t\"4\n$FetchUserDetailsByNameG" +
      "rpcRequestDTO\022\014\n\004name\030\001 \001(\t\"\323\001\n\032UserDeta" +
      "ilsGrpcResponseDTO\022\020\n\010userName\030\001 \001(\t\022\020\n\010" +
      "password\030\002 \001(\t\022\021\n\tfirstName\030\003 \001(\t\022\020\n\010las" +
      "tName\030\004 \001(\t\022\014\n\004role\030\005 \001(\t\022\n\n\002id\030\006 \001(\t\022\023\n" +
      "\013dateOfBirth\030\007 \001(\t\022(\n\006gender\030\010 \001(\0162\030.com" +
      ".gw.user.grpc.Gender\022\023\n\013homeCountry\030\t \001(" +
      "\t*V\n\006Gender\022\026\n\022GENDER_UNSPECIFIED\020\000\022\017\n\013G" +
      "ENDER_MALE\020\001\022\021\n\rGENDER_FEMALE\020\002\022\020\n\014GENDE" +
      "R_OTHER\020\0032\202\002\n\020FetchUserService\022t\n\016fetchU" +
      "sersById\0224.com.gw.user.grpc.FetchUserDet" +
      "ailsByIdGrpcRequestDTO\032,.com.gw.user.grp" +
      "c.UserDetailsGrpcResponseDTO\022x\n\020fetchUse" +
      "rsByName\0226.com.gw.user.grpc.FetchUserDet" +
      "ailsByNameGrpcRequestDTO\032,.com.gw.user.g" +
      "rpc.UserDetailsGrpcResponseDTO2n\n\024Create" +
      "AccountService\022V\n\rcreateAccount\022-.com.gw" +
      ".user.grpc.AccountCreateGrpcRequestDTO\032\026" +
      ".google.protobuf.EmptyB\025B\021AccountCreateG" +
      "rpcP\001b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.google.protobuf.EmptyProto.getDescriptor(),
        });
    internal_static_com_gw_user_grpc_AccountCreateGrpcRequestDTO_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_gw_user_grpc_AccountCreateGrpcRequestDTO_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_gw_user_grpc_AccountCreateGrpcRequestDTO_descriptor,
        new java.lang.String[] { "UserName", "Password", "FirstName", "LastName", "Role", "DateOfBirth", "Gender", "HomeCountry", });
    internal_static_com_gw_user_grpc_FetchUserDetailsByIdGrpcRequestDTO_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_com_gw_user_grpc_FetchUserDetailsByIdGrpcRequestDTO_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_gw_user_grpc_FetchUserDetailsByIdGrpcRequestDTO_descriptor,
        new java.lang.String[] { "Id", });
    internal_static_com_gw_user_grpc_FetchUserDetailsByNameGrpcRequestDTO_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_com_gw_user_grpc_FetchUserDetailsByNameGrpcRequestDTO_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_gw_user_grpc_FetchUserDetailsByNameGrpcRequestDTO_descriptor,
        new java.lang.String[] { "Name", });
    internal_static_com_gw_user_grpc_UserDetailsGrpcResponseDTO_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_com_gw_user_grpc_UserDetailsGrpcResponseDTO_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_gw_user_grpc_UserDetailsGrpcResponseDTO_descriptor,
        new java.lang.String[] { "UserName", "Password", "FirstName", "LastName", "Role", "Id", "DateOfBirth", "Gender", "HomeCountry", });
    com.google.protobuf.EmptyProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}