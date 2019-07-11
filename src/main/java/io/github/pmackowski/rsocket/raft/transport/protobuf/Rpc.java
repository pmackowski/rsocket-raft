// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Rpc.proto

package io.github.pmackowski.rsocket.raft.transport.protobuf;

public final class Rpc {
  private Rpc() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_PreVoteRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_PreVoteRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_PreVoteResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_PreVoteResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_VoteRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_VoteRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_VoteResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_VoteResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_AppendEntriesRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_AppendEntriesRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_AppendEntriesResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_AppendEntriesResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_AddServerRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_AddServerRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_AddServerResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_AddServerResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_RemoveServerRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_RemoveServerRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_RemoveServerResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_RemoveServerResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_InstallSnapshotRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_InstallSnapshotRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_InstallSnapshotResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_InstallSnapshotResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_CommandRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_CommandRequest_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\tRpc.proto\"h\n\016PreVoteRequest\022\021\n\tnext_te" +
      "rm\030\001 \001(\005\022\024\n\014candidate_id\030\002 \001(\005\022\026\n\016last_l" +
      "og_index\030\003 \001(\003\022\025\n\rlast_log_term\030\004 \001(\003\"5\n" +
      "\017PreVoteResponse\022\014\n\004term\030\001 \001(\005\022\024\n\014vote_g" +
      "ranted\030\002 \001(\010\"`\n\013VoteRequest\022\014\n\004term\030\001 \001(" +
      "\005\022\024\n\014candidate_id\030\002 \001(\005\022\026\n\016last_log_inde" +
      "x\030\003 \001(\003\022\025\n\rlast_log_term\030\004 \001(\003\"2\n\014VoteRe" +
      "sponse\022\014\n\004term\030\001 \001(\005\022\024\n\014vote_granted\030\002 \001" +
      "(\010\"\216\001\n\024AppendEntriesRequest\022\014\n\004term\030\001 \001(" +
      "\005\022\021\n\tleader_id\030\002 \001(\005\022\026\n\016prev_log_index\030\003",
      " \001(\003\022\025\n\rprev_log_term\030\004 \001(\003\022\017\n\007entries\030\005" +
      " \003(\014\022\025\n\rleader_commit\030\006 \001(\003\"N\n\025AppendEnt" +
      "riesResponse\022\014\n\004term\030\001 \001(\005\022\017\n\007success\030\002 " +
      "\001(\010\022\026\n\016last_log_index\030\003 \001(\003\"&\n\020AddServer" +
      "Request\022\022\n\nnew_server\030\001 \001(\005\"8\n\021AddServer" +
      "Response\022\016\n\006status\030\001 \001(\010\022\023\n\013leader_hint\030" +
      "\002 \001(\005\")\n\023RemoveServerRequest\022\022\n\nold_serv" +
      "er\030\001 \001(\005\";\n\024RemoveServerResponse\022\016\n\006stat" +
      "us\030\001 \001(\010\022\023\n\013leader_hint\030\002 \001(\005\"\216\001\n\026Instal" +
      "lSnapshotRequest\022\014\n\004term\030\001 \001(\005\022\021\n\tleader",
      "_id\030\002 \001(\005\022\033\n\023last_included_index\030\003 \001(\003\022\032" +
      "\n\022last_included_term\030\004 \001(\003\022\014\n\004data\030\005 \001(\014" +
      "\022\014\n\004done\030\006 \001(\010\"\'\n\027InstallSnapshotRespons" +
      "e\022\014\n\004term\030\001 \001(\005\"C\n\016CommandRequest\022\013\n\003key" +
      "\030\001 \001(\t\022\r\n\005value\030\002 \001(\t\022\025\n\rset_operation\030\003" +
      " \001(\010B8\n4io.github.pmackowski.rsocket.raf" +
      "t.transport.protobufP\001b\006proto3"
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
    internal_static_PreVoteRequest_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_PreVoteRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_PreVoteRequest_descriptor,
        new java.lang.String[] { "NextTerm", "CandidateId", "LastLogIndex", "LastLogTerm", });
    internal_static_PreVoteResponse_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_PreVoteResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_PreVoteResponse_descriptor,
        new java.lang.String[] { "Term", "VoteGranted", });
    internal_static_VoteRequest_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_VoteRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_VoteRequest_descriptor,
        new java.lang.String[] { "Term", "CandidateId", "LastLogIndex", "LastLogTerm", });
    internal_static_VoteResponse_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_VoteResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_VoteResponse_descriptor,
        new java.lang.String[] { "Term", "VoteGranted", });
    internal_static_AppendEntriesRequest_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_AppendEntriesRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_AppendEntriesRequest_descriptor,
        new java.lang.String[] { "Term", "LeaderId", "PrevLogIndex", "PrevLogTerm", "Entries", "LeaderCommit", });
    internal_static_AppendEntriesResponse_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_AppendEntriesResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_AppendEntriesResponse_descriptor,
        new java.lang.String[] { "Term", "Success", "LastLogIndex", });
    internal_static_AddServerRequest_descriptor =
      getDescriptor().getMessageTypes().get(6);
    internal_static_AddServerRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_AddServerRequest_descriptor,
        new java.lang.String[] { "NewServer", });
    internal_static_AddServerResponse_descriptor =
      getDescriptor().getMessageTypes().get(7);
    internal_static_AddServerResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_AddServerResponse_descriptor,
        new java.lang.String[] { "Status", "LeaderHint", });
    internal_static_RemoveServerRequest_descriptor =
      getDescriptor().getMessageTypes().get(8);
    internal_static_RemoveServerRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_RemoveServerRequest_descriptor,
        new java.lang.String[] { "OldServer", });
    internal_static_RemoveServerResponse_descriptor =
      getDescriptor().getMessageTypes().get(9);
    internal_static_RemoveServerResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_RemoveServerResponse_descriptor,
        new java.lang.String[] { "Status", "LeaderHint", });
    internal_static_InstallSnapshotRequest_descriptor =
      getDescriptor().getMessageTypes().get(10);
    internal_static_InstallSnapshotRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_InstallSnapshotRequest_descriptor,
        new java.lang.String[] { "Term", "LeaderId", "LastIncludedIndex", "LastIncludedTerm", "Data", "Done", });
    internal_static_InstallSnapshotResponse_descriptor =
      getDescriptor().getMessageTypes().get(11);
    internal_static_InstallSnapshotResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_InstallSnapshotResponse_descriptor,
        new java.lang.String[] { "Term", });
    internal_static_CommandRequest_descriptor =
      getDescriptor().getMessageTypes().get(12);
    internal_static_CommandRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_CommandRequest_descriptor,
        new java.lang.String[] { "Key", "Value", "SetOperation", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}