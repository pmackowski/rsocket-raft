// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: raft.proto

package io.github.pmackowski.rsocket.raft.transport.protobuf;

public interface AddGroupRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:AddGroupRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>optional string state_machine = 1;</code>
   */
  java.lang.String getStateMachine();
  /**
   * <code>optional string state_machine = 1;</code>
   */
  com.google.protobuf.ByteString
      getStateMachineBytes();

  /**
   * <code>optional int32 election_timeout_min = 2;</code>
   */
  int getElectionTimeoutMin();

  /**
   * <code>optional int32 election_timeout_max = 3;</code>
   */
  int getElectionTimeoutMax();

  /**
   * <code>optional bool passive = 4;</code>
   */
  boolean getPassive();

  /**
   * <code>optional bool persistent_storage = 5;</code>
   */
  boolean getPersistentStorage();

  /**
   * <code>optional int32 segment_size = 6;</code>
   */
  int getSegmentSize();

  /**
   * <code>repeated int32 nodes = 7;</code>
   */
  java.util.List<java.lang.Integer> getNodesList();
  /**
   * <code>repeated int32 nodes = 7;</code>
   */
  int getNodesCount();
  /**
   * <code>repeated int32 nodes = 7;</code>
   */
  int getNodes(int index);
}
