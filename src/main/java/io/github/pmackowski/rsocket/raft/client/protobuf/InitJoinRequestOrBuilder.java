// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: client.proto

package io.github.pmackowski.rsocket.raft.client.protobuf;

public interface InitJoinRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:InitJoinRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>optional int32 requester_port = 1;</code>
   */
  int getRequesterPort();

  /**
   * <code>optional string host = 2;</code>
   */
  java.lang.String getHost();
  /**
   * <code>optional string host = 2;</code>
   */
  com.google.protobuf.ByteString
      getHostBytes();

  /**
   * <code>optional int32 port = 3;</code>
   */
  int getPort();
}