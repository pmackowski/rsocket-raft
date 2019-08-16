package io.github.pmackowski.rsocket.raft.gossip;

import io.github.pmackowski.rsocket.raft.gossip.protobuf.Ack;
import io.github.pmackowski.rsocket.raft.gossip.protobuf.Gossip;
import io.github.pmackowski.rsocket.raft.gossip.protobuf.Ping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GossipProbeTest {

    private static final int NODE_ID = 7000;
    private static final int DESTINATION_NODE_ID = 7001;

    @Mock
    private GossipTransport gossipTransport;

    private List<Gossip> gossips = new ArrayList<>();

    private GossipProbe gossipProbe;

    @BeforeEach
    void setUp() {
        // gossip value not important (not mocked because Gossip is final)
        gossips = new ArrayList<>();
        gossips.add(Gossip.newBuilder().setNodeId(1).build());
        gossipProbe = new GossipProbe(NODE_ID, gossipTransport);
    }

    @Test
    void noPeers() { // cluster with one node
        StepVerifier.create(gossipProbe.probeNode(PeerProbe.NO_PEER_PROBE, gossips, Duration.ofMillis(10), Duration.ofMillis(30)))
                .expectSubscription()
                .thenAwait(Duration.ofMillis(30))
                .assertNext(probeResult -> assertThat(probeResult).isEqualTo(ProbeResult.NO_PROBE_ACKS))
                .verifyComplete();

        verify(gossipTransport, never()).ping(any());
    }

    @Test
    void onePeerDirectSuccessfulWithinRoundTripTime() { // cluster with two nodes
        PeerProbe peerProbe = new PeerProbe(DESTINATION_NODE_ID, Collections.emptyList());

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(NODE_ID)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(true)
                .setCounter(1)
                .build())
        ).willReturn(Flux.just(Ack.newBuilder().setNodeId(DESTINATION_NODE_ID).build()));

        StepVerifier.create(gossipProbe.probeNode(peerProbe, gossips, Duration.ofMillis(20), Duration.ofMillis(50)))
                .expectSubscription()
                .thenAwait(Duration.ofMillis(50))
                .assertNext(probeResult -> {
                    assertThat(probeResult.getDestinationNodeId()).isEqualTo(DESTINATION_NODE_ID);
                    assertThat(probeResult.hasAck()).isTrue();
                    assertThat(probeResult.getAcks()).containsExactly(Ack.newBuilder().setNodeId(DESTINATION_NODE_ID).build());
                    assertThat(probeResult.getSubgroupSize()).isEqualTo(0);
                    assertThat(probeResult.hasMissedNack()).isFalse();
                })
                .verifyComplete();

        verify(gossipTransport).ping(any());
    }

    @Test
    void onePeerDirectSuccessfulAfterRoundTripTime() { // cluster with two nodes
        PeerProbe peerProbe = new PeerProbe(DESTINATION_NODE_ID, Collections.emptyList());

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(NODE_ID)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(true)
                .setCounter(1)
                .build())
        ).willReturn(Mono.delay(Duration.ofMillis(30)).thenMany(Flux.just(Ack.newBuilder().setNodeId(DESTINATION_NODE_ID).build())));

        StepVerifier.create(gossipProbe.probeNode(peerProbe, gossips, Duration.ofMillis(10), Duration.ofMillis(50)))
                .expectSubscription()
                .thenAwait(Duration.ofMillis(50))
                .assertNext(probeResult -> {
                    assertThat(probeResult.getDestinationNodeId()).isEqualTo(DESTINATION_NODE_ID);
                    assertThat(probeResult.hasAck()).isTrue();
                    assertThat(probeResult.getAcks()).containsExactly(Ack.newBuilder().setNodeId(DESTINATION_NODE_ID).build());
                    assertThat(probeResult.getSubgroupSize()).isEqualTo(0);
                    assertThat(probeResult.hasMissedNack()).isFalse();
                })
                .verifyComplete();

        verify(gossipTransport).ping(any());
    }

    @Test
    void onePeerDirectSuccessfulAfterProbeTimeout() { // cluster with two nodes
        PeerProbe peerProbe = new PeerProbe(DESTINATION_NODE_ID, Collections.emptyList());

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(NODE_ID)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(true)
                .setCounter(1)
                .build())
        ).willReturn(Mono.delay(Duration.ofMillis(200)).thenMany(Flux.just(Ack.newBuilder().setNodeId(DESTINATION_NODE_ID).build())));

        StepVerifier.create(gossipProbe.probeNode(peerProbe, gossips, Duration.ofMillis(10), Duration.ofMillis(100)))
                .expectSubscription()
                .thenAwait(Duration.ofMillis(100))
                .assertNext(probeResult -> {
                    assertThat(probeResult.getDestinationNodeId()).isEqualTo(DESTINATION_NODE_ID);
                    assertThat(probeResult.hasAck()).isFalse();
                    assertThat(probeResult.getAcks()).isEmpty();
                    assertThat(probeResult.getSubgroupSize()).isEqualTo(0);
                    assertThat(probeResult.hasMissedNack()).isFalse();
                })
                .verifyComplete();

        verify(gossipTransport).ping(any());
    }

    @Test
    void onePeerDirectError() { // cluster with two nodes
        PeerProbe peerProbe = new PeerProbe(DESTINATION_NODE_ID, Collections.emptyList());

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(NODE_ID)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(true)
                .setCounter(1)
                .build())
        ).willReturn(Flux.error(new TimeoutException()));

        StepVerifier.create(gossipProbe.probeNode(peerProbe, gossips, Duration.ofMillis(10), Duration.ofMillis(50)))
                .expectSubscription()
                .thenAwait(Duration.ofMillis(50))
                .assertNext(probeResult -> {
                    assertThat(probeResult.getDestinationNodeId()).isEqualTo(DESTINATION_NODE_ID);
                    assertThat(probeResult.hasAck()).isFalse();
                    assertThat(probeResult.getAcks()).isEmpty();
                    assertThat(probeResult.hasMissedNack()).isFalse();
                })
                .verifyComplete();

        verify(gossipTransport).ping(any());
    }

    @Test
    void manyPeersSuccessfulDirectWithinRoundTripTime() {
        List<Integer> proxyNodeIds = Arrays.asList(7002,7003);
        PeerProbe peerProbe = new PeerProbe(DESTINATION_NODE_ID, proxyNodeIds);

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(NODE_ID)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(true)
                .setCounter(1)
                .build())
        ).willReturn(Flux.just(Ack.newBuilder().setNodeId(DESTINATION_NODE_ID).build()));

        StepVerifier.create(gossipProbe.probeNode(peerProbe, gossips, Duration.ofMillis(20), Duration.ofMillis(50)))
                    .expectSubscription()
                    .assertNext(probeResult -> {
                        assertThat(probeResult.getDestinationNodeId()).isEqualTo(DESTINATION_NODE_ID);
                        assertThat(probeResult.hasAck()).isTrue();
                        assertThat(probeResult.getAcks()).containsExactly(Ack.newBuilder().setNodeId(DESTINATION_NODE_ID).build());
                        assertThat(probeResult.getSubgroupSize()).isEqualTo(0);
                        assertThat(probeResult.hasMissedNack()).isFalse();
                    })
                    .verifyComplete();

        verify(gossipTransport).ping(any());
    }

    @Test
    void manyPeersSuccessfulDirectAfterRoundTripTime() {
        List<Integer> proxyNodeIds = Arrays.asList(7002,7003);
        PeerProbe peerProbe = new PeerProbe(DESTINATION_NODE_ID, proxyNodeIds);

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(NODE_ID)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(true)
                .setCounter(1)
                .build())
        ).willReturn(Mono.delay(Duration.ofMillis(80)).thenMany(Flux.just(Ack.newBuilder().setNodeId(DESTINATION_NODE_ID).build())));

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(7002)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(false)
                .setCounter(0)
                .build())
        ).willReturn(Mono.delay(Duration.ofMillis(70)).thenMany(Flux.just(Ack.newBuilder().setNodeId(7002).build())));

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(7003)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(false)
                .setCounter(0)
                .build())
        ).willReturn(Flux.just(Ack.newBuilder().setNodeId(7003).build()));

        // expected order 7003 (indirect), 7001 (direct), 7002 (indirect)
        StepVerifier.create(gossipProbe.probeNode(peerProbe, gossips, Duration.ofMillis(50), Duration.ofMillis(200)))
                .expectSubscription()
                .assertNext(probeResult -> {
                    assertThat(probeResult.getDestinationNodeId()).isEqualTo(DESTINATION_NODE_ID);
                    assertThat(probeResult.hasAck()).isTrue();
                    assertThat(probeResult.getAcks()).containsExactly(
                            Ack.newBuilder().setNodeId(7003).build(),
                            Ack.newBuilder().setNodeId(DESTINATION_NODE_ID).build(),
                            Ack.newBuilder().setNodeId(7002).build()
                    );
                    assertThat(probeResult.getSubgroupSize()).isEqualTo(2);
                    assertThat(probeResult.hasMissedNack()).isFalse();
                })
                .verifyComplete();

        verify(gossipTransport, times(3)).ping(any());
    }

    @Test
    void manyPeersFailedDirectAfterRoundTripTimeAndSuccessfulIndirect() {
        List<Integer> proxyNodeIds = Arrays.asList(7002,7003);
        PeerProbe peerProbe = new PeerProbe(DESTINATION_NODE_ID, proxyNodeIds);

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(NODE_ID)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(true)
                .setCounter(1)
                .build())
        ).willReturn(Mono.delay(Duration.ofMillis(30)).thenMany(Flux.error(new TimeoutException())));

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(7002)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(false)
                .setCounter(0)
                .build())
        ).willReturn(Mono.delay(Duration.ofMillis(15)).thenMany(Flux.just(Ack.newBuilder().setNodeId(7002).build())));

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(7003)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(false)
                .setCounter(0)
                .build())
        ).willReturn(Flux.just(Ack.newBuilder().setNodeId(7003).build()));

        // expected order 7003 (indirect), 7002 (indirect)
        StepVerifier.create(gossipProbe.probeNode(peerProbe, gossips, Duration.ofMillis(10), Duration.ofMillis(100)))
                .expectSubscription()
                .assertNext(probeResult -> {
                    assertThat(probeResult.getDestinationNodeId()).isEqualTo(DESTINATION_NODE_ID);
                    assertThat(probeResult.hasAck()).isTrue();
                    assertThat(probeResult.getAcks()).containsExactly(
                            Ack.newBuilder().setNodeId(7003).build(),
                            Ack.newBuilder().setNodeId(7002).build()
                    );
                    assertThat(probeResult.getSubgroupSize()).isEqualTo(2);
                    assertThat(probeResult.hasMissedNack()).isFalse();
                })
                .verifyComplete();

        verify(gossipTransport, times(3)).ping(any());
    }

    @Test
    void manyPeersFailedDirectWithinRoundTripTimeAndSuccessfulIndirect() {
        List<Integer> proxyNodeIds = Arrays.asList(7002,7003);
        PeerProbe peerProbe = new PeerProbe(DESTINATION_NODE_ID, proxyNodeIds);

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(NODE_ID)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(true)
                .setCounter(1)
                .build())
        ).willReturn(Flux.error(new TimeoutException()));

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(7002)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(false)
                .setCounter(0)
                .build())
        ).willReturn(Mono.delay(Duration.ofMillis(15)).thenMany(Flux.just(Ack.newBuilder().setNodeId(7002).build())));

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(7003)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(false)
                .setCounter(0)
                .build())
        ).willReturn(Flux.just(Ack.newBuilder().setNodeId(7003).build()));

        // expected order 7003 (indirect), 7002 (indirect)
        StepVerifier.create(gossipProbe.probeNode(peerProbe, gossips, Duration.ofMillis(10), Duration.ofMillis(50)))
                .expectSubscription()
                .assertNext(probeResult -> {
                    assertThat(probeResult.getDestinationNodeId()).isEqualTo(DESTINATION_NODE_ID);
                    assertThat(probeResult.hasAck()).isTrue();
                    assertThat(probeResult.getAcks()).containsExactly(
                            Ack.newBuilder().setNodeId(7003).build(),
                            Ack.newBuilder().setNodeId(7002).build()
                    );
                    assertThat(probeResult.getSubgroupSize()).isEqualTo(2);
                    assertThat(probeResult.hasMissedNack()).isFalse();
                })
                .verifyComplete();

        verify(gossipTransport, times(3)).ping(any());
    }

    @Test
    void manyPeersSuccessfulDirectAfterRoundTripTimeAndFailedIndirect() {
        List<Integer> proxyNodeIds = Arrays.asList(7002,7003);
        PeerProbe peerProbe = new PeerProbe(DESTINATION_NODE_ID, proxyNodeIds);

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(NODE_ID)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(true)
                .setCounter(1)
                .build())
        ).willReturn(Mono.delay(Duration.ofMillis(15)).thenMany(Flux.just(Ack.newBuilder().setNodeId(DESTINATION_NODE_ID).build())));

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(7002)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(false)
                .setCounter(0)
                .build())
        ).willReturn(Flux.error(new TimeoutException()));

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(7003)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(false)
                .setCounter(0)
                .build())
        ).willReturn(Flux.error(new TimeoutException()));

        StepVerifier.create(gossipProbe.probeNode(peerProbe, gossips, Duration.ofMillis(10), Duration.ofMillis(30)))
                .expectSubscription()
                .assertNext(probeResult -> {
                    assertThat(probeResult.getDestinationNodeId()).isEqualTo(DESTINATION_NODE_ID);
                    assertThat(probeResult.hasAck()).isTrue();
                    assertThat(probeResult.getAcks()).containsExactly(
                            Ack.newBuilder().setNodeId(DESTINATION_NODE_ID).build()
                    );
                    assertThat(probeResult.getSubgroupSize()).isEqualTo(2);
                    assertThat(probeResult.hasMissedNack()).isTrue();
                })
                .verifyComplete();

        verify(gossipTransport, times(3)).ping(any());
    }

    @Test
    void manyPeersSuccessfulDirectAfterRoundTripTimeAndIndirectCheckDelayError() {
        List<Integer> proxyNodeIds = Arrays.asList(7002,7003);
        PeerProbe peerProbe = new PeerProbe(DESTINATION_NODE_ID, proxyNodeIds);

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(NODE_ID)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(true)
                .setCounter(1)
                .build())
        ).willReturn(Mono.delay(Duration.ofMillis(100)).thenMany(Flux.just(Ack.newBuilder().setNodeId(DESTINATION_NODE_ID).build())));

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(7002)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(false)
                .setCounter(0)
                .build())
        ).willReturn(Flux.error(new TimeoutException()));

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(7003)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(false)
                .setCounter(0)
                .build())
        ).willReturn(Mono.delay(Duration.ofMillis(10)).thenMany(Flux.just(Ack.newBuilder().setNodeId(7003).build())));

        StepVerifier.create(gossipProbe.probeNode(peerProbe, gossips, Duration.ofMillis(10), Duration.ofMillis(200)))
                .expectSubscription()
                .assertNext(probeResult -> {
                    assertThat(probeResult.getDestinationNodeId()).isEqualTo(DESTINATION_NODE_ID);
                    assertThat(probeResult.hasAck()).isTrue();
                    assertThat(probeResult.getAcks()).containsExactly(
                            Ack.newBuilder().setNodeId(7003).build(),
                            Ack.newBuilder().setNodeId(DESTINATION_NODE_ID).build()
                    );
                    assertThat(probeResult.getSubgroupSize()).isEqualTo(2);
                    assertThat(probeResult.hasMissedNack()).isTrue();
                })
                .verifyComplete();

        verify(gossipTransport, times(3)).ping(any());
    }

    @Test
    void manyPeersFailedDirectAfterRoundTripTimeAndNackIndirect() {
        List<Integer> proxyNodeIds = Arrays.asList(7002,7003);
        PeerProbe peerProbe = new PeerProbe(DESTINATION_NODE_ID, proxyNodeIds);

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(NODE_ID)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(true)
                .setCounter(1)
                .build())
        ).willReturn(Mono.delay(Duration.ofMillis(30)).thenMany(Flux.error(new TimeoutException())));

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(7002)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(false)
                .setCounter(0)
                .build())
        ).willReturn(Mono.delay(Duration.ofMillis(15)).thenMany(Flux.just(Ack.newBuilder().setNodeId(7002).setNack(true).build())));

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(7003)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(false)
                .setCounter(0)
                .build())
        ).willReturn(Flux.just(Ack.newBuilder().setNodeId(7003).setNack(true).build()));

        // expected order 7003 (indirect), 7002 (indirect)
        StepVerifier.create(gossipProbe.probeNode(peerProbe, gossips, Duration.ofMillis(10), Duration.ofMillis(100)))
                .expectSubscription()
                .assertNext(probeResult -> {
                    assertThat(probeResult.getDestinationNodeId()).isEqualTo(DESTINATION_NODE_ID);
                    assertThat(probeResult.hasAck()).isFalse();
                    assertThat(probeResult.getAcks()).containsExactly(
                            Ack.newBuilder().setNodeId(7003).setNack(true).build(),
                            Ack.newBuilder().setNodeId(7002).setNack(true).build()
                    );
                    assertThat(probeResult.getSubgroupSize()).isEqualTo(2);
                    assertThat(probeResult.hasMissedNack()).isFalse();
                })
                .verifyComplete();

        verify(gossipTransport, times(3)).ping(any());
    }

    @Test
    void manyPeersFailedDirectAfterRoundTripTimeAndPartialNackIndirect() {
        List<Integer> proxyNodeIds = Arrays.asList(7002,7003);
        PeerProbe peerProbe = new PeerProbe(DESTINATION_NODE_ID, proxyNodeIds);

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(NODE_ID)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(true)
                .setCounter(1)
                .build())
        ).willReturn(Mono.delay(Duration.ofMillis(30)).thenMany(Flux.error(new TimeoutException())));

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(7002)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(false)
                .setCounter(0)
                .build())
        ).willReturn(Mono.delay(Duration.ofMillis(15)).thenMany(Flux.just(Ack.newBuilder().setNodeId(7002).setNack(true).build())));

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(7003)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(false)
                .setCounter(0)
                .build())
        ).willReturn(Flux.just(Ack.newBuilder().setNodeId(7003).build()));

        // expected order 7003 (indirect), 7002 (indirect)
        StepVerifier.create(gossipProbe.probeNode(peerProbe, gossips, Duration.ofMillis(10), Duration.ofMillis(100)))
                .expectSubscription()
                .assertNext(probeResult -> {
                    assertThat(probeResult.getDestinationNodeId()).isEqualTo(DESTINATION_NODE_ID);
                    assertThat(probeResult.hasAck()).isTrue();
                    assertThat(probeResult.getAcks()).containsExactly(
                            Ack.newBuilder().setNodeId(7003).build(),
                            Ack.newBuilder().setNodeId(7002).setNack(true).build()
                    );
                    assertThat(probeResult.getSubgroupSize()).isEqualTo(2);
                    assertThat(probeResult.hasMissedNack()).isFalse();
                })
                .verifyComplete();

        verify(gossipTransport, times(3)).ping(any());
    }

    @Test
    void manyPeersSuccessfulDirectAfterRoundTripTimeAndOneIndirectReturnsNackAndAck() {
        // lifeguard - LHA-Probe
        List<Integer> proxyNodeIds = Arrays.asList(7002,7003);
        PeerProbe peerProbe = new PeerProbe(DESTINATION_NODE_ID, proxyNodeIds);

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(NODE_ID)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(true)
                .setCounter(1)
                .build())
        ).willReturn(Flux.just(Ack.newBuilder().setNodeId(DESTINATION_NODE_ID).build()).delayElements(Duration.ofMillis(100)));

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(7002)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(false)
                .setCounter(0)
                .build())
        ).willReturn(Flux.error(new TimeoutException()));

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(7003)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(false)
                .setCounter(0)
                .build())
        ).willReturn(Flux.merge(
                Flux.just(Ack.newBuilder().setNodeId(7003).setNack(true).build()),
                Flux.just(Ack.newBuilder().setNodeId(7003).build()).delayElements(Duration.ofMillis(120))
                )
        );

        StepVerifier.create(gossipProbe.probeNode(peerProbe, gossips, Duration.ofMillis(10), Duration.ofMillis(300)))
                .expectSubscription()
                .assertNext(probeResult -> {
                    assertThat(probeResult.getDestinationNodeId()).isEqualTo(DESTINATION_NODE_ID);
                    assertThat(probeResult.hasAck()).isTrue();
                    assertThat(probeResult.getAcks()).containsExactly(
                            Ack.newBuilder().setNodeId(7003).setNack(true).build(),
                            Ack.newBuilder().setNodeId(DESTINATION_NODE_ID).build(),
                            Ack.newBuilder().setNodeId(7003).build()
                    );
                    assertThat(probeResult.getDistinctAcks()).containsExactlyInAnyOrder(
                            Ack.newBuilder().setNodeId(DESTINATION_NODE_ID).build(),
                            Ack.newBuilder().setNodeId(7003).build()
                    );
                    assertThat(probeResult.getSubgroupSize()).isEqualTo(2);
                    assertThat(probeResult.hasMissedNack()).isTrue();
                })
                .verifyComplete();

        verify(gossipTransport, times(3)).ping(any());
    }

    @Test
    void manyPeersSuccessfulDirectAfterRoundTripTimeCheckMissedNack() {
        // lifeguard - LHA-Probe
        List<Integer> proxyNodeIds = Arrays.asList(7002,7003);
        PeerProbe peerProbe = new PeerProbe(DESTINATION_NODE_ID, proxyNodeIds);

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(NODE_ID)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(true)
                .setCounter(1)
                .build())
        ).willReturn(Flux.just(Ack.newBuilder().setNodeId(DESTINATION_NODE_ID).build()).delayElements(Duration.ofMillis(100)));

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(7002)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(false)
                .setCounter(0)
                .build())
        // missed nack
        ).willReturn(Flux.just(Ack.newBuilder().setNodeId(DESTINATION_NODE_ID).build()).delayElements(Duration.ofDays(1)));

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(7003)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(false)
                .setCounter(0)
                .build())
        ).willReturn(Flux.just(Ack.newBuilder().setNodeId(7003).setNack(true).build(), Ack.newBuilder().setNodeId(7003).build())
                .delayElements(Duration.ofMillis(70))
        );

        StepVerifier.create(gossipProbe.probeNode(peerProbe, gossips, Duration.ofMillis(10), Duration.ofMillis(200)))
                .expectSubscription()
                .assertNext(probeResult -> {
                    assertThat(probeResult.getDestinationNodeId()).isEqualTo(DESTINATION_NODE_ID);
                    assertThat(probeResult.hasAck()).isTrue();
                    assertThat(probeResult.getAcks()).containsExactly(
                            Ack.newBuilder().setNodeId(7003).setNack(true).build(),
                            Ack.newBuilder().setNodeId(DESTINATION_NODE_ID).build(),
                            Ack.newBuilder().setNodeId(7003).build()
                    );
                    assertThat(probeResult.getDistinctAcks()).containsExactlyInAnyOrder(
                            Ack.newBuilder().setNodeId(DESTINATION_NODE_ID).build(),
                            Ack.newBuilder().setNodeId(7003).build()
                    );
                    assertThat(probeResult.getSubgroupSize()).isEqualTo(2);
                    assertThat(probeResult.hasMissedNack()).isTrue();
                })
                .verifyComplete();

        verify(gossipTransport, times(3)).ping(any());
    }

    @Test
    void manyPeersFailedDirectCheckMissedNack() {
        // lifeguard - LHA-Probe
        List<Integer> proxyNodeIds = Arrays.asList(7002,7003);
        PeerProbe peerProbe = new PeerProbe(DESTINATION_NODE_ID, proxyNodeIds);

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(NODE_ID)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(true)
                .setCounter(1)
                .build())
        ).willReturn(Flux.error(new TimeoutException()));

        given(gossipTransport.ping(Ping.newBuilder()
                        .setInitiatorNodeId(NODE_ID)
                        .setRequestorNodeId(7002)
                        .setDestinationNodeId(DESTINATION_NODE_ID)
                        .addAllGossips(gossips)
                        .setDirect(false)
                        .setCounter(0)
                        .build())
        // missed nack
        ).willReturn(Flux.just(Ack.newBuilder().setNodeId(DESTINATION_NODE_ID).build()).delayElements(Duration.ofDays(1)));

        given(gossipTransport.ping(Ping.newBuilder()
                .setInitiatorNodeId(NODE_ID)
                .setRequestorNodeId(7003)
                .setDestinationNodeId(DESTINATION_NODE_ID)
                .addAllGossips(gossips)
                .setDirect(false)
                .setCounter(0)
                .build())
        ).willReturn(Flux.just(Ack.newBuilder().setNodeId(7003).setNack(true).build(), Ack.newBuilder().setNodeId(7003).build())
                .delayElements(Duration.ofMillis(70))
        );

        StepVerifier.create(gossipProbe.probeNode(peerProbe, gossips, Duration.ofMillis(10), Duration.ofMillis(200)))
                .expectSubscription()
                .assertNext(probeResult -> {
                    assertThat(probeResult.getDestinationNodeId()).isEqualTo(DESTINATION_NODE_ID);
                    assertThat(probeResult.hasAck()).isTrue();
                    assertThat(probeResult.getAcks()).containsExactly(
                            Ack.newBuilder().setNodeId(7003).setNack(true).build(),
                            Ack.newBuilder().setNodeId(7003).build()
                    );
                    assertThat(probeResult.getDistinctAcks()).containsExactly(
                            Ack.newBuilder().setNodeId(7003).build()
                    );
                    assertThat(probeResult.getSubgroupSize()).isEqualTo(2);
                    assertThat(probeResult.hasMissedNack()).isTrue();
                })
                .verifyComplete();

        verify(gossipTransport, times(3)).ping(any());
    }
}