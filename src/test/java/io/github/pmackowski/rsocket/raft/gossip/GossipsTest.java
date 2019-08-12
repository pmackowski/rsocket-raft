package io.github.pmackowski.rsocket.raft.gossip;

import io.github.pmackowski.rsocket.raft.gossip.protobuf.Ack;
import io.github.pmackowski.rsocket.raft.gossip.protobuf.Gossip;
import org.junit.jupiter.api.Test;

import static io.github.pmackowski.rsocket.raft.gossip.protobuf.Gossip.Suspicion.*;
import static org.assertj.core.api.Assertions.assertThat;

class GossipsTest {

    static final int INITIATOR_NODE_ID = 7000;
    static final int DESTINATION_NODE_ID = 7001;

    @Test
    void probeCompletedInitializedWithEmptyGossips() {
        // given
        Gossips gossips = Gossips.builder().nodeId(INITIATOR_NODE_ID).build();

        ProbeResult probeResult = ProbeResult.builder()
                .destinationNodeId(DESTINATION_NODE_ID)
                .subgroupSize(0)
                .probeResult(new ProbeOperatorResult<>(false,
                    Ack.newBuilder()
                        .addGossips(Gossip.newBuilder().setNodeId(7004).setSuspicion(SUSPECT).setIncarnation(1).build())
                        .addGossips(Gossip.newBuilder().setNodeId(7005).setSuspicion(ALIVE).setIncarnation(0).build())
                        .build()
                    )
                )
                .build();

        // when
        gossips.probeCompleted(probeResult);

        // then
        // produced by probeCompleted
        assertThat(gossips.getGossip(DESTINATION_NODE_ID)).hasValue(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(ALIVE).setIncarnation(0).build());
        // copied from Ack
        assertThat(gossips.getGossip(7004)).hasValue(Gossip.newBuilder().setNodeId(7004).setSuspicion(SUSPECT).setIncarnation(1).build());
        assertThat(gossips.getGossip(7005)).hasValue(Gossip.newBuilder().setNodeId(7005).setSuspicion(ALIVE).setIncarnation(0).build());
        assertThat(gossips.count()).isEqualTo(3);
    }

    @Test
    void probeCompletedInitializedWithGossips() {
        // given
        Gossips gossips = Gossips.builder()
                .nodeId(INITIATOR_NODE_ID)
                .addGossip(Gossip.newBuilder().setNodeId(7004).setSuspicion(SUSPECT).setIncarnation(1).build())
                .addGossip(Gossip.newBuilder().setNodeId(7005).setSuspicion(ALIVE).setIncarnation(0).build())
                .build();

        ProbeResult probeResult = ProbeResult.builder()
                .destinationNodeId(DESTINATION_NODE_ID)
                .subgroupSize(0)
                .probeResult(new ProbeOperatorResult<>(false,
                                Ack.newBuilder()
                                        .addGossips(Gossip.newBuilder().setNodeId(7004).setSuspicion(ALIVE).setIncarnation(0).build())
                                        .addGossips(Gossip.newBuilder().setNodeId(7006).setSuspicion(ALIVE).setIncarnation(0).build())
                                        .build()
                        )
                )
                .build();

        // when
        gossips.probeCompleted(probeResult);

        // then
        // produced by probeCompleted
        assertThat(gossips.getGossip(DESTINATION_NODE_ID)).hasValue(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(ALIVE).setIncarnation(0).build());
        // merged
        assertThat(gossips.getGossip(7004)).hasValue(Gossip.newBuilder().setNodeId(7004).setSuspicion(SUSPECT).setIncarnation(1).build());
        assertThat(gossips.getGossip(7005)).hasValue(Gossip.newBuilder().setNodeId(7005).setSuspicion(ALIVE).setIncarnation(0).build());
        assertThat(gossips.getGossip(7006)).hasValue(Gossip.newBuilder().setNodeId(7006).setSuspicion(ALIVE).setIncarnation(0).build());
        assertThat(gossips.count()).isEqualTo(4);
    }


