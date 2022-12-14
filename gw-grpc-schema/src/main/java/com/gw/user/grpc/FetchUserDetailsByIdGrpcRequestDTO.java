// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: UserService.proto

package com.gw.user.grpc;

/**
 * Protobuf type {@code com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO}
 */
public final class FetchUserDetailsByIdGrpcRequestDTO extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO)
    FetchUserDetailsByIdGrpcRequestDTOOrBuilder {
private static final long serialVersionUID = 0L;
  // Use FetchUserDetailsByIdGrpcRequestDTO.newBuilder() to construct.
  private FetchUserDetailsByIdGrpcRequestDTO(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private FetchUserDetailsByIdGrpcRequestDTO() {
    id_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new FetchUserDetailsByIdGrpcRequestDTO();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.gw.user.grpc.UserManagementGrpc.internal_static_com_gw_user_grpc_FetchUserDetailsByIdGrpcRequestDTO_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.gw.user.grpc.UserManagementGrpc.internal_static_com_gw_user_grpc_FetchUserDetailsByIdGrpcRequestDTO_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO.class, com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO.Builder.class);
  }

  public static final int ID_FIELD_NUMBER = 1;
  private volatile java.lang.Object id_;
  /**
   * <code>string id = 1;</code>
   * @return The id.
   */
  @java.lang.Override
  public java.lang.String getId() {
    java.lang.Object ref = id_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      id_ = s;
      return s;
    }
  }
  /**
   * <code>string id = 1;</code>
   * @return The bytes for id.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getIdBytes() {
    java.lang.Object ref = id_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      id_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(id_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, id_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(id_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, id_);
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO)) {
      return super.equals(obj);
    }
    com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO other = (com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO) obj;

    if (!getId()
        .equals(other.getId())) return false;
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + ID_FIELD_NUMBER;
    hash = (53 * hash) + getId().hashCode();
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO)
      com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTOOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.gw.user.grpc.UserManagementGrpc.internal_static_com_gw_user_grpc_FetchUserDetailsByIdGrpcRequestDTO_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.gw.user.grpc.UserManagementGrpc.internal_static_com_gw_user_grpc_FetchUserDetailsByIdGrpcRequestDTO_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO.class, com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO.Builder.class);
    }

    // Construct using com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO.newBuilder()
    private Builder() {

    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);

    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      id_ = "";

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.gw.user.grpc.UserManagementGrpc.internal_static_com_gw_user_grpc_FetchUserDetailsByIdGrpcRequestDTO_descriptor;
    }

    @java.lang.Override
    public com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO getDefaultInstanceForType() {
      return com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO.getDefaultInstance();
    }

    @java.lang.Override
    public com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO build() {
      com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO buildPartial() {
      com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO result = new com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO(this);
      result.id_ = id_;
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO) {
        return mergeFrom((com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO other) {
      if (other == com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO.getDefaultInstance()) return this;
      if (!other.getId().isEmpty()) {
        id_ = other.id_;
        onChanged();
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 10: {
              id_ = input.readStringRequireUtf8();

              break;
            } // case 10
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }

    private java.lang.Object id_ = "";
    /**
     * <code>string id = 1;</code>
     * @return The id.
     */
    public java.lang.String getId() {
      java.lang.Object ref = id_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        id_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string id = 1;</code>
     * @return The bytes for id.
     */
    public com.google.protobuf.ByteString
        getIdBytes() {
      java.lang.Object ref = id_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        id_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string id = 1;</code>
     * @param value The id to set.
     * @return This builder for chaining.
     */
    public Builder setId(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      id_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string id = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearId() {
      
      id_ = getDefaultInstance().getId();
      onChanged();
      return this;
    }
    /**
     * <code>string id = 1;</code>
     * @param value The bytes for id to set.
     * @return This builder for chaining.
     */
    public Builder setIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      id_ = value;
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO)
  }

  // @@protoc_insertion_point(class_scope:com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO)
  private static final com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO();
  }

  public static com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<FetchUserDetailsByIdGrpcRequestDTO>
      PARSER = new com.google.protobuf.AbstractParser<FetchUserDetailsByIdGrpcRequestDTO>() {
    @java.lang.Override
    public FetchUserDetailsByIdGrpcRequestDTO parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<FetchUserDetailsByIdGrpcRequestDTO> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<FetchUserDetailsByIdGrpcRequestDTO> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.gw.user.grpc.FetchUserDetailsByIdGrpcRequestDTO getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

