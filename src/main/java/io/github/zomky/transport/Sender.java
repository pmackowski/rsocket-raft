package io.github.zomky.transport;

import com.google.protobuf.InvalidProtocolBufferException;
import io.github.zomky.raft.RaftException;
import io.github.zomky.raft.RaftGroup;
import io.github.zomky.transport.protobuf.*;
import io.github.zomky.utils.NettyUtils;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.ByteBufPayload;
import reactor.core.publisher.Mono;

public class Sender {

    private final int nodeId;
    private final Mono<RSocket> monoRaftSocket;
    private final boolean available;

    public static Sender createSender(int nodeId) {
        return availableSender(nodeId); // TODO
    }

    public static Sender availableSender(int nodeId) {
        final Mono<RSocket> mono = RSocketFactory.connect()
                .transport(TcpClientTransport.create(nodeId))
                .start()
                .cache();
        return new Sender(nodeId, mono, true);
    }

    public static Sender unavailableSender(int nodeId) {
        return new Sender(nodeId, Mono.error(new RuntimeException("ZOMKY NO NO !!!")),false);
    }

    public Sender(int nodeId, Mono<RSocket> monoRaftSocket, boolean available) {
        this.nodeId = nodeId;
        this.monoRaftSocket = monoRaftSocket;
        this.available = available;
    }

    private byte[] metadataRequest(String groupName, RpcType rpcType) {
        MetadataRequest metadataRequest = MetadataRequest.newBuilder()
                .setGroupName(groupName)
                .setMessageType(rpcType.getCode())
                .build();
        return metadataRequest.toByteArray();
    }

    private Mono<Payload> requestResponse(Payload payload) {
        return monoRaftSocket.flatMap(raftSocket -> raftSocket.requestResponse(payload));
    }

    public Mono<PreVoteResponse> requestPreVote(RaftGroup raftGroup, PreVoteRequest preVoteRequest) {
        Payload payload = ByteBufPayload.create(preVoteRequest.toByteArray(), metadataRequest(raftGroup.getGroupName(), RpcType.PRE_REQUEST_VOTE));

        return requestResponse(payload)
                .map(payload1 -> {
                    try {
                        return PreVoteResponse.parseFrom(NettyUtils.toByteArray(payload1.sliceData()));
                    } catch (InvalidProtocolBufferException e) {
                        throw new RaftException("Invalid pre-vote response!", e);
                    }
                });
    }

    public Mono<VoteResponse> requestVote(RaftGroup raftGroup, VoteRequest voteRequest) {
        Payload payload = ByteBufPayload.create(voteRequest.toByteArray(), metadataRequest(raftGroup.getGroupName(), RpcType.REQUEST_VOTE));
        return requestResponse(payload)
                .map(payload1 -> {
                    try {
                        return VoteResponse.parseFrom(NettyUtils.toByteArray(payload1.sliceData()));
                    } catch (InvalidProtocolBufferException e) {
                        throw new RaftException("Invalid vote response!", e);
                    }
                });
    }

    public Mono<AppendEntriesResponse> appendEntries(RaftGroup raftGroup, AppendEntriesRequest appendEntriesRequest) {
        Payload payload = ByteBufPayload.create(appendEntriesRequest.toByteArray(), metadataRequest(raftGroup.getGroupName(), RpcType.APPEND_ENTRIES));
        return requestResponse(payload)
                .map(appendEntriesResponsePayload -> {
                    try {
                        return AppendEntriesResponse.parseFrom(NettyUtils.toByteArray(appendEntriesResponsePayload.sliceData()));
                    } catch (InvalidProtocolBufferException e) {
                        throw new RaftException("Invalid append entries response!", e);
                    } finally {
                        appendEntriesResponsePayload.release();
                    }
                });
    }

    public Mono<AddServerResponse> addServer(String groupName, AddServerRequest addServerRequest) {
        Payload payload = ByteBufPayload.create(addServerRequest.toByteArray(), metadataRequest(groupName, RpcType.ADD_SERVER));
        return requestResponse(payload)
                .map(payload1 -> {
                    try {
                        return AddServerResponse.parseFrom(NettyUtils.toByteArray(payload1.sliceData()));
                    } catch (InvalidProtocolBufferException e) {
                        throw new RaftException("Invalid add server response!", e);
                    }
                });
    }

    public Mono<RemoveServerResponse> removeServer(String groupName, RemoveServerRequest removeServerRequest) {
        Payload payload = ByteBufPayload.create(removeServerRequest.toByteArray(), metadataRequest(groupName, RpcType.REMOVE_SERVER));
        return requestResponse(payload)
                .map(payload1 -> {
                    try {
                        return RemoveServerResponse.parseFrom(NettyUtils.toByteArray(payload1.sliceData()));
                    } catch (InvalidProtocolBufferException e) {
                        throw new RaftException("Invalid remove server response!", e);
                    }
                });
    }

    public Mono<AddGroupResponse> addGroup(String groupName, AddGroupRequest createGroupRequest) {
        Payload payload = ByteBufPayload.create(createGroupRequest.toByteArray(), metadataRequest(groupName, RpcType.ADD_GROUP));
        return requestResponse(payload)
                .map(payload1 -> {
                    try {
                        return AddGroupResponse.parseFrom(NettyUtils.toByteArray(payload1.sliceData()));
                    } catch (InvalidProtocolBufferException e) {
                        throw new RaftException("Invalid add group response!", e);
                    }
                });
    }

    public int getNodeId() {
        return nodeId;
    }

    public boolean isAvailable() {
        return available;
    }

    public boolean isNotAvailable() {
        return !available;
    }

    @Override
    public String toString() {
        return "Sender{" +
                "nodeId=" + nodeId +
                ", available=" + available +
                '}';
    }

    public void stop() {
//        if (raftSocket != null) raftSocket.dispose();
    }

}