    @Test
    void probeCompletedNoAcks() {
        // given
        Gossips gossips = Gossips.builder().nodeId(INITIATOR_NODE_ID).build();

        ProbeResult probeResult = ProbeResult.builder()
                .destinationNodeId(DESTINATION_NODE_ID)
                .subgroupSize(1)
                .probeResult(new ProbeOperatorResult<>(true))
                .build();

        // when
        gossips.probeCompleted(probeResult);

        // then
        assertThat(gossips.getGossip(DESTINATION_NODE_ID)).hasValue(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(SUSPECT).setIncarnation(0).build());
        assertThat(gossips.count()).isEqualTo(1);
    }


    @Test
    void probeCompletedMultipleAcks() {
        // given
        Gossips gossips = Gossips.builder().nodeId(INITIATOR_NODE_ID).build();

        ProbeResult probeResult = ProbeResult.builder()
                .destinationNodeId(DESTINATION_NODE_ID)
                .subgroupSize(1)
                .probeResult(new ProbeOperatorResult<>(true,
                                Ack.newBuilder()
                                        .addGossips(Gossip.newBuilder().setNodeId(7004).setSuspicion(ALIVE).setIncarnation(1).build())
                                        .addGossips(Gossip.newBuilder().setNodeId(7005).setSuspicion(ALIVE).setIncarnation(0).build())
                                        .build(),
                                Ack.newBuilder()
                                        .addGossips(Gossip.newBuilder().setNodeId(7004).setSuspicion(ALIVE).setIncarnation(0).build())
                                        .addGossips(Gossip.newBuilder().setNodeId(7006).setSuspicion(ALIVE).setIncarnation(0).build())
                                        .build()

                        )
                )
                .build();

        // when
        gossips.probeCompleted(probeResult);

        // then
        // produced by probeCompleted
        assertThat(gossips.getGossip(DESTINATION_NODE_ID)).hasValue(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(ALIVE).setIncarnation(0).build());
        // merged
        assertThat(gossips.getGossip(7004)).hasValue(Gossip.newBuilder().setNodeId(7004).setSuspicion(ALIVE).setIncarnation(1).build());
        assertThat(gossips.getGossip(7005)).hasValue(Gossip.newBuilder().setNodeId(7005).setSuspicion(ALIVE).setIncarnation(0).build());
        assertThat(gossips.getGossip(7006)).hasValue(Gossip.newBuilder().setNodeId(7006).setSuspicion(ALIVE).setIncarnation(0).build());
        assertThat(gossips.count()).isEqualTo(4);
    }

    @Test
    void probeCompletedOnlyNacks() { // nack can be returned by indirect ping
        // given
        Gossips gossips = Gossips.builder().nodeId(INITIATOR_NODE_ID).build();

        ProbeResult probeResult = ProbeResult.builder()
                .destinationNodeId(DESTINATION_NODE_ID)
                .subgroupSize(2)
                .probeResult(new ProbeOperatorResult<>(true,
                                Ack.newBuilder()
                                        .setNack(true)
                                        .addGossips(Gossip.newBuilder().setNodeId(7004).setSuspicion(ALIVE).setIncarnation(1).build())
                                        .addGossips(Gossip.newBuilder().setNodeId(7005).setSuspicion(ALIVE).setIncarnation(0).build())
                                        .build(),
                                Ack.newBuilder()
                                        .setNack(true)
                                        .addGossips(Gossip.newBuilder().setNodeId(7004).setSuspicion(ALIVE).setIncarnation(0).build())
                                        .addGossips(Gossip.newBuilder().setNodeId(7006).setSuspicion(ALIVE).setIncarnation(0).build())
                                        .build()

                        )
                )
                .build();

        // when
        gossips.probeCompleted(probeResult);

        // then
        // produced by probeCompleted
        assertThat(gossips.getGossip(DESTINATION_NODE_ID)).hasValue(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(SUSPECT).setIncarnation(0).build());
        // merged
        assertThat(gossips.getGossip(7004)).hasValue(Gossip.newBuilder().setNodeId(7004).setSuspicion(ALIVE).setIncarnation(1).build());
        assertThat(gossips.getGossip(7005)).hasValue(Gossip.newBuilder().setNodeId(7005).setSuspicion(ALIVE).setIncarnation(0).build());
        assertThat(gossips.getGossip(7006)).hasValue(Gossip.newBuilder().setNodeId(7006).setSuspicion(ALIVE).setIncarnation(0).build());
        assertThat(gossips.count()).isEqualTo(4);
    }

