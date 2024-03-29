// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: userservice.proto

package com.gw.user.grpc;

/**
 * Protobuf enum {@code com.gw.user.grpc.ExternalSystem}
 */
public enum ExternalSystem
    implements com.google.protobuf.ProtocolMessageEnum {
  /**
   * <code>UNKNOWN = 0;</code>
   */
  UNKNOWN(0),
  /**
   * <code>GOOGLE = 1;</code>
   */
  GOOGLE(1),
  /**
   * <code>FACEBOOK = 2;</code>
   */
  FACEBOOK(2),
  UNRECOGNIZED(-1),
  ;

  /**
   * <code>UNKNOWN = 0;</code>
   */
  public static final int UNKNOWN_VALUE = 0;
  /**
   * <code>GOOGLE = 1;</code>
   */
  public static final int GOOGLE_VALUE = 1;
  /**
   * <code>FACEBOOK = 2;</code>
   */
  public static final int FACEBOOK_VALUE = 2;


  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @java.lang.Deprecated
  public static ExternalSystem valueOf(int value) {
    return forNumber(value);
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   */
  public static ExternalSystem forNumber(int value) {
    switch (value) {
      case 0: return UNKNOWN;
      case 1: return GOOGLE;
      case 2: return FACEBOOK;
      default: return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<ExternalSystem>
      internalGetValueMap() {
    return internalValueMap;
  }
  private static final com.google.protobuf.Internal.EnumLiteMap<
      ExternalSystem> internalValueMap =
        new com.google.protobuf.Internal.EnumLiteMap<ExternalSystem>() {
          public ExternalSystem findValueByNumber(int number) {
            return ExternalSystem.forNumber(number);
          }
        };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor
      getValueDescriptor() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalStateException(
          "Can't get the descriptor of an unrecognized enum value.");
    }
    return getDescriptor().getValues().get(ordinal());
  }
  public final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptorForType() {
    return getDescriptor();
  }
  public static final com.google.protobuf.Descriptors.EnumDescriptor
      getDescriptor() {
    return com.gw.user.grpc.UserManagementGrpc.getDescriptor().getEnumTypes().get(0);
  }

  private static final ExternalSystem[] VALUES = values();

  public static ExternalSystem valueOf(
      com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException(
        "EnumValueDescriptor is not for this type.");
    }
    if (desc.getIndex() == -1) {
      return UNRECOGNIZED;
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private ExternalSystem(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:com.gw.user.grpc.ExternalSystem)
}

