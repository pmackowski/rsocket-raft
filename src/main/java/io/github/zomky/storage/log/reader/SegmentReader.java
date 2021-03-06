package io.github.zomky.storage.log.reader;

import io.github.zomky.storage.log.entry.IndexedLogEntry;

import java.util.Iterator;

public interface SegmentReader extends Iterator<IndexedLogEntry>, AutoCloseable {

    @Override
    void close();

    void reset(long index);

    long getCurrentIndex();
}
