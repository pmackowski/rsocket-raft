package io.github.zomky.storage;


import io.github.zomky.storage.log.SizeUnit;
import io.github.zomky.storage.log.entry.LogEntry;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 0, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
@Threads(2)
public class RaftStorageAppendExistingLogBenchmark {

    FileSystemRaftStorage raftStorage;

    Path directory;

    LogEntry logEntry;

    @Param({"32", "256", "1024", "1048576"})
    public int messageSize;

    private int segmentSize = 32; // 32MB

    @Setup
    public void setup() throws Exception {
        directory = RaftStorageBenchmarkUtils.createTempDirectory();
        raftStorage = new FileSystemRaftStorage(RaftStorageConfiguration.builder()
                .segmentSize(SizeUnit.megabytes, segmentSize)
                .directory(Paths.get(directory.toAbsolutePath().toString()))
                .build());
        logEntry = RaftStorageBenchmarkUtils.commandEntry(1, 1, messageSize);
    }

    @TearDown
    public void tearDown() throws IOException {
        raftStorage.close();
        RaftStorageBenchmarkUtils.delete(directory);
    }

    @Benchmark
    public void appendLog(Blackhole blackhole) {
        blackhole.consume(raftStorage.append(logEntry));
    }

}
