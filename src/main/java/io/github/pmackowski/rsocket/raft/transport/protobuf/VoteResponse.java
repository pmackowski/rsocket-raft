// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Rpc.proto

package io.github.pmackowski.rsocket.raft.transport.protobuf;

/**
 * Protobuf type {@code VoteResponse}
 */
public  final class VoteResponse extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:VoteResponse)
    VoteResponseOrBuilder {
  // Use VoteResponse.newBuilder() to construct.
  private VoteResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private VoteResponse() {
    term_ = 0;
    voteGranted_ = false;
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private VoteResponse(
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

            voteGranted_ = input.readBool();
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
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return io.github.pmackowski.rsocket.raft.transport.protobuf.Rpc.internal_static_VoteResponse_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return io.github.pmackowski.rsocket.raft.transport.protobuf.Rpc.internal_static_VoteResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse.class, io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse.Builder.class);
  }

  public static final int TERM_FIELD_NUMBER = 1;
  private int term_;
  /**
   * <code>optional int32 term = 1;</code>
   */
  public int getTerm() {
    return term_;
  }

  public static final int VOTE_GRANTED_FIELD_NUMBER = 2;
  private boolean voteGranted_;
  /**
   * <code>optional bool vote_granted = 2;</code>
   */
  public boolean getVoteGranted() {
    return voteGranted_;
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
    if (voteGranted_ != false) {
      output.writeBool(2, voteGranted_);
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
    if (voteGranted_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(2, voteGranted_);
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
    if (!(obj instanceof io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse)) {
      return super.equals(obj);
    }
    io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse other = (io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse) obj;

    boolean result = true;
    result = result && (getTerm()
        == other.getTerm());
    result = result && (getVoteGranted()
        == other.getVoteGranted());
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
    hash = (37 * hash) + VOTE_GRANTED_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getVoteGranted());
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse parseFrom(
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
  public static Builder newBuilder(io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse prototype) {
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
   * Protobuf type {@code VoteResponse}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:VoteResponse)
      io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return io.github.pmackowski.rsocket.raft.transport.protobuf.Rpc.internal_static_VoteResponse_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return io.github.pmackowski.rsocket.raft.transport.protobuf.Rpc.internal_static_VoteResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse.class, io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse.Builder.class);
    }

    // Construct using io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse.newBuilder()
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

      voteGranted_ = false;

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return io.github.pmackowski.rsocket.raft.transport.protobuf.Rpc.internal_static_VoteResponse_descriptor;
    }

    public io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse getDefaultInstanceForType() {
      return io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse.getDefaultInstance();
    }

    public io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse build() {
      io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse buildPartial() {
      io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse result = new io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse(this);
      result.term_ = term_;
      result.voteGranted_ = voteGranted_;
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
      if (other instanceof io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse) {
        return mergeFrom((io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse other) {
      if (other == io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse.getDefaultInstance()) return this;
      if (other.getTerm() != 0) {
        setTerm(other.getTerm());
      }
      if (other.getVoteGranted() != false) {
        setVoteGranted(other.getVoteGranted());
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
      io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private int term_ ;
    /**
     * <code>optional int32 term = 1;</code>
     */
    public int getTerm() {
      return term_;
    }
    /**
     * <code>optional int32 term = 1;</code>
     */
    public Builder setTerm(int value) {
      
      term_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 term = 1;</code>
     */
    public Builder clearTerm() {
      
      term_ = 0;
      onChanged();
      return this;
    }

    private boolean voteGranted_ ;
    /**
     * <code>optional bool vote_granted = 2;</code>
     */
    public boolean getVoteGranted() {
      return voteGranted_;
    }
    /**
     * <code>optional bool vote_granted = 2;</code>
     */
    public Builder setVoteGranted(boolean value) {
      
      voteGranted_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional bool vote_granted = 2;</code>
     */
    public Builder clearVoteGranted() {
      
      voteGranted_ = false;
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


    // @@protoc_insertion_point(builder_scope:VoteResponse)
  }

  // @@protoc_insertion_point(class_scope:VoteResponse)
  private static final io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse();
  }

  public static io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<VoteResponse>
      PARSER = new com.google.protobuf.AbstractParser<VoteResponse>() {
    public VoteResponse parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return new VoteResponse(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<VoteResponse> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<VoteResponse> getParserForType() {
    return PARSER;
  }

  public io.github.pmackowski.rsocket.raft.transport.protobuf.VoteResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