    @Test
    void probeCompletedAckWinsWithNack() { // nack can be returned by indirect ping
        // given
        Gossips gossips = Gossips.builder().nodeId(INITIATOR_NODE_ID).build();

        ProbeResult probeResult = ProbeResult.builder()
                .destinationNodeId(DESTINATION_NODE_ID)
                .subgroupSize(2)
                .probeResult(new ProbeOperatorResult<>(true,
                                Ack.newBuilder()
                                        .addGossips(Gossip.newBuilder().setNodeId(7004).setSuspicion(ALIVE).setIncarnation(1).build())
                                        .addGossips(Gossip.newBuilder().setNodeId(7005).setSuspicion(ALIVE).setIncarnation(0).build())
                                        .build(),
                                Ack.newBuilder()
                                        .setNack(true)
                                        .addGossips(Gossip.newBuilder().setNodeId(7004).setSuspicion(ALIVE).setIncarnation(0).build())
                                        .addGossips(Gossip.newBuilder().setNodeId(7006).setSuspicion(ALIVE).setIncarnation(0).build())
                                        .build()

                        )
                )
                .build();

        // when
        gossips.probeCompleted(probeResult);

        // then
        // produced by probeCompleted
        assertThat(gossips.getGossip(DESTINATION_NODE_ID)).hasValue(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(ALIVE).setIncarnation(0).build());
        // merged
        assertThat(gossips.getGossip(7004)).hasValue(Gossip.newBuilder().setNodeId(7004).setSuspicion(ALIVE).setIncarnation(1).build());
        assertThat(gossips.getGossip(7005)).hasValue(Gossip.newBuilder().setNodeId(7005).setSuspicion(ALIVE).setIncarnation(0).build());
        assertThat(gossips.getGossip(7006)).hasValue(Gossip.newBuilder().setNodeId(7006).setSuspicion(ALIVE).setIncarnation(0).build());
        assertThat(gossips.count()).isEqualTo(4);
    }

    @Test
    void probeCompletedNodeIsSuspected() {
        // given
        Gossips gossips = Gossips.builder().nodeId(INITIATOR_NODE_ID).build();

        ProbeResult probeResult = ProbeResult.builder()
                .destinationNodeId(DESTINATION_NODE_ID)
                .subgroupSize(0)
                .probeResult(new ProbeOperatorResult<>(false,
                                Ack.newBuilder()
                                        .addGossips(Gossip.newBuilder().setNodeId(INITIATOR_NODE_ID).setSuspicion(SUSPECT).setIncarnation(0).build())
                                        .build()
                        )
                )
                .build();

        // when
        gossips.probeCompleted(probeResult);

        // then
        // produced by probeCompleted
        assertThat(gossips.getGossip(DESTINATION_NODE_ID)).hasValue(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(ALIVE).setIncarnation(0).build());
        // merged
        assertThat(gossips.getGossip(INITIATOR_NODE_ID)).hasValue(Gossip.newBuilder().setNodeId(INITIATOR_NODE_ID).setSuspicion(ALIVE).setIncarnation(1).build());
        assertThat(gossips.count()).isEqualTo(2);
    }

    @Test
    void probeCompletedProbedNodeIsSuspected() {
        // given
        Gossips gossips = Gossips.builder()
                .nodeId(INITIATOR_NODE_ID)
                .addGossip(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(SUSPECT).setIncarnation(3).build())
                .build();

        ProbeResult probeResult = ProbeResult.builder()
                .destinationNodeId(DESTINATION_NODE_ID)
                .subgroupSize(0)
                .probeResult(new ProbeOperatorResult<>(false,
                                Ack.newBuilder()
                                        // we expect suspected node to refute suspicion by sending alive message with increased incarnation number
                                        .addGossips(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(ALIVE).setIncarnation(3+1).build())
                                        .build()
                        )
                )
                .build();

        // when
        gossips.probeCompleted(probeResult);

        // then
        // produced by probeCompleted
        assertThat(gossips.getGossip(DESTINATION_NODE_ID)).hasValue(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(ALIVE).setIncarnation(4).build());
        assertThat(gossips.count()).isEqualTo(1);
    }

