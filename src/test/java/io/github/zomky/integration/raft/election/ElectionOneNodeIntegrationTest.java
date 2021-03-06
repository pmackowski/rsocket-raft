package io.github.zomky.integration.raft.election;

import io.github.zomky.IntegrationTest;
import io.github.zomky.integration.Nodes;
import io.github.zomky.external.statemachine.KVStateMachine1;
import io.github.zomky.external.statemachine.KVStateMachineEntryConverter;
import io.github.zomky.integration.IntegrationTestsUtils;
import io.github.zomky.raft.ElectionTimeout;
import io.github.zomky.raft.RaftConfiguration;
import io.github.zomky.raft.RaftGroup;
import io.github.zomky.storage.RaftStorage;
import io.github.zomky.storage.meta.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@ExtendWith(MockitoExtension.class)
@IntegrationTest
class ElectionOneNodeIntegrationTest {

    @TempDir
    Path directory;

    RaftStorage raftStorage;

    Nodes nodes;

    @BeforeEach
    void setUp() {
        IntegrationTestsUtils.checkBlockingCalls();
        raftStorage = IntegrationTestsUtils.raftStorage(directory);
        nodes = Nodes.create(7000);
    }

    @AfterEach
    void tearDown() {
        raftStorage.close();
        nodes.dispose();
    }

    @Test
    void electionPreVoteDisabled() {
        // when
        nodes.addRaftGroup("group1", raftStorage, raftConfiguration(false));

        // then
        RaftGroup actual = nodes.raftGroup(7000, "group1");

        await().atMost(1, TimeUnit.SECONDS).until(() -> actual.getCurrentLeaderId() == 7000);

        assertThat(actual.isLeader()).isTrue();
        assertThat(raftStorage.getTerm()).isEqualTo(1);
        assertThat(raftStorage.getVotedFor()).isEqualTo(7000);
    }

    @Test
    void electionPreVoteEnabled() {
        // when
        nodes.addRaftGroup("group1", raftStorage, raftConfiguration(true));

        // then
        RaftGroup actual = nodes.raftGroup(7000, "group1");

        await().atMost(1, TimeUnit.SECONDS).until(() -> actual.getCurrentLeaderId() == 7000);

        assertThat(actual.isLeader()).isTrue();
        assertThat(raftStorage.getTerm()).isEqualTo(1);
        assertThat(raftStorage.getVotedFor()).isEqualTo(7000);
    }

    private RaftConfiguration raftConfiguration(boolean preVote) {
        return RaftConfiguration.builder()
                .stateMachine(new KVStateMachine1(7000))
                .stateMachineEntryConverter(new KVStateMachineEntryConverter())
                .electionTimeout(ElectionTimeout.defaultTimeout())
                .configuration(new Configuration(7000))
                .preVote(preVote)
                .build();
    }

}
