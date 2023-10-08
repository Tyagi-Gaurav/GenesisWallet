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
    internal_static_com_gw_user_grpc_UserCreateGrpcResponseDTO_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_gw_user_grpc_UserCreateGrpcResponseDTO_fieldAccessorTable;
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
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_gw_user_grpc_ExternalUserCreateGrpcRequestDTO_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_gw_user_grpc_ExternalUserCreateGrpcRequestDTO_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_gw_user_grpc_ExternalUserCreateGrpcResponseDTO_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_gw_user_grpc_ExternalUserCreateGrpcResponseDTO_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\021UserService.proto\022\020com.gw.user.grpc\032\033g" +
      "oogle/protobuf/empty.proto\"x\n\030UserCreate" +
      "GrpcRequestDTO\022\020\n\010userName\030\001 \001(\t\022\020\n\010pass" +
      "word\030\002 \001(\t\022\021\n\tfirstName\030\003 \001(\t\022\020\n\010lastNam" +
      "e\030\004 \001(\t\022\023\n\013dateOfBirth\030\005 \001(\t\",\n\031UserCrea" +
      "teGrpcResponseDTO\022\017\n\007created\030\001 \001(\010\"0\n\"Fe" +
      "tchUserDetailsByIdGrpcRequestDTO\022\n\n\002id\030\001" +
      " \001(\t\"\224\001\n\032UserDetailsGrpcResponseDTO\022\020\n\010u" +
      "serName\030\001 \001(\t\022\020\n\010password\030\002 \001(\t\022\021\n\tfirst" +
      "Name\030\003 \001(\t\022\020\n\010lastName\030\004 \001(\t\022\014\n\004role\030\005 \001" +
      "(\t\022\n\n\002id\030\006 \001(\t\022\023\n\013dateOfBirth\030\007 \001(\t\"\303\001\n " +
      "ExternalUserCreateGrpcRequestDTO\022\r\n\005emai" +
      "l\030\001 \001(\t\022\021\n\tfirstName\030\002 \001(\t\022\020\n\010lastName\030\003" +
      " \001(\t\022\022\n\ntokenValue\030\004 \001(\t\022\021\n\ttokenType\030\005 " +
      "\001(\t\022\027\n\017tokenExpiryTime\030\006 \001(\003\022\026\n\016external" +
      "System\030\007 \001(\t\022\023\n\013dateOfBirth\030\010 \001(\t\"#\n!Ext" +
      "ernalUserCreateGrpcResponseDTO2\351\002\n\013UserS" +
      "ervice\022t\n\016fetchUsersById\0224.com.gw.user.g" +
      "rpc.FetchUserDetailsByIdGrpcRequestDTO\032," +
      ".com.gw.user.grpc.UserDetailsGrpcRespons" +
      "eDTO\022e\n\ncreateUser\022*.com.gw.user.grpc.Us" +
      "erCreateGrpcRequestDTO\032+.com.gw.user.grp" +
      "c.UserCreateGrpcResponseDTO\022}\n\022createExt" +
      "ernalUser\0222.com.gw.user.grpc.ExternalUse" +
      "rCreateGrpcRequestDTO\0323.com.gw.user.grpc" +
      ".ExternalUserCreateGrpcResponseDTOB\026B\022Us" +
      "erManagementGrpcP\001b\006proto3"
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
        new java.lang.String[] { "UserName", "Password", "FirstName", "LastName", "DateOfBirth", });
    internal_static_com_gw_user_grpc_UserCreateGrpcResponseDTO_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_com_gw_user_grpc_UserCreateGrpcResponseDTO_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_gw_user_grpc_UserCreateGrpcResponseDTO_descriptor,
        new java.lang.String[] { "Created", });
    internal_static_com_gw_user_grpc_FetchUserDetailsByIdGrpcRequestDTO_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_com_gw_user_grpc_FetchUserDetailsByIdGrpcRequestDTO_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_gw_user_grpc_FetchUserDetailsByIdGrpcRequestDTO_descriptor,
        new java.lang.String[] { "Id", });
    internal_static_com_gw_user_grpc_UserDetailsGrpcResponseDTO_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_com_gw_user_grpc_UserDetailsGrpcResponseDTO_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_gw_user_grpc_UserDetailsGrpcResponseDTO_descriptor,
        new java.lang.String[] { "UserName", "Password", "FirstName", "LastName", "Role", "Id", "DateOfBirth", });
    internal_static_com_gw_user_grpc_ExternalUserCreateGrpcRequestDTO_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_com_gw_user_grpc_ExternalUserCreateGrpcRequestDTO_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_gw_user_grpc_ExternalUserCreateGrpcRequestDTO_descriptor,
        new java.lang.String[] { "Email", "FirstName", "LastName", "TokenValue", "TokenType", "TokenExpiryTime", "ExternalSystem", "DateOfBirth", });
    internal_static_com_gw_user_grpc_ExternalUserCreateGrpcResponseDTO_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_com_gw_user_grpc_ExternalUserCreateGrpcResponseDTO_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_gw_user_grpc_ExternalUserCreateGrpcResponseDTO_descriptor,
        new java.lang.String[] { });
    com.google.protobuf.EmptyProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