    @Test
    void probeCompletedProducedIncarnationNeverDecreases() {
        // given
        Gossips gossips = Gossips.builder()
                .nodeId(INITIATOR_NODE_ID)
                .addGossip(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(ALIVE).setIncarnation(3).build())
                .build();

        ProbeResult probeResult = ProbeResult.builder()
                .destinationNodeId(DESTINATION_NODE_ID)
                .subgroupSize(2)
                .probeResult(new ProbeOperatorResult<>(true))
                .build();

        // when
        gossips.probeCompleted(probeResult);

        // then
        // produced by probeCompleted
        assertThat(gossips.getGossip(DESTINATION_NODE_ID)).hasValue(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(SUSPECT).setIncarnation(3).build());
        assertThat(gossips.count()).isEqualTo(1);
    }

    @Test
    void addDeadGossipAboutItself() { // incarnation number does not matter
        // given
        Gossips gossips = Gossips.builder()
                .nodeId(INITIATOR_NODE_ID)
                .addGossip(Gossip.newBuilder().setNodeId(INITIATOR_NODE_ID).setSuspicion(ALIVE).setIncarnation(3).build())
                .build();

        // when
        gossips.addGossip(Gossip.newBuilder().setNodeId(INITIATOR_NODE_ID).setSuspicion(DEAD).setIncarnation(0).build());

        // then
        assertThat(gossips.getGossip(INITIATOR_NODE_ID))
                .hasValue(Gossip.newBuilder().setNodeId(INITIATOR_NODE_ID).setSuspicion(DEAD).setIncarnation(0).build());
        assertThat(gossips.count()).isEqualTo(1);
    }

    @Test
    void addSuspectGossipAboutItselfWithSameIncarnationNumber() {
        // given
        Gossips gossips = Gossips.builder().nodeId(INITIATOR_NODE_ID).build();

        // when
        gossips.addGossip(Gossip.newBuilder().setNodeId(INITIATOR_NODE_ID).setSuspicion(SUSPECT).setIncarnation(0).build());

        // then
        assertThat(gossips.getGossip(INITIATOR_NODE_ID))
                .hasValue(Gossip.newBuilder().setNodeId(INITIATOR_NODE_ID).setSuspicion(ALIVE).setIncarnation(1).build());
        assertThat(gossips.incarnation()).isEqualTo(1);
        assertThat(gossips.count()).isEqualTo(1);
    }


    @Test
    void addSuspectGossipAboutItselfWithDifferentIncarnationNumber() {
        // given
        Gossips gossips = Gossips.builder()
                .nodeId(INITIATOR_NODE_ID)
                .incarnation(1)
                .build();

        // when
        gossips.addGossip(Gossip.newBuilder().setNodeId(INITIATOR_NODE_ID).setSuspicion(SUSPECT).setIncarnation(0).build());

        // then
        assertThat(gossips.incarnation()).isEqualTo(1);
        assertThat(gossips.count()).isEqualTo(0);
    }

    @Test
    void addAliveGossipAboutItselfIsIgnored() {
        // given
        Gossips gossips = Gossips.builder()
                .nodeId(INITIATOR_NODE_ID)
                .incarnation(0)
                .build();

        // when
        gossips.addGossip(Gossip.newBuilder().setNodeId(INITIATOR_NODE_ID).setSuspicion(ALIVE).setIncarnation(0).build());

        // then
        assertThat(gossips.incarnation()).isEqualTo(0);
        assertThat(gossips.count()).isEqualTo(0);
    }

