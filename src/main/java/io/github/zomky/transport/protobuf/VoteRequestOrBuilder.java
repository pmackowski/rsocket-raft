// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: raft.proto

package io.github.zomky.transport.protobuf;

public interface VoteRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:VoteRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * candidate’s term
   * </pre>
   *
   * <code>int32 term = 1;</code>
   */
  int getTerm();

  /**
   * <pre>
   * candidate requesting vote
   * </pre>
   *
   * <code>int32 candidate_id = 2;</code>
   */
  int getCandidateId();

  /**
   * <pre>
   * index of candidate’s last log entry
   * </pre>
   *
   * <code>int64 last_log_index = 3;</code>
   */
  long getLastLogIndex();

  /**
   * <pre>
   * term of candidate’s last log entry
   * </pre>
   *
   * <code>int64 last_log_term = 4;</code>
   */
  long getLastLogTerm();
}