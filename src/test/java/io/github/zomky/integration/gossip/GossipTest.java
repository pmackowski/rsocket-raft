package io.github.zomky.integration.gossip;

import io.github.zomky.Node;
import io.github.zomky.NodeFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

class GossipTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GossipTest.class);

    @Test
    @Disabled
    void names() throws InterruptedException {
//        Hooks.onOperatorDebug();

        float lambdaGossipSharedMultiplier = 1f;

        NodeFactory.receive()
                .port(7000)
                .retryJoin(7001)
                .baseProbeTimeout(Duration.ofSeconds(1))
                .lambdaGossipSharedMultiplier(lambdaGossipSharedMultiplier)
                .start()
                .subscribeOn(Schedulers.elastic())
                .subscribe();

        Thread.sleep(2000);

        Node node2 = NodeFactory.receive()
                .port(7001)
                .retryJoin(7000)
                .baseProbeTimeout(Duration.ofSeconds(200))
                .lambdaGossipSharedMultiplier(lambdaGossipSharedMultiplier)
                .start()
                .subscribeOn(Schedulers.elastic())
                .block();

        NodeFactory.receive()
                .port(7002)
                .retryJoin(7000)
                .baseProbeTimeout(Duration.ofSeconds(1))
                .lambdaGossipSharedMultiplier(lambdaGossipSharedMultiplier)
                .start()
                .subscribeOn(Schedulers.elastic())
                .block();

        NodeFactory.receive()
                .port(7003)
                .retryJoin(7002)
                .baseProbeTimeout(Duration.ofSeconds(1))
                .lambdaGossipSharedMultiplier(lambdaGossipSharedMultiplier)
                .start()
                .subscribeOn(Schedulers.elastic())
                .block();

        NodeFactory.receive()
                .port(7004)
                .retryJoin(7002)
                .baseProbeTimeout(Duration.ofSeconds(1))
                .lambdaGossipSharedMultiplier(lambdaGossipSharedMultiplier)
                .start()
                .subscribeOn(Schedulers.elastic())
                .block();

        Thread.sleep(10000);
        node2.dispose();

        Thread.sleep(60000);
    }

}