    @Test
    void addAliveGossipAboutItselfWithGreaterIncarnationNumberIsAlsoIgnored() {
        // impossible situation as only node itself can increase its incarnation number,
        // solution - silently ignore gossip

        // given
        Gossips gossips = Gossips.builder()
                .nodeId(INITIATOR_NODE_ID)
                .addGossip(Gossip.newBuilder().setNodeId(INITIATOR_NODE_ID).setSuspicion(ALIVE).setIncarnation(1).build())
                .incarnation(1)
                .build();

        // when
        gossips.addGossip(Gossip.newBuilder().setNodeId(INITIATOR_NODE_ID).setSuspicion(ALIVE).setIncarnation(2).build());

        // then
        assertThat(gossips.incarnation()).isEqualTo(1);
        assertThat(gossips.getGossip(INITIATOR_NODE_ID))
                .hasValue(Gossip.newBuilder().setNodeId(INITIATOR_NODE_ID).setSuspicion(ALIVE).setIncarnation(1).build());

        assertThat(gossips.count()).isEqualTo(1);
    }

    @Test
    void addDeadGossip() { // incarnation number does not matter
        // given
        Gossips gossips = Gossips.builder()
                .nodeId(INITIATOR_NODE_ID)
                .addGossip(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(ALIVE).setIncarnation(3).build())
                .build();

        // when
        gossips.addGossip(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(DEAD).setIncarnation(0).build());

        // then
        assertThat(gossips.getGossip(DESTINATION_NODE_ID))
                .hasValue(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(DEAD).setIncarnation(0).build());
        assertThat(gossips.count()).isEqualTo(1);
    }

    @Test
    void addAliveGossipWhichOverrideOtherAlive() {
        // given
        Gossips gossips = Gossips.builder()
                .nodeId(INITIATOR_NODE_ID)
                .addGossip(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(ALIVE).setIncarnation(3).build(), 10)
                .build();

        // when
        gossips.addGossip(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(ALIVE).setIncarnation(4).build());

        // then
        assertThat(gossips.getGossip(DESTINATION_NODE_ID))
                .hasValue(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(ALIVE).setIncarnation(4).build());
        assertThat(gossips.getGossipShared(DESTINATION_NODE_ID)).isEqualTo(0);

        assertThat(gossips.count()).isEqualTo(1);
    }

    @Test
    void addAliveGossipWhichDoesNotOverrideOtherAlive() {
        // given
        Gossips gossips = Gossips.builder()
                .nodeId(INITIATOR_NODE_ID)
                .addGossip(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(ALIVE).setIncarnation(3).build(), 10)
                .build();

        // when
        gossips.addGossip(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(ALIVE).setIncarnation(3).build());

        // then
        assertThat(gossips.getGossip(DESTINATION_NODE_ID))
                .hasValue(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(ALIVE).setIncarnation(3).build());
        assertThat(gossips.getGossipShared(DESTINATION_NODE_ID)).isEqualTo(10);

        assertThat(gossips.count()).isEqualTo(1);
    }

    @Test
    void addAliveGossipWhichOverrideSuspectGossip() {
        // given
        Gossips gossips = Gossips.builder()
                .nodeId(INITIATOR_NODE_ID)
                .addGossip(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(SUSPECT).setIncarnation(3).build(), 10)
                .build();

        // when
        gossips.addGossip(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(ALIVE).setIncarnation(4).build());

        // then
        assertThat(gossips.getGossip(DESTINATION_NODE_ID))
                .hasValue(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(ALIVE).setIncarnation(4).build());
        assertThat(gossips.getGossipShared(DESTINATION_NODE_ID)).isEqualTo(0);

        assertThat(gossips.count()).isEqualTo(1);
    }

    @Test
    void addAliveGossipWhichDoesNotOverrideSuspectGossip() {
        // given
        Gossips gossips = Gossips.builder()
                .nodeId(INITIATOR_NODE_ID)
                .addGossip(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(SUSPECT).setIncarnation(3).build(), 10)
                .build();

        // when
        gossips.addGossip(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(ALIVE).setIncarnation(3).build());

        // then
        assertThat(gossips.getGossip(DESTINATION_NODE_ID))
                .hasValue(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(SUSPECT).setIncarnation(3).build());
        assertThat(gossips.getGossipShared(DESTINATION_NODE_ID)).isEqualTo(10);

        assertThat(gossips.count()).isEqualTo(1);
    }

