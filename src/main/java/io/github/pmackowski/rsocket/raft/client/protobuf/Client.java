// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: client.proto

package io.github.pmackowski.rsocket.raft.client.protobuf;

public final class Client {
  private Client() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_InfoRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_InfoRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_InfoResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_InfoResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_InitJoinRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_InitJoinRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_InitJoinResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_InitJoinResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_JoinRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_JoinRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_JoinResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_JoinResponse_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\014client.proto\"\r\n\013InfoRequest\"\037\n\014InfoRes" +
      "ponse\022\017\n\007members\030\001 \003(\005\"E\n\017InitJoinReques" +
      "t\022\026\n\016requester_port\030\001 \001(\005\022\014\n\004host\030\002 \001(\t\022" +
      "\014\n\004port\030\003 \001(\005\"\"\n\020InitJoinResponse\022\016\n\006sta" +
      "tus\030\001 \001(\010\"A\n\013JoinRequest\022\026\n\016requester_po" +
      "rt\030\001 \001(\005\022\014\n\004host\030\002 \001(\t\022\014\n\004port\030\003 \001(\005\"\036\n\014" +
      "JoinResponse\022\016\n\006status\030\001 \001(\010B5\n1io.githu" +
      "b.pmackowski.rsocket.raft.client.protobu" +
      "fP\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_InfoRequest_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_InfoRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_InfoRequest_descriptor,
        new java.lang.String[] { });
    internal_static_InfoResponse_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_InfoResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_InfoResponse_descriptor,
        new java.lang.String[] { "Members", });
    internal_static_InitJoinRequest_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_InitJoinRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_InitJoinRequest_descriptor,
        new java.lang.String[] { "RequesterPort", "Host", "Port", });
    internal_static_InitJoinResponse_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_InitJoinResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_InitJoinResponse_descriptor,
        new java.lang.String[] { "Status", });
    internal_static_JoinRequest_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_JoinRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_JoinRequest_descriptor,
        new java.lang.String[] { "RequesterPort", "Host", "Port", });
    internal_static_JoinResponse_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_JoinResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_JoinResponse_descriptor,
        new java.lang.String[] { "Status", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
