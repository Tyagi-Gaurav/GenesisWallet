// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: userservice.proto

package com.gw.user.grpc;

/**
 * Protobuf type {@code com.gw.user.grpc.UserAuthResponseDTO}
 */
public final class UserAuthResponseDTO extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:com.gw.user.grpc.UserAuthResponseDTO)
    UserAuthResponseDTOOrBuilder {
private static final long serialVersionUID = 0L;
  // Use UserAuthResponseDTO.newBuilder() to construct.
  private UserAuthResponseDTO(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private UserAuthResponseDTO() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new UserAuthResponseDTO();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.gw.user.grpc.UserManagementGrpc.internal_static_com_gw_user_grpc_UserAuthResponseDTO_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.gw.user.grpc.UserManagementGrpc.internal_static_com_gw_user_grpc_UserAuthResponseDTO_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.gw.user.grpc.UserAuthResponseDTO.class, com.gw.user.grpc.UserAuthResponseDTO.Builder.class);
  }

  private int eitherCase_ = 0;
  private java.lang.Object either_;
  public enum EitherCase
      implements com.google.protobuf.Internal.EnumLite,
          com.google.protobuf.AbstractMessage.InternalOneOfEnum {
    AUTHDETAILS(1),
    ERROR(2),
    EITHER_NOT_SET(0);
    private final int value;
    private EitherCase(int value) {
      this.value = value;
    }
    /**
     * @param value The number of the enum to look for.
     * @return The enum associated with the given number.
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static EitherCase valueOf(int value) {
      return forNumber(value);
    }

    public static EitherCase forNumber(int value) {
      switch (value) {
        case 1: return AUTHDETAILS;
        case 2: return ERROR;
        case 0: return EITHER_NOT_SET;
        default: return null;
      }
    }
    public int getNumber() {
      return this.value;
    }
  };

  public EitherCase
  getEitherCase() {
    return EitherCase.forNumber(
        eitherCase_);
  }

  public static final int AUTHDETAILS_FIELD_NUMBER = 1;
  /**
   * <code>.com.gw.user.grpc.UserAuthDetailsDTO authDetails = 1;</code>
   * @return Whether the authDetails field is set.
   */
  @java.lang.Override
  public boolean hasAuthDetails() {
    return eitherCase_ == 1;
  }
  /**
   * <code>.com.gw.user.grpc.UserAuthDetailsDTO authDetails = 1;</code>
   * @return The authDetails.
   */
  @java.lang.Override
  public com.gw.user.grpc.UserAuthDetailsDTO getAuthDetails() {
    if (eitherCase_ == 1) {
       return (com.gw.user.grpc.UserAuthDetailsDTO) either_;
    }
    return com.gw.user.grpc.UserAuthDetailsDTO.getDefaultInstance();
  }
  /**
   * <code>.com.gw.user.grpc.UserAuthDetailsDTO authDetails = 1;</code>
   */
  @java.lang.Override
  public com.gw.user.grpc.UserAuthDetailsDTOOrBuilder getAuthDetailsOrBuilder() {
    if (eitherCase_ == 1) {
       return (com.gw.user.grpc.UserAuthDetailsDTO) either_;
    }
    return com.gw.user.grpc.UserAuthDetailsDTO.getDefaultInstance();
  }

  public static final int ERROR_FIELD_NUMBER = 2;
  /**
   * <code>.com.gw.common.grpc.Error error = 2;</code>
   * @return Whether the error field is set.
   */
  @java.lang.Override
  public boolean hasError() {
    return eitherCase_ == 2;
  }
  /**
   * <code>.com.gw.common.grpc.Error error = 2;</code>
   * @return The error.
   */
  @java.lang.Override
  public com.gw.common.grpc.Error getError() {
    if (eitherCase_ == 2) {
       return (com.gw.common.grpc.Error) either_;
    }
    return com.gw.common.grpc.Error.getDefaultInstance();
  }
  /**
   * <code>.com.gw.common.grpc.Error error = 2;</code>
   */
  @java.lang.Override
  public com.gw.common.grpc.ErrorOrBuilder getErrorOrBuilder() {
    if (eitherCase_ == 2) {
       return (com.gw.common.grpc.Error) either_;
    }
    return com.gw.common.grpc.Error.getDefaultInstance();
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
    if (eitherCase_ == 1) {
      output.writeMessage(1, (com.gw.user.grpc.UserAuthDetailsDTO) either_);
    }
    if (eitherCase_ == 2) {
      output.writeMessage(2, (com.gw.common.grpc.Error) either_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (eitherCase_ == 1) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, (com.gw.user.grpc.UserAuthDetailsDTO) either_);
    }
    if (eitherCase_ == 2) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, (com.gw.common.grpc.Error) either_);
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
    if (!(obj instanceof com.gw.user.grpc.UserAuthResponseDTO)) {
      return super.equals(obj);
    }
    com.gw.user.grpc.UserAuthResponseDTO other = (com.gw.user.grpc.UserAuthResponseDTO) obj;

    if (!getEitherCase().equals(other.getEitherCase())) return false;
    switch (eitherCase_) {
      case 1:
        if (!getAuthDetails()
            .equals(other.getAuthDetails())) return false;
        break;
      case 2:
        if (!getError()
            .equals(other.getError())) return false;
        break;
      case 0:
      default:
    }
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
    switch (eitherCase_) {
      case 1:
        hash = (37 * hash) + AUTHDETAILS_FIELD_NUMBER;
        hash = (53 * hash) + getAuthDetails().hashCode();
        break;
      case 2:
        hash = (37 * hash) + ERROR_FIELD_NUMBER;
        hash = (53 * hash) + getError().hashCode();
        break;
      case 0:
      default:
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.gw.user.grpc.UserAuthResponseDTO parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.gw.user.grpc.UserAuthResponseDTO parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.gw.user.grpc.UserAuthResponseDTO parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.gw.user.grpc.UserAuthResponseDTO parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.gw.user.grpc.UserAuthResponseDTO parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.gw.user.grpc.UserAuthResponseDTO parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.gw.user.grpc.UserAuthResponseDTO parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.gw.user.grpc.UserAuthResponseDTO parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.gw.user.grpc.UserAuthResponseDTO parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.gw.user.grpc.UserAuthResponseDTO parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.gw.user.grpc.UserAuthResponseDTO parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.gw.user.grpc.UserAuthResponseDTO parseFrom(
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
  public static Builder newBuilder(com.gw.user.grpc.UserAuthResponseDTO prototype) {
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
   * Protobuf type {@code com.gw.user.grpc.UserAuthResponseDTO}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:com.gw.user.grpc.UserAuthResponseDTO)
      com.gw.user.grpc.UserAuthResponseDTOOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.gw.user.grpc.UserManagementGrpc.internal_static_com_gw_user_grpc_UserAuthResponseDTO_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.gw.user.grpc.UserManagementGrpc.internal_static_com_gw_user_grpc_UserAuthResponseDTO_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.gw.user.grpc.UserAuthResponseDTO.class, com.gw.user.grpc.UserAuthResponseDTO.Builder.class);
    }

    // Construct using com.gw.user.grpc.UserAuthResponseDTO.newBuilder()
    private Builder() {

    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);

    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (authDetailsBuilder_ != null) {
        authDetailsBuilder_.clear();
      }
      if (errorBuilder_ != null) {
        errorBuilder_.clear();
      }
      eitherCase_ = 0;
      either_ = null;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.gw.user.grpc.UserManagementGrpc.internal_static_com_gw_user_grpc_UserAuthResponseDTO_descriptor;
    }

    @java.lang.Override
    public com.gw.user.grpc.UserAuthResponseDTO getDefaultInstanceForType() {
      return com.gw.user.grpc.UserAuthResponseDTO.getDefaultInstance();
    }

    @java.lang.Override
    public com.gw.user.grpc.UserAuthResponseDTO build() {
      com.gw.user.grpc.UserAuthResponseDTO result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.gw.user.grpc.UserAuthResponseDTO buildPartial() {
      com.gw.user.grpc.UserAuthResponseDTO result = new com.gw.user.grpc.UserAuthResponseDTO(this);
      if (eitherCase_ == 1) {
        if (authDetailsBuilder_ == null) {
          result.either_ = either_;
        } else {
          result.either_ = authDetailsBuilder_.build();
        }
      }
      if (eitherCase_ == 2) {
        if (errorBuilder_ == null) {
          result.either_ = either_;
        } else {
          result.either_ = errorBuilder_.build();
        }
      }
      result.eitherCase_ = eitherCase_;
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
      if (other instanceof com.gw.user.grpc.UserAuthResponseDTO) {
        return mergeFrom((com.gw.user.grpc.UserAuthResponseDTO)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.gw.user.grpc.UserAuthResponseDTO other) {
      if (other == com.gw.user.grpc.UserAuthResponseDTO.getDefaultInstance()) return this;
      switch (other.getEitherCase()) {
        case AUTHDETAILS: {
          mergeAuthDetails(other.getAuthDetails());
          break;
        }
        case ERROR: {
          mergeError(other.getError());
          break;
        }
        case EITHER_NOT_SET: {
          break;
        }
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
              input.readMessage(
                  getAuthDetailsFieldBuilder().getBuilder(),
                  extensionRegistry);
              eitherCase_ = 1;
              break;
            } // case 10
            case 18: {
              input.readMessage(
                  getErrorFieldBuilder().getBuilder(),
                  extensionRegistry);
              eitherCase_ = 2;
              break;
            } // case 18
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
    private int eitherCase_ = 0;
    private java.lang.Object either_;
    public EitherCase
        getEitherCase() {
      return EitherCase.forNumber(
          eitherCase_);
    }

    public Builder clearEither() {
      eitherCase_ = 0;
      either_ = null;
      onChanged();
      return this;
    }


    private com.google.protobuf.SingleFieldBuilderV3<
        com.gw.user.grpc.UserAuthDetailsDTO, com.gw.user.grpc.UserAuthDetailsDTO.Builder, com.gw.user.grpc.UserAuthDetailsDTOOrBuilder> authDetailsBuilder_;
    /**
     * <code>.com.gw.user.grpc.UserAuthDetailsDTO authDetails = 1;</code>
     * @return Whether the authDetails field is set.
     */
    @java.lang.Override
    public boolean hasAuthDetails() {
      return eitherCase_ == 1;
    }
    /**
     * <code>.com.gw.user.grpc.UserAuthDetailsDTO authDetails = 1;</code>
     * @return The authDetails.
     */
    @java.lang.Override
    public com.gw.user.grpc.UserAuthDetailsDTO getAuthDetails() {
      if (authDetailsBuilder_ == null) {
        if (eitherCase_ == 1) {
          return (com.gw.user.grpc.UserAuthDetailsDTO) either_;
        }
        return com.gw.user.grpc.UserAuthDetailsDTO.getDefaultInstance();
      } else {
        if (eitherCase_ == 1) {
          return authDetailsBuilder_.getMessage();
        }
        return com.gw.user.grpc.UserAuthDetailsDTO.getDefaultInstance();
      }
    }
    /**
     * <code>.com.gw.user.grpc.UserAuthDetailsDTO authDetails = 1;</code>
     */
    public Builder setAuthDetails(com.gw.user.grpc.UserAuthDetailsDTO value) {
      if (authDetailsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        either_ = value;
        onChanged();
      } else {
        authDetailsBuilder_.setMessage(value);
      }
      eitherCase_ = 1;
      return this;
    }
    /**
     * <code>.com.gw.user.grpc.UserAuthDetailsDTO authDetails = 1;</code>
     */
    public Builder setAuthDetails(
        com.gw.user.grpc.UserAuthDetailsDTO.Builder builderForValue) {
      if (authDetailsBuilder_ == null) {
        either_ = builderForValue.build();
        onChanged();
      } else {
        authDetailsBuilder_.setMessage(builderForValue.build());
      }
      eitherCase_ = 1;
      return this;
    }
    /**
     * <code>.com.gw.user.grpc.UserAuthDetailsDTO authDetails = 1;</code>
     */
    public Builder mergeAuthDetails(com.gw.user.grpc.UserAuthDetailsDTO value) {
      if (authDetailsBuilder_ == null) {
        if (eitherCase_ == 1 &&
            either_ != com.gw.user.grpc.UserAuthDetailsDTO.getDefaultInstance()) {
          either_ = com.gw.user.grpc.UserAuthDetailsDTO.newBuilder((com.gw.user.grpc.UserAuthDetailsDTO) either_)
              .mergeFrom(value).buildPartial();
        } else {
          either_ = value;
        }
        onChanged();
      } else {
        if (eitherCase_ == 1) {
          authDetailsBuilder_.mergeFrom(value);
        } else {
          authDetailsBuilder_.setMessage(value);
        }
      }
      eitherCase_ = 1;
      return this;
    }
    /**
     * <code>.com.gw.user.grpc.UserAuthDetailsDTO authDetails = 1;</code>
     */
    public Builder clearAuthDetails() {
      if (authDetailsBuilder_ == null) {
        if (eitherCase_ == 1) {
          eitherCase_ = 0;
          either_ = null;
          onChanged();
        }
      } else {
        if (eitherCase_ == 1) {
          eitherCase_ = 0;
          either_ = null;
        }
        authDetailsBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>.com.gw.user.grpc.UserAuthDetailsDTO authDetails = 1;</code>
     */
    public com.gw.user.grpc.UserAuthDetailsDTO.Builder getAuthDetailsBuilder() {
      return getAuthDetailsFieldBuilder().getBuilder();
    }
    /**
     * <code>.com.gw.user.grpc.UserAuthDetailsDTO authDetails = 1;</code>
     */
    @java.lang.Override
    public com.gw.user.grpc.UserAuthDetailsDTOOrBuilder getAuthDetailsOrBuilder() {
      if ((eitherCase_ == 1) && (authDetailsBuilder_ != null)) {
        return authDetailsBuilder_.getMessageOrBuilder();
      } else {
        if (eitherCase_ == 1) {
          return (com.gw.user.grpc.UserAuthDetailsDTO) either_;
        }
        return com.gw.user.grpc.UserAuthDetailsDTO.getDefaultInstance();
      }
    }
    /**
     * <code>.com.gw.user.grpc.UserAuthDetailsDTO authDetails = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.gw.user.grpc.UserAuthDetailsDTO, com.gw.user.grpc.UserAuthDetailsDTO.Builder, com.gw.user.grpc.UserAuthDetailsDTOOrBuilder> 
        getAuthDetailsFieldBuilder() {
      if (authDetailsBuilder_ == null) {
        if (!(eitherCase_ == 1)) {
          either_ = com.gw.user.grpc.UserAuthDetailsDTO.getDefaultInstance();
        }
        authDetailsBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.gw.user.grpc.UserAuthDetailsDTO, com.gw.user.grpc.UserAuthDetailsDTO.Builder, com.gw.user.grpc.UserAuthDetailsDTOOrBuilder>(
                (com.gw.user.grpc.UserAuthDetailsDTO) either_,
                getParentForChildren(),
                isClean());
        either_ = null;
      }
      eitherCase_ = 1;
      onChanged();;
      return authDetailsBuilder_;
    }

    private com.google.protobuf.SingleFieldBuilderV3<
        com.gw.common.grpc.Error, com.gw.common.grpc.Error.Builder, com.gw.common.grpc.ErrorOrBuilder> errorBuilder_;
    /**
     * <code>.com.gw.common.grpc.Error error = 2;</code>
     * @return Whether the error field is set.
     */
    @java.lang.Override
    public boolean hasError() {
      return eitherCase_ == 2;
    }
    /**
     * <code>.com.gw.common.grpc.Error error = 2;</code>
     * @return The error.
     */
    @java.lang.Override
    public com.gw.common.grpc.Error getError() {
      if (errorBuilder_ == null) {
        if (eitherCase_ == 2) {
          return (com.gw.common.grpc.Error) either_;
        }
        return com.gw.common.grpc.Error.getDefaultInstance();
      } else {
        if (eitherCase_ == 2) {
          return errorBuilder_.getMessage();
        }
        return com.gw.common.grpc.Error.getDefaultInstance();
      }
    }
    /**
     * <code>.com.gw.common.grpc.Error error = 2;</code>
     */
    public Builder setError(com.gw.common.grpc.Error value) {
      if (errorBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        either_ = value;
        onChanged();
      } else {
        errorBuilder_.setMessage(value);
      }
      eitherCase_ = 2;
      return this;
    }
    /**
     * <code>.com.gw.common.grpc.Error error = 2;</code>
     */
    public Builder setError(
        com.gw.common.grpc.Error.Builder builderForValue) {
      if (errorBuilder_ == null) {
        either_ = builderForValue.build();
        onChanged();
      } else {
        errorBuilder_.setMessage(builderForValue.build());
      }
      eitherCase_ = 2;
      return this;
    }
    /**
     * <code>.com.gw.common.grpc.Error error = 2;</code>
     */
    public Builder mergeError(com.gw.common.grpc.Error value) {
      if (errorBuilder_ == null) {
        if (eitherCase_ == 2 &&
            either_ != com.gw.common.grpc.Error.getDefaultInstance()) {
          either_ = com.gw.common.grpc.Error.newBuilder((com.gw.common.grpc.Error) either_)
              .mergeFrom(value).buildPartial();
        } else {
          either_ = value;
        }
        onChanged();
      } else {
        if (eitherCase_ == 2) {
          errorBuilder_.mergeFrom(value);
        } else {
          errorBuilder_.setMessage(value);
        }
      }
      eitherCase_ = 2;
      return this;
    }
    /**
     * <code>.com.gw.common.grpc.Error error = 2;</code>
     */
    public Builder clearError() {
      if (errorBuilder_ == null) {
        if (eitherCase_ == 2) {
          eitherCase_ = 0;
          either_ = null;
          onChanged();
        }
      } else {
        if (eitherCase_ == 2) {
          eitherCase_ = 0;
          either_ = null;
        }
        errorBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>.com.gw.common.grpc.Error error = 2;</code>
     */
    public com.gw.common.grpc.Error.Builder getErrorBuilder() {
      return getErrorFieldBuilder().getBuilder();
    }
    /**
     * <code>.com.gw.common.grpc.Error error = 2;</code>
     */
    @java.lang.Override
    public com.gw.common.grpc.ErrorOrBuilder getErrorOrBuilder() {
      if ((eitherCase_ == 2) && (errorBuilder_ != null)) {
        return errorBuilder_.getMessageOrBuilder();
      } else {
        if (eitherCase_ == 2) {
          return (com.gw.common.grpc.Error) either_;
        }
        return com.gw.common.grpc.Error.getDefaultInstance();
      }
    }
    /**
     * <code>.com.gw.common.grpc.Error error = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.gw.common.grpc.Error, com.gw.common.grpc.Error.Builder, com.gw.common.grpc.ErrorOrBuilder> 
        getErrorFieldBuilder() {
      if (errorBuilder_ == null) {
        if (!(eitherCase_ == 2)) {
          either_ = com.gw.common.grpc.Error.getDefaultInstance();
        }
        errorBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.gw.common.grpc.Error, com.gw.common.grpc.Error.Builder, com.gw.common.grpc.ErrorOrBuilder>(
                (com.gw.common.grpc.Error) either_,
                getParentForChildren(),
                isClean());
        either_ = null;
      }
      eitherCase_ = 2;
      onChanged();;
      return errorBuilder_;
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


    // @@protoc_insertion_point(builder_scope:com.gw.user.grpc.UserAuthResponseDTO)
  }

  // @@protoc_insertion_point(class_scope:com.gw.user.grpc.UserAuthResponseDTO)
  private static final com.gw.user.grpc.UserAuthResponseDTO DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.gw.user.grpc.UserAuthResponseDTO();
  }

  public static com.gw.user.grpc.UserAuthResponseDTO getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<UserAuthResponseDTO>
      PARSER = new com.google.protobuf.AbstractParser<UserAuthResponseDTO>() {
    @java.lang.Override
    public UserAuthResponseDTO parsePartialFrom(
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

  public static com.google.protobuf.Parser<UserAuthResponseDTO> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<UserAuthResponseDTO> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.gw.user.grpc.UserAuthResponseDTO getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
