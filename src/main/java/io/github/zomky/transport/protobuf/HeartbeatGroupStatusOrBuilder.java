// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: raft.proto

package io.github.zomky.transport.protobuf;

public interface HeartbeatGroupStatusOrBuilder extends
    // @@protoc_insertion_point(interface_extends:HeartbeatGroupStatus)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * set leader’s term
   * </pre>
   *
   * <code>int32 term = 1;</code>
   */
  int getTerm();

  /**
   * <code>string group_name = 2;</code>
   */
  java.lang.String getGroupName();
  /**
   * <code>string group_name = 2;</code>
   */
  com.google.protobuf.ByteString
      getGroupNameBytes();

  /**
   * <code>bool success = 3;</code>
   */
  boolean getSuccess();
}
