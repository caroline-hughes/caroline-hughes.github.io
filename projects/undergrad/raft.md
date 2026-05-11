# Raft KV Store

A distributed key-value store built on top of the [Raft consensus protocol](https://raft.github.io/). The system runs as a small replica cluster, elects a leader, replicates writes through the Raft log, and keeps `get` / `put` operations consistent even when replicas fail or messages are delayed.

What made this project interesting to me was that it pulled together several different distributed-systems concerns at once: leader election, heartbeats, log replication, quorum-based commits, and client redirection when a follower received a request.

## Highlights

-   Leader election with randomized timeouts and `RequestVote` RPCs
-   Log replication and heartbeat handling through `AppendEntries`
-   Majority-based commit logic for durable writes
-   Follower redirection when the client contacts the wrong node
-   Multi-process cluster behavior over UDP

[View on GitHub](https://github.com/caroline-hughes/raft#readme)
