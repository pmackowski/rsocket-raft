// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Rpc.proto

package io.github.pmackowski.rsocket.raft.rpc;

/**
 * Protobuf type {@code AppendEntriesRequest}
 */
public  final class AppendEntriesRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:AppendEntriesRequest)
    AppendEntriesRequestOrBuilder {
  // Use AppendEntriesRequest.newBuilder() to construct.
  private AppendEntriesRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private AppendEntriesRequest() {
    term_ = 0;
    leaderId_ = 0;
    prevLogIndex_ = 0L;
    prevLogTerm_ = 0L;
    entries_ = java.util.Collections.emptyList();
    leaderCommit_ = 0L;
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private AppendEntriesRequest(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    int mutable_bitField0_ = 0;
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!input.skipField(tag)) {
              done = true;
            }
            break;
          }
          case 8: {

            term_ = input.readInt32();
            break;
          }
          case 16: {

            leaderId_ = input.readInt32();
            break;
          }
          case 24: {

            prevLogIndex_ = input.readInt64();
            break;
          }
          case 32: {

            prevLogTerm_ = input.readInt64();
            break;
          }
          case 42: {
            if (!((mutable_bitField0_ & 0x00000010) == 0x00000010)) {
              entries_ = new java.util.ArrayList<com.google.protobuf.ByteString>();
              mutable_bitField0_ |= 0x00000010;
            }
            entries_.add(input.readBytes());
            break;
          }
          case 48: {

            leaderCommit_ = input.readInt64();
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      if (((mutable_bitField0_ & 0x00000010) == 0x00000010)) {
        entries_ = java.util.Collections.unmodifiableList(entries_);
      }
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return io.github.pmackowski.rsocket.raft.rpc.Rpc.internal_static_AppendEntriesRequest_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return io.github.pmackowski.rsocket.raft.rpc.Rpc.internal_static_AppendEntriesRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest.class, io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest.Builder.class);
  }

  private int bitField0_;
  public static final int TERM_FIELD_NUMBER = 1;
  private int term_;
  /**
   * <pre>
   * set leader’s term
   * </pre>
   *
   * <code>optional int32 term = 1;</code>
   */
  public int getTerm() {
    return term_;
  }

  public static final int LEADERID_FIELD_NUMBER = 2;
  private int leaderId_;
  /**
   * <pre>
   * leaderId so follower can redirect clients
   * </pre>
   *
   * <code>optional int32 leaderId = 2;</code>
   */
  public int getLeaderId() {
    return leaderId_;
  }

  public static final int PREVLOGINDEX_FIELD_NUMBER = 3;
  private long prevLogIndex_;
  /**
   * <pre>
   * index of log entry immediately preceding new ones
   * </pre>
   *
   * <code>optional int64 prevLogIndex = 3;</code>
   */
  public long getPrevLogIndex() {
    return prevLogIndex_;
  }

  public static final int PREVLOGTERM_FIELD_NUMBER = 4;
  private long prevLogTerm_;
  /**
   * <pre>
   * term of prevLogIndex entry
   * </pre>
   *
   * <code>optional int64 prevLogTerm = 4;</code>
   */
  public long getPrevLogTerm() {
    return prevLogTerm_;
  }

  public static final int ENTRIES_FIELD_NUMBER = 5;
  private java.util.List<com.google.protobuf.ByteString> entries_;
  /**
   * <pre>
   * log entries to store (empty for heartbeat may send more than one for efficiency)
   * </pre>
   *
   * <code>repeated bytes entries = 5;</code>
   */
  public java.util.List<com.google.protobuf.ByteString>
      getEntriesList() {
    return entries_;
  }
  /**
   * <pre>
   * log entries to store (empty for heartbeat may send more than one for efficiency)
   * </pre>
   *
   * <code>repeated bytes entries = 5;</code>
   */
  public int getEntriesCount() {
    return entries_.size();
  }
  /**
   * <pre>
   * log entries to store (empty for heartbeat may send more than one for efficiency)
   * </pre>
   *
   * <code>repeated bytes entries = 5;</code>
   */
  public com.google.protobuf.ByteString getEntries(int index) {
    return entries_.get(index);
  }

  public static final int LEADERCOMMIT_FIELD_NUMBER = 6;
  private long leaderCommit_;
  /**
   * <pre>
   * leader’s commitIndex
   * </pre>
   *
   * <code>optional int64 leaderCommit = 6;</code>
   */
  public long getLeaderCommit() {
    return leaderCommit_;
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (term_ != 0) {
      output.writeInt32(1, term_);
    }
    if (leaderId_ != 0) {
      output.writeInt32(2, leaderId_);
    }
    if (prevLogIndex_ != 0L) {
      output.writeInt64(3, prevLogIndex_);
    }
    if (prevLogTerm_ != 0L) {
      output.writeInt64(4, prevLogTerm_);
    }
    for (int i = 0; i < entries_.size(); i++) {
      output.writeBytes(5, entries_.get(i));
    }
    if (leaderCommit_ != 0L) {
      output.writeInt64(6, leaderCommit_);
    }
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (term_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(1, term_);
    }
    if (leaderId_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(2, leaderId_);
    }
    if (prevLogIndex_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(3, prevLogIndex_);
    }
    if (prevLogTerm_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(4, prevLogTerm_);
    }
    {
      int dataSize = 0;
      for (int i = 0; i < entries_.size(); i++) {
        dataSize += com.google.protobuf.CodedOutputStream
          .computeBytesSizeNoTag(entries_.get(i));
      }
      size += dataSize;
      size += 1 * getEntriesList().size();
    }
    if (leaderCommit_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(6, leaderCommit_);
    }
    memoizedSize = size;
    return size;
  }

  private static final long serialVersionUID = 0L;
  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest)) {
      return super.equals(obj);
    }
    io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest other = (io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest) obj;

    boolean result = true;
    result = result && (getTerm()
        == other.getTerm());
    result = result && (getLeaderId()
        == other.getLeaderId());
    result = result && (getPrevLogIndex()
        == other.getPrevLogIndex());
    result = result && (getPrevLogTerm()
        == other.getPrevLogTerm());
    result = result && getEntriesList()
        .equals(other.getEntriesList());
    result = result && (getLeaderCommit()
        == other.getLeaderCommit());
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptorForType().hashCode();
    hash = (37 * hash) + TERM_FIELD_NUMBER;
    hash = (53 * hash) + getTerm();
    hash = (37 * hash) + LEADERID_FIELD_NUMBER;
    hash = (53 * hash) + getLeaderId();
    hash = (37 * hash) + PREVLOGINDEX_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getPrevLogIndex());
    hash = (37 * hash) + PREVLOGTERM_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getPrevLogTerm());
    if (getEntriesCount() > 0) {
      hash = (37 * hash) + ENTRIES_FIELD_NUMBER;
      hash = (53 * hash) + getEntriesList().hashCode();
    }
    hash = (37 * hash) + LEADERCOMMIT_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getLeaderCommit());
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
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
   * Protobuf type {@code AppendEntriesRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:AppendEntriesRequest)
      io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return io.github.pmackowski.rsocket.raft.rpc.Rpc.internal_static_AppendEntriesRequest_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return io.github.pmackowski.rsocket.raft.rpc.Rpc.internal_static_AppendEntriesRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest.class, io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest.Builder.class);
    }

    // Construct using io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    public Builder clear() {
      super.clear();
      term_ = 0;

      leaderId_ = 0;

      prevLogIndex_ = 0L;

      prevLogTerm_ = 0L;

      entries_ = java.util.Collections.emptyList();
      bitField0_ = (bitField0_ & ~0x00000010);
      leaderCommit_ = 0L;

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return io.github.pmackowski.rsocket.raft.rpc.Rpc.internal_static_AppendEntriesRequest_descriptor;
    }

    public io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest getDefaultInstanceForType() {
      return io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest.getDefaultInstance();
    }

    public io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest build() {
      io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest buildPartial() {
      io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest result = new io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      result.term_ = term_;
      result.leaderId_ = leaderId_;
      result.prevLogIndex_ = prevLogIndex_;
      result.prevLogTerm_ = prevLogTerm_;
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        entries_ = java.util.Collections.unmodifiableList(entries_);
        bitField0_ = (bitField0_ & ~0x00000010);
      }
      result.entries_ = entries_;
      result.leaderCommit_ = leaderCommit_;
      result.bitField0_ = to_bitField0_;
      onBuilt();
      return result;
    }

    public Builder clone() {
      return (Builder) super.clone();
    }
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return (Builder) super.setField(field, value);
    }
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest) {
        return mergeFrom((io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest other) {
      if (other == io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest.getDefaultInstance()) return this;
      if (other.getTerm() != 0) {
        setTerm(other.getTerm());
      }
      if (other.getLeaderId() != 0) {
        setLeaderId(other.getLeaderId());
      }
      if (other.getPrevLogIndex() != 0L) {
        setPrevLogIndex(other.getPrevLogIndex());
      }
      if (other.getPrevLogTerm() != 0L) {
        setPrevLogTerm(other.getPrevLogTerm());
      }
      if (!other.entries_.isEmpty()) {
        if (entries_.isEmpty()) {
          entries_ = other.entries_;
          bitField0_ = (bitField0_ & ~0x00000010);
        } else {
          ensureEntriesIsMutable();
          entries_.addAll(other.entries_);
        }
        onChanged();
      }
      if (other.getLeaderCommit() != 0L) {
        setLeaderCommit(other.getLeaderCommit());
      }
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private int term_ ;
    /**
     * <pre>
     * set leader’s term
     * </pre>
     *
     * <code>optional int32 term = 1;</code>
     */
    public int getTerm() {
      return term_;
    }
    /**
     * <pre>
     * set leader’s term
     * </pre>
     *
     * <code>optional int32 term = 1;</code>
     */
    public Builder setTerm(int value) {
      
      term_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * set leader’s term
     * </pre>
     *
     * <code>optional int32 term = 1;</code>
     */
    public Builder clearTerm() {
      
      term_ = 0;
      onChanged();
      return this;
    }

    private int leaderId_ ;
    /**
     * <pre>
     * leaderId so follower can redirect clients
     * </pre>
     *
     * <code>optional int32 leaderId = 2;</code>
     */
    public int getLeaderId() {
      return leaderId_;
    }
    /**
     * <pre>
     * leaderId so follower can redirect clients
     * </pre>
     *
     * <code>optional int32 leaderId = 2;</code>
     */
    public Builder setLeaderId(int value) {
      
      leaderId_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * leaderId so follower can redirect clients
     * </pre>
     *
     * <code>optional int32 leaderId = 2;</code>
     */
    public Builder clearLeaderId() {
      
      leaderId_ = 0;
      onChanged();
      return this;
    }

    private long prevLogIndex_ ;
    /**
     * <pre>
     * index of log entry immediately preceding new ones
     * </pre>
     *
     * <code>optional int64 prevLogIndex = 3;</code>
     */
    public long getPrevLogIndex() {
      return prevLogIndex_;
    }
    /**
     * <pre>
     * index of log entry immediately preceding new ones
     * </pre>
     *
     * <code>optional int64 prevLogIndex = 3;</code>
     */
    public Builder setPrevLogIndex(long value) {
      
      prevLogIndex_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * index of log entry immediately preceding new ones
     * </pre>
     *
     * <code>optional int64 prevLogIndex = 3;</code>
     */
    public Builder clearPrevLogIndex() {
      
      prevLogIndex_ = 0L;
      onChanged();
      return this;
    }

    private long prevLogTerm_ ;
    /**
     * <pre>
     * term of prevLogIndex entry
     * </pre>
     *
     * <code>optional int64 prevLogTerm = 4;</code>
     */
    public long getPrevLogTerm() {
      return prevLogTerm_;
    }
    /**
     * <pre>
     * term of prevLogIndex entry
     * </pre>
     *
     * <code>optional int64 prevLogTerm = 4;</code>
     */
    public Builder setPrevLogTerm(long value) {
      
      prevLogTerm_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * term of prevLogIndex entry
     * </pre>
     *
     * <code>optional int64 prevLogTerm = 4;</code>
     */
    public Builder clearPrevLogTerm() {
      
      prevLogTerm_ = 0L;
      onChanged();
      return this;
    }

    private java.util.List<com.google.protobuf.ByteString> entries_ = java.util.Collections.emptyList();
    private void ensureEntriesIsMutable() {
      if (!((bitField0_ & 0x00000010) == 0x00000010)) {
        entries_ = new java.util.ArrayList<com.google.protobuf.ByteString>(entries_);
        bitField0_ |= 0x00000010;
       }
    }
    /**
     * <pre>
     * log entries to store (empty for heartbeat may send more than one for efficiency)
     * </pre>
     *
     * <code>repeated bytes entries = 5;</code>
     */
    public java.util.List<com.google.protobuf.ByteString>
        getEntriesList() {
      return java.util.Collections.unmodifiableList(entries_);
    }
    /**
     * <pre>
     * log entries to store (empty for heartbeat may send more than one for efficiency)
     * </pre>
     *
     * <code>repeated bytes entries = 5;</code>
     */
    public int getEntriesCount() {
      return entries_.size();
    }
    /**
     * <pre>
     * log entries to store (empty for heartbeat may send more than one for efficiency)
     * </pre>
     *
     * <code>repeated bytes entries = 5;</code>
     */
    public com.google.protobuf.ByteString getEntries(int index) {
      return entries_.get(index);
    }
    /**
     * <pre>
     * log entries to store (empty for heartbeat may send more than one for efficiency)
     * </pre>
     *
     * <code>repeated bytes entries = 5;</code>
     */
    public Builder setEntries(
        int index, com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  ensureEntriesIsMutable();
      entries_.set(index, value);
      onChanged();
      return this;
    }
    /**
     * <pre>
     * log entries to store (empty for heartbeat may send more than one for efficiency)
     * </pre>
     *
     * <code>repeated bytes entries = 5;</code>
     */
    public Builder addEntries(com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  ensureEntriesIsMutable();
      entries_.add(value);
      onChanged();
      return this;
    }
    /**
     * <pre>
     * log entries to store (empty for heartbeat may send more than one for efficiency)
     * </pre>
     *
     * <code>repeated bytes entries = 5;</code>
     */
    public Builder addAllEntries(
        java.lang.Iterable<? extends com.google.protobuf.ByteString> values) {
      ensureEntriesIsMutable();
      com.google.protobuf.AbstractMessageLite.Builder.addAll(
          values, entries_);
      onChanged();
      return this;
    }
    /**
     * <pre>
     * log entries to store (empty for heartbeat may send more than one for efficiency)
     * </pre>
     *
     * <code>repeated bytes entries = 5;</code>
     */
    public Builder clearEntries() {
      entries_ = java.util.Collections.emptyList();
      bitField0_ = (bitField0_ & ~0x00000010);
      onChanged();
      return this;
    }

    private long leaderCommit_ ;
    /**
     * <pre>
     * leader’s commitIndex
     * </pre>
     *
     * <code>optional int64 leaderCommit = 6;</code>
     */
    public long getLeaderCommit() {
      return leaderCommit_;
    }
    /**
     * <pre>
     * leader’s commitIndex
     * </pre>
     *
     * <code>optional int64 leaderCommit = 6;</code>
     */
    public Builder setLeaderCommit(long value) {
      
      leaderCommit_ = value;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * leader’s commitIndex
     * </pre>
     *
     * <code>optional int64 leaderCommit = 6;</code>
     */
    public Builder clearLeaderCommit() {
      
      leaderCommit_ = 0L;
      onChanged();
      return this;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }


    // @@protoc_insertion_point(builder_scope:AppendEntriesRequest)
  }

  // @@protoc_insertion_point(class_scope:AppendEntriesRequest)
  private static final io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest();
  }

  public static io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<AppendEntriesRequest>
      PARSER = new com.google.protobuf.AbstractParser<AppendEntriesRequest>() {
    public AppendEntriesRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new AppendEntriesRequest(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<AppendEntriesRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<AppendEntriesRequest> getParserForType() {
    return PARSER;
  }

  public io.github.pmackowski.rsocket.raft.rpc.AppendEntriesRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