    @Test
    void addSuspectGossipWhichOverrideOtherSuspectGossip() {
        // given
        Gossips gossips = Gossips.builder()
                .nodeId(INITIATOR_NODE_ID)
                .addGossip(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(SUSPECT).setIncarnation(3).build(), 10)
                .build();

        // when
        gossips.addGossip(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(SUSPECT).setIncarnation(4).build());

        // then
        assertThat(gossips.getGossip(DESTINATION_NODE_ID))
                .hasValue(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(SUSPECT).setIncarnation(4).build());
        assertThat(gossips.getGossipShared(DESTINATION_NODE_ID)).isEqualTo(0);

        assertThat(gossips.count()).isEqualTo(1);
    }

    @Test
    void addSuspectGossipWhichDoesNotOverrideOtherSuspectGossip() {
        // given
        Gossips gossips = Gossips.builder()
                .nodeId(INITIATOR_NODE_ID)
                .addGossip(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(SUSPECT).setIncarnation(3).build(), 10)
                .build();

        // when
        gossips.addGossip(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(SUSPECT).setIncarnation(3).build());

        // then
        assertThat(gossips.getGossip(DESTINATION_NODE_ID))
                .hasValue(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(SUSPECT).setIncarnation(3).build());
        assertThat(gossips.getGossipShared(DESTINATION_NODE_ID)).isEqualTo(10);

        assertThat(gossips.count()).isEqualTo(1);
    }

    @Test
    void addSuspectGossipWhichOverrideAliveGossip() {
        // given
        Gossips gossips = Gossips.builder()
                .nodeId(INITIATOR_NODE_ID)
                .addGossip(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(ALIVE).setIncarnation(3).build(), 10)
                .build();

        // when
        gossips.addGossip(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(SUSPECT).setIncarnation(3).build());

        // then
        assertThat(gossips.getGossip(DESTINATION_NODE_ID))
                .hasValue(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(SUSPECT).setIncarnation(3).build());
        assertThat(gossips.getGossipShared(DESTINATION_NODE_ID)).isEqualTo(0);

        assertThat(gossips.count()).isEqualTo(1);
    }

    @Test
    void addSuspectGossipWhichDoesNotOverrideAliveGossip() {
        // given
        Gossips gossips = Gossips.builder()
                .nodeId(INITIATOR_NODE_ID)
                .addGossip(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(ALIVE).setIncarnation(4).build(), 10)
                .build();

        // when
        gossips.addGossip(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(SUSPECT).setIncarnation(3).build());

        // then
        assertThat(gossips.getGossip(DESTINATION_NODE_ID))
                .hasValue(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(ALIVE).setIncarnation(4).build());
        assertThat(gossips.getGossipShared(DESTINATION_NODE_ID)).isEqualTo(10);

        assertThat(gossips.count()).isEqualTo(1);
    }

    @Test
    void addSuspectGossipNeverOverrideDeadGossip() {
        // given
        Gossips gossips = Gossips.builder()
                .nodeId(INITIATOR_NODE_ID)
                .addGossip(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(DEAD).setIncarnation(0).build(), 10)
                .build();

        // when
        gossips.addGossip(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(SUSPECT).setIncarnation(1).build());

        // then
        assertThat(gossips.getGossip(DESTINATION_NODE_ID))
                .hasValue(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(DEAD).setIncarnation(0).build());
        assertThat(gossips.getGossipShared(DESTINATION_NODE_ID)).isEqualTo(10);

        assertThat(gossips.count()).isEqualTo(1);
    }

    @Test
    void addAliveGossipNeverOverrideDeadGossip() {
        // given
        Gossips gossips = Gossips.builder()
                .nodeId(INITIATOR_NODE_ID)
                .addGossip(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(DEAD).setIncarnation(0).build(), 10)
                .build();

        // when
        gossips.addGossip(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(ALIVE).setIncarnation(1).build());

        // then
        assertThat(gossips.getGossip(DESTINATION_NODE_ID))
                .hasValue(Gossip.newBuilder().setNodeId(DESTINATION_NODE_ID).setSuspicion(DEAD).setIncarnation(0).build());
        assertThat(gossips.getGossipShared(DESTINATION_NODE_ID)).isEqualTo(10);

        assertThat(gossips.count()).isEqualTo(1);
    }
}