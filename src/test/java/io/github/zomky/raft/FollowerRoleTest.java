package io.github.zomky.raft;

import com.google.protobuf.ByteString;
import io.github.zomky.Cluster;
import io.github.zomky.storage.InMemoryRaftStorage;
import io.github.zomky.storage.RaftStorage;
import io.github.zomky.storage.log.entry.CommandEntry;
import io.github.zomky.storage.log.entry.LogEntry;
import io.github.zomky.transport.Sender;
import io.github.zomky.transport.protobuf.AppendEntriesRequest;
import io.github.zomky.transport.protobuf.PreVoteRequest;
import io.github.zomky.transport.protobuf.PreVoteResponse;
import io.github.zomky.transport.protobuf.VoteRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static io.github.zomky.storage.log.serializer.LogEntrySerializer.serialize;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowerRoleTest {
    FollowerRole followerRole = new FollowerRole();

    @Mock
    Cluster cluster;

    @Mock
    RaftGroup raftGroup;

    @Mock
    Sender sender1, sender2;

    RaftStorage raftStorage = new InMemoryRaftStorage();

    //// ELECTION TIMEOUT ////

    @Test
    void electionTimeout() {
        Duration electionTimeout = Duration.ofMillis(50);
        given(raftGroup.nextElectionTimeout()).willReturn(electionTimeout);
        given(raftGroup.isPreVote()).willReturn(false);
        followerRole.onInit(cluster, raftGroup, raftStorage);

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> verify(raftGroup).convertToCandidate());
    }

    @Test
    void electionTimeoutWithPreVoteEnabled() {
        Duration electionTimeout = Duration.ofMillis(50);
        given(raftGroup.nextElectionTimeout()).willReturn(electionTimeout);
        given(raftGroup.isPreVote()).willReturn(true);
        given(raftGroup.availableSenders()).willReturn(Flux.just(sender1, sender2));
        given(raftGroup.quorum()).willReturn(2);

        PreVoteResponse preVoteResponse = PreVoteResponse
                .newBuilder()
                .setTerm(0)
                .setVoteGranted(true)
                .build();
        given(sender1.requestPreVote(eq(raftGroup), any(PreVoteRequest.class)))
                .willReturn(Mono.just(preVoteResponse).delayElement(Duration.ofMillis(20)));
        given(sender2.requestPreVote(eq(raftGroup), any(PreVoteRequest.class)))
                .willReturn(Mono.just(preVoteResponse));

        followerRole.onInit(cluster, raftGroup, raftStorage);

        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> verify(raftGroup).convertToCandidate());
    }

    @Test
    void electionTimeoutWithPreVoteEnabledAndVoteNotGranted() throws InterruptedException {
        Duration electionTimeout = Duration.ofMillis(10);
        given(raftGroup.nextElectionTimeout()).willReturn(electionTimeout);
        given(raftGroup.isPreVote()).willReturn(true);
        given(raftGroup.availableSenders()).willReturn(Flux.just(sender1, sender2));
        given(raftGroup.quorum()).willReturn(2);

        PreVoteResponse preVoteResponse = PreVoteResponse
                .newBuilder()
                .setTerm(0)
                .setVoteGranted(false)
                .build();
        given(sender1.requestPreVote(eq(raftGroup), any(PreVoteRequest.class)))
                .willReturn(Mono.just(preVoteResponse).delayElement(Duration.ofMillis(20)));
        given(sender2.requestPreVote(eq(raftGroup), any(PreVoteRequest.class)))
                .willReturn(Mono.just(preVoteResponse));

        followerRole.onInit(cluster, raftGroup, raftStorage);

        int lag = 50;
        Thread.sleep(electionTimeout.toMillis() + lag);
        verify(raftGroup).refreshFollower();
    }

    @Test
    void noElectionTimeout() throws InterruptedException {
        Duration electionTimeout = Duration.ofMillis(50);
        given(raftGroup.nextElectionTimeout()).willReturn(electionTimeout);
        followerRole.onInit(cluster, raftGroup, raftStorage);

        long lessThanElectionTimeout = electionTimeout.toMillis() - 20;
        Thread.sleep(lessThanElectionTimeout);
        verify(raftGroup, never()).convertToCandidate();
    }

    @Test
    void onExitCallResetElectionTimeout() throws InterruptedException {
        given(raftGroup.nextElectionTimeout()).willReturn(Duration.ofMillis(50));
        followerRole.onInit(cluster, raftGroup, raftStorage);
        followerRole.onExit(cluster, raftGroup, raftStorage);

        Thread.sleep(100);
        verify(raftGroup, never()).convertToCandidate();
    }

    @Test
    void voteGrantedResetElectionTimeout() throws InterruptedException {
        given(raftGroup.nextElectionTimeout()).willReturn(Duration.ofMillis(100));

        VoteRequest voteRequest = VoteRequest.newBuilder()
                .setCandidateId(1)
                .setLastLogIndex(0)
                .setLastLogTerm(0)
                .setTerm(1)
                .build();

        followerRole.onInit(cluster, raftGroup, raftStorage);

        followerRole.onRequestVote(cluster, raftGroup, raftStorage, voteRequest)
                .delaySubscription(Duration.ofMillis(50))
                .subscribe();

        Thread.sleep(150);
        verify(raftGroup, never()).convertToCandidate();
    }

    @Test
    void validAppendEntriesResetElectionTimeout() throws InterruptedException {
        given(raftGroup.nextElectionTimeout()).willReturn(Duration.ofMillis(100));

        AppendEntriesRequest appendEntriesRequest = AppendEntriesRequest.newBuilder()
                .setPrevLogIndex(0)
                .setPrevLogTerm(0)
                .setTerm(1)
                .setLeaderCommit(1)
                .build();

        followerRole.onInit(cluster, raftGroup, raftStorage);

        followerRole.onAppendEntries(cluster, raftGroup, raftStorage, appendEntriesRequest)
                .delaySubscription(Duration.ofMillis(50))
                .subscribe();

        Thread.sleep(150);
        verify(raftGroup, never()).convertToCandidate();
        verify(raftGroup).appendEntriesCall();
    }

    //// REQUEST VOTE ////

    @Test
    void requestVoteBothCandidateAndFollowerHaveEmptyLog() {
        initFollower();

        VoteRequest voteRequest = VoteRequest.newBuilder()
                .setCandidateId(1)
                .setLastLogIndex(0)
                .setLastLogTerm(0)
                .setTerm(1)
                .build();

        StepVerifier.create(followerRole.onRequestVote(cluster, raftGroup, raftStorage, voteRequest))
                .assertNext(voteResponse -> {
                    assertThat(voteResponse.getTerm()).isEqualTo(0);
                    assertThat(voteResponse.getVoteGranted()).isEqualTo(true);
                }).verifyComplete();
    }

    @Test
    void requestVoteCandidateHasNotEmptyLog() {
        initFollower();

        VoteRequest voteRequest = VoteRequest.newBuilder()
                .setCandidateId(100)
                .setLastLogIndex(1)
                .setLastLogTerm(1)
                .setTerm(2)
                .build();

        StepVerifier.create(followerRole.onRequestVote(cluster, raftGroup, raftStorage, voteRequest))
                .assertNext(voteResponse -> {
                    assertThat(voteResponse.getTerm()).isEqualTo(0);
                    assertThat(voteResponse.getVoteGranted()).isEqualTo(true);
                    assertThat(raftStorage.getTerm()).isEqualTo(2);
                    assertThat(raftStorage.getVotedFor()).isEqualTo(100);
                    verify(raftGroup, times(2)).convertToFollower(2); // TODO should it be 1 invocation?

                }).verifyComplete();
    }

    @Test
    void requestVoteCandidateLogIsNotUpToDate() {
        initFollower();
        raftStorage.update(1, 0);
        raftStorage.append(commandEntry(1,  "val1"));

        VoteRequest voteRequest = VoteRequest.newBuilder()
                .setCandidateId(100)
                .setLastLogIndex(0)
                .setLastLogTerm(0)
                .setTerm(2)
                .build();

        StepVerifier.create(followerRole.onRequestVote(cluster, raftGroup, raftStorage, voteRequest))
                .assertNext(voteResponse -> {
                    assertThat(voteResponse.getTerm()).isEqualTo(1);
                    assertThat(voteResponse.getVoteGranted()).isEqualTo(false);
                    assertThat(raftStorage.getTerm()).isEqualTo(1);
                    assertThat(raftStorage.getVotedFor()).isEqualTo(0);
                    verify(raftGroup).convertToFollower(2);

                }).verifyComplete();
    }

    @Test
    void requestVoteCandidateLogIsNotUpToDateAndBothHaveSameTerm() {
        initFollower();
        raftStorage.update(2, 0);
        raftStorage.append(commandEntry(1,  "val1"));
        raftStorage.append(commandEntry(2,  "val2"));

        VoteRequest voteRequest = VoteRequest.newBuilder()
                .setCandidateId(100)
                .setLastLogIndex(1)
                .setLastLogTerm(2)
                .setTerm(2)
                .build();

        StepVerifier.create(followerRole.onRequestVote(cluster, raftGroup, raftStorage, voteRequest))
                .assertNext(voteResponse -> {
                    assertThat(voteResponse.getTerm()).isEqualTo(2);
                    assertThat(voteResponse.getVoteGranted()).isEqualTo(false);
                    assertThat(raftStorage.getTerm()).isEqualTo(2);
                    assertThat(raftStorage.getVotedFor()).isEqualTo(0);
                    verify(raftGroup, never()).convertToFollower(anyInt());

                }).verifyComplete();
    }

    @Test
    void requestVoteCandidateLogIsUpToDateAndBothHaveSameTerm() {
        initFollower();
        raftStorage.update(2, 0);
        raftStorage.append(commandEntry(1,  "val1"));
        raftStorage.append(commandEntry(2,  "val2"));

        VoteRequest voteRequest = VoteRequest.newBuilder()
                .setCandidateId(100)
                .setLastLogIndex(2)
                .setLastLogTerm(2)
                .setTerm(2)
                .build();

        StepVerifier.create(followerRole.onRequestVote(cluster, raftGroup, raftStorage, voteRequest))
                .assertNext(voteResponse -> {
                    assertThat(voteResponse.getTerm()).isEqualTo(2);
                    assertThat(voteResponse.getVoteGranted()).isEqualTo(true);
                    assertThat(raftStorage.getTerm()).isEqualTo(2);
                    assertThat(raftStorage.getVotedFor()).isEqualTo(100);
                    verify(raftGroup).convertToFollower(2);

                }).verifyComplete();
    }

    @Test
    void requestVoteCandidateLogIsUpToDateAndFollowerAlreadyVoted() {
        initFollower();
        raftStorage.update(2, 200);
        raftStorage.append(commandEntry(1,  "val1"));
        raftStorage.append(commandEntry(2,  "val2"));

        VoteRequest voteRequest = VoteRequest.newBuilder()
                .setCandidateId(100)
                .setLastLogIndex(2)
                .setLastLogTerm(2)
                .setTerm(2)
                .build();

        StepVerifier.create(followerRole.onRequestVote(cluster, raftGroup, raftStorage, voteRequest))
                .assertNext(voteResponse -> {
                    assertThat(voteResponse.getTerm()).isEqualTo(2);
                    assertThat(voteResponse.getVoteGranted()).isEqualTo(false);
                    assertThat(raftStorage.getTerm()).isEqualTo(2);
                    assertThat(raftStorage.getVotedFor()).isEqualTo(200);
                    verify(raftGroup, never()).convertToFollower(anyInt());
                }).verifyComplete();
    }

    @Test
    void requestVoteBothCandidateAndFollowerHaveEmptyLogAndFollowerAlreadyVoted() {
        initFollower();
        raftStorage.update(1, 200);

        VoteRequest voteRequest = VoteRequest.newBuilder()
                .setCandidateId(100)
                .setLastLogIndex(0)
                .setLastLogTerm(0)
                .setTerm(1)
                .build();

        StepVerifier.create(followerRole.onRequestVote(cluster, raftGroup, raftStorage, voteRequest))
                .assertNext(voteResponse -> {
                    assertThat(voteResponse.getTerm()).isEqualTo(1);
                    assertThat(voteResponse.getVoteGranted()).isEqualTo(false);
                }).verifyComplete();
    }

    @Test
    void requestVoteCandidateTermIsGreaterAndFollowerAlreadyVoted() {
        initFollower();
        raftStorage.update(1, 200);

        VoteRequest voteRequest = VoteRequest.newBuilder()
                .setCandidateId(100)
                .setLastLogIndex(0)
                .setLastLogTerm(0)
                .setTerm(2)
                .build();

        StepVerifier.create(followerRole.onRequestVote(cluster, raftGroup, raftStorage, voteRequest))
                .assertNext(voteResponse -> {
                    assertThat(voteResponse.getTerm()).isEqualTo(1);
                    assertThat(voteResponse.getVoteGranted()).isEqualTo(true);
                    assertThat(raftStorage.getTerm()).isEqualTo(2);
                    assertThat(raftStorage.getVotedFor()).isEqualTo(100);
                    verify(raftGroup, times(2)).convertToFollower(anyInt());
                }).verifyComplete();
    }

    //// REQUEST PRE-VOTE ////

    @Test
    void requestPreVoteLeaderStickiness() {
        initFollower();
        given(raftGroup.isLeaderStickiness()).willReturn(true);
        given(raftGroup.lastAppendEntriesWithinElectionTimeout()).willReturn(true);
        raftStorage.update(1, 0);

        PreVoteRequest preVoteRequest = PreVoteRequest.newBuilder()
                .setCandidateId(100)
                .setLastLogIndex(0)
                .setLastLogTerm(0)
                .setNextTerm(2)
                .build();

        StepVerifier.create(followerRole.onPreRequestVote(cluster, raftGroup, raftStorage, preVoteRequest))
                .assertNext(preVoteResponse -> {
                    assertThat(preVoteResponse.getTerm()).isEqualTo(1);
                    assertThat(preVoteResponse.getVoteGranted()).isEqualTo(false);
                    assertThat(raftStorage.getTerm()).isEqualTo(1);
                    assertThat(raftStorage.getVotedFor()).isEqualTo(0);
                    verify(raftGroup, never()).convertToFollower(anyInt());
                }).verifyComplete();
    }

    @Test
    void requestPreVoteLeaderStickinessButNoAppendEntriesCall() {
        initFollower();
        given(raftGroup.isLeaderStickiness()).willReturn(true);
        given(raftGroup.lastAppendEntriesWithinElectionTimeout()).willReturn(false);
        raftStorage.update(1, 0);

        PreVoteRequest preVoteRequest = PreVoteRequest.newBuilder()
                .setCandidateId(100)
                .setLastLogIndex(0)
                .setLastLogTerm(0)
                .setNextTerm(2)
                .build();

        StepVerifier.create(followerRole.onPreRequestVote(cluster, raftGroup, raftStorage, preVoteRequest))
                .assertNext(preVoteResponse -> {
                    assertThat(preVoteResponse.getTerm()).isEqualTo(1);
                    assertThat(preVoteResponse.getVoteGranted()).isEqualTo(true);
                    assertThat(raftStorage.getTerm()).isEqualTo(1);
                    assertThat(raftStorage.getVotedFor()).isEqualTo(0);
                    verify(raftGroup, never()).convertToFollower(anyInt());
                }).verifyComplete();
    }

    @Test
    void requestPreVoteNoLeaderStickinessCandidateNextTermSmallerThanFollowerCurrentTerm() {
        initFollower();
        given(raftGroup.isLeaderStickiness()).willReturn(false);
        raftStorage.update(3, 0);

        PreVoteRequest preVoteRequest = PreVoteRequest.newBuilder()
                .setCandidateId(100)
                .setLastLogIndex(0)
                .setLastLogTerm(0)
                .setNextTerm(2)
                .build();

        StepVerifier.create(followerRole.onPreRequestVote(cluster, raftGroup, raftStorage, preVoteRequest))
                .assertNext(preVoteResponse -> {
                    assertThat(preVoteResponse.getTerm()).isEqualTo(3);
                    assertThat(preVoteResponse.getVoteGranted()).isEqualTo(false);
                    assertThat(raftStorage.getTerm()).isEqualTo(3);
                    assertThat(raftStorage.getVotedFor()).isEqualTo(0);
                    verify(raftGroup, never()).convertToFollower(anyInt());
                }).verifyComplete();
    }

    @Test
    void requestPreVoteNoLeaderStickinessCandidateLogIsUpToDateAndBothHaveSameTerm() {
        initFollower();
        raftStorage.update(2, 0);
        raftStorage.append(commandEntry(1,  "val1"));
        raftStorage.append(commandEntry(2,  "val2"));

        PreVoteRequest preVoteRequest = PreVoteRequest.newBuilder()
                .setCandidateId(100)
                .setLastLogIndex(2)
                .setLastLogTerm(2)
                .setNextTerm(3)
                .build();

        StepVerifier.create(followerRole.onPreRequestVote(cluster, raftGroup, raftStorage, preVoteRequest))
                .assertNext(preVoteResponse -> {
                    assertThat(preVoteResponse.getTerm()).isEqualTo(2);
                    assertThat(preVoteResponse.getVoteGranted()).isEqualTo(true);
                    assertThat(raftStorage.getTerm()).isEqualTo(2);
                    assertThat(raftStorage.getVotedFor()).isEqualTo(0);
                    verify(raftGroup, never()).convertToFollower(anyInt());
                }).verifyComplete();
    }

    //// APPEND ENTRIES ////

    @Test
    void appendEntriesEmptyLog() {
        initFollower();
        raftStorage.update(0, 0);

        AppendEntriesRequest appendEntriesRequest = AppendEntriesRequest.newBuilder()
                .setPrevLogIndex(0)
                .setPrevLogTerm(0)
                .setTerm(1)
                .setLeaderCommit(1)
                .addEntries(entry(1, "val1"))
                .build();

        StepVerifier.create(followerRole.onAppendEntries(cluster, raftGroup, raftStorage, appendEntriesRequest))
                .assertNext(appendEntriesResponse -> {
                    assertThat(appendEntriesResponse.getTerm()).isEqualTo(0);
                    assertThat(appendEntriesResponse.getSuccess()).isEqualTo(true);
                    assertThat(raftStorage.getLastIndexedTerm().getIndex()).isEqualTo(1);
                    assertThat(((CommandEntry) raftStorage.getLastEntry().get().getLogEntry()).getValue()).isEqualTo("val1".getBytes());
                    verify(raftGroup).appendEntriesCall();
                    verify(raftGroup).setCommitIndex(1);
                    verify(raftGroup).convertToFollower(1);
                }).verifyComplete();
    }

    @Test
    void appendEntriesLogContainsEntries() {
        initFollower();
        raftStorage.update(1, 0);
        raftStorage.append(commandEntry(1,  "val1"));
        raftStorage.append(commandEntry(1,  "val2"));

        AppendEntriesRequest appendEntriesRequest = AppendEntriesRequest.newBuilder()
                .setPrevLogIndex(2)
                .setPrevLogTerm(1)
                .setTerm(2)
                .setLeaderCommit(3)
                .addEntries(entry(2, "val3"))
                .build();

        StepVerifier.create(followerRole.onAppendEntries(cluster, raftGroup, raftStorage, appendEntriesRequest))
                .assertNext(appendEntriesResponse -> {
                    assertThat(appendEntriesResponse.getTerm()).isEqualTo(1);
                    assertThat(appendEntriesResponse.getSuccess()).isEqualTo(true);
                    assertThat(raftStorage.getLastIndexedTerm().getIndex()).isEqualTo(3);
                    assertThat(((CommandEntry) raftStorage.getLastEntry().get().getLogEntry()).getValue()).isEqualTo("val3".getBytes());
                    verify(raftGroup).appendEntriesCall();
                    verify(raftGroup).setCommitIndex(3);
                    verify(raftGroup).convertToFollower(2);
                }).verifyComplete();
    }

    @Test
    void appendEntriesCommitIndexNoGreaterThanLogSize() {
        initFollower();
        raftStorage.update(1, 0);
        raftStorage.append(commandEntry(1,  "val1"));

        AppendEntriesRequest appendEntriesRequest = AppendEntriesRequest.newBuilder()
                .setPrevLogIndex(1)
                .setPrevLogTerm(1)
                .setTerm(1)
                .setLeaderCommit(20)
                .addEntries(entry(1, "val2")) // leader sends only one entry despite it could send more entries
                .build();

        StepVerifier.create(followerRole.onAppendEntries(cluster, raftGroup, raftStorage, appendEntriesRequest))
                .assertNext(appendEntriesResponse -> {
                    assertThat(appendEntriesResponse.getTerm()).isEqualTo(1);
                    assertThat(appendEntriesResponse.getSuccess()).isEqualTo(true);
                    verify(raftGroup).appendEntriesCall();
                    assertThat(raftStorage.getLastIndexedTerm().getIndex()).isEqualTo(2);
                    assertThat(((CommandEntry) raftStorage.getLastEntry().get().getLogEntry()).getValue()).isEqualTo("val2".getBytes());
                    verify(raftGroup).setCommitIndex(2); // not 20
                    verify(raftGroup, never()).convertToFollower(anyInt());
                }).verifyComplete();
    }

    @Test
    void appendEntriesLogContainsEntriesThaShouldBeTruncated() {
        initFollower();
        raftStorage.update(1, 0);
        raftStorage.append(commandEntry(1,  "val1"));
        raftStorage.append(commandEntry(2,  "val2"));
        raftStorage.append(commandEntry(3,  "val3"));

        AppendEntriesRequest appendEntriesRequest = AppendEntriesRequest.newBuilder()
                .setPrevLogIndex(1)
                .setPrevLogTerm(1)
                .setTerm(1)
                .setLeaderCommit(2)
                .addEntries(entry(1, "val2"))
                .build();

        StepVerifier.create(followerRole.onAppendEntries(cluster, raftGroup, raftStorage, appendEntriesRequest))
                .assertNext(appendEntriesResponse -> {
                    assertThat(appendEntriesResponse.getTerm()).isEqualTo(1);
                    assertThat(appendEntriesResponse.getSuccess()).isEqualTo(true);
                    verify(raftGroup).appendEntriesCall();
                    assertThat(raftStorage.getLastIndexedTerm().getIndex()).isEqualTo(2);
                    assertThat(((CommandEntry) raftStorage.getLastEntry().get().getLogEntry()).getValue()).isEqualTo("val2".getBytes());
                    verify(raftGroup).setCommitIndex(2);
                    verify(raftGroup, never()).convertToFollower(anyInt());
                }).verifyComplete();
    }

    @Test
    void appendEntriesOutdatedRPCReceived() {
        initFollower();
        raftStorage.update(1, 0);
        raftStorage.append(commandEntry(1,  "val1"));
        raftStorage.append(commandEntry(1,  "val2"));
        raftStorage.append(commandEntry(1,  "val3"));

        AppendEntriesRequest appendEntriesRequest = AppendEntriesRequest.newBuilder()
                .setPrevLogIndex(1)
                .setPrevLogTerm(1)
                .setTerm(1)
                .setLeaderCommit(2)
                .addEntries(entry(1, "val2"))
                .build();

        StepVerifier.create(followerRole.onAppendEntries(cluster, raftGroup, raftStorage, appendEntriesRequest))
                .assertNext(appendEntriesResponse -> {
                    assertThat(appendEntriesResponse.getTerm()).isEqualTo(1);
                    assertThat(appendEntriesResponse.getSuccess()).isEqualTo(true);
                    verify(raftGroup).appendEntriesCall();
                    assertThat(raftStorage.getLastIndexedTerm().getIndex()).isEqualTo(3);
                    assertThat(((CommandEntry) raftStorage.getLastEntry().get().getLogEntry()).getValue()).isEqualTo("val3".getBytes());
                    verify(raftGroup).setCommitIndex(2);
                    verify(raftGroup, never()).convertToFollower(anyInt());
                }).verifyComplete();
    }

    @Test
    void appendEntriesOutdatedHeartbeatReceived() {
        initFollower();
        raftStorage.update(1, 0);
        raftStorage.append(commandEntry(1,  "val1"));
        raftStorage.append(commandEntry(1,  "val2"));
        raftStorage.append(commandEntry(1,  "val3"));

        AppendEntriesRequest appendEntriesRequest = AppendEntriesRequest.newBuilder()
                .setPrevLogIndex(1)
                .setPrevLogTerm(1)
                .setTerm(1)
                .setLeaderCommit(100)
                .build();

        StepVerifier.create(followerRole.onAppendEntries(cluster, raftGroup, raftStorage, appendEntriesRequest))
                .assertNext(appendEntriesResponse -> {
                    assertThat(appendEntriesResponse.getTerm()).isEqualTo(1);
                    assertThat(appendEntriesResponse.getSuccess()).isEqualTo(true);
                    verify(raftGroup).appendEntriesCall();
                    assertThat(raftStorage.getLastIndexedTerm().getIndex()).isEqualTo(3);
                    assertThat(((CommandEntry) raftStorage.getLastEntry().get().getLogEntry()).getValue()).isEqualTo("val3".getBytes());
                    verify(raftGroup).setCommitIndex(3);
                    verify(raftGroup, never()).convertToFollower(anyInt());
                }).verifyComplete();
    }

    @Test
    void appendEntriesLogContainsEntryTermsNotMatchingAtPreviousIndex() {
        initFollower();
        raftStorage.update(1, 0);
        raftStorage.append(commandEntry(1,  "val1"));
        raftStorage.append(commandEntry(1,  "val2"));

        AppendEntriesRequest appendEntriesRequest = AppendEntriesRequest.newBuilder()
                .setPrevLogIndex(2)
                .setPrevLogTerm(2) // greater term
                .setTerm(2)
                .setLeaderCommit(3)
                .addEntries(entry(2, "val3"))
                .build();

        StepVerifier.create(followerRole.onAppendEntries(cluster, raftGroup, raftStorage, appendEntriesRequest))
                .assertNext(appendEntriesResponse -> {
                    assertThat(appendEntriesResponse.getTerm()).isEqualTo(1);
                    assertThat(appendEntriesResponse.getSuccess()).isEqualTo(false);
                    verify(raftGroup).appendEntriesCall();
                    assertThat(raftStorage.getLastIndexedTerm().getIndex()).isEqualTo(2);
                    assertThat(((CommandEntry) raftStorage.getLastEntry().get().getLogEntry()).getValue()).isEqualTo("val2".getBytes());
                    verify(raftGroup, never()).setCommitIndex(3);
                    verify(raftGroup).convertToFollower(2);
                }).verifyComplete();
    }

    private void initFollower() {
        given(raftGroup.nextElectionTimeout()).willReturn(Duration.ofSeconds(100));
        followerRole.onInit(cluster, raftGroup, raftStorage);
    }

    private CommandEntry commandEntry(int term, String value) {
        return new CommandEntry(term, System.currentTimeMillis(), value.getBytes());
    }

    private ByteString entry(int term, String value) {
        CommandEntry commandEntry = commandEntry(term, value);
        ByteBuffer byteBuffer = ByteBuffer.allocate(LogEntry.SIZE + value.length() + 1);
        serialize(commandEntry, byteBuffer);
        byteBuffer.flip();

        return ByteString.copyFrom(byteBuffer);
    }

}