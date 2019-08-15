package io.github.pmackowski.rsocket.raft.gossip;

public class GossipException extends RuntimeException {

    public GossipException(String message) {
        super(message);
    }

    public GossipException(Throwable cause) {
        super(cause);
    }

    public GossipException(String message, Throwable cause) {
        super(message, cause);
    }
}
