// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: error.proto

package com.gw.common.grpc;

public interface ErrorOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.gw.common.grpc.Error)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.com.gw.common.grpc.Error.ErrorCode code = 1;</code>
   * @return The enum numeric value on the wire for code.
   */
  int getCodeValue();
  /**
   * <code>.com.gw.common.grpc.Error.ErrorCode code = 1;</code>
   * @return The code.
   */
  com.gw.common.grpc.Error.ErrorCode getCode();

  /**
   * <code>string description = 2;</code>
   * @return The description.
   */
  java.lang.String getDescription();
  /**
   * <code>string description = 2;</code>
   * @return The bytes for description.
   */
  com.google.protobuf.ByteString
      getDescriptionBytes();
}
