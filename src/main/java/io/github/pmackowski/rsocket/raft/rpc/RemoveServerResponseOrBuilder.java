// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Rpc.proto

package io.github.pmackowski.rsocket.raft.rpc;

public interface RemoveServerResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:RemoveServerResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * OK if server was removed successfully
   * </pre>
   *
   * <code>optional bool status = 1;</code>
   */
  boolean getStatus();

  /**
   * <pre>
   * address of recent leader, if known
   * </pre>
   *
   * <code>optional int32 leaderHint = 2;</code>
   */
  int getLeaderHint();
}
