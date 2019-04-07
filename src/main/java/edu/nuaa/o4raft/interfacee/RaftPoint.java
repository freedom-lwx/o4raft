package edu.nuaa.o4raft.interfacee;

import edu.nuaa.o4raft.entity.AppendEntriesResponse;
import edu.nuaa.o4raft.entity.Entry;
import edu.nuaa.o4raft.entity.RequestVoteResponse;

public interface RaftPoint {
    public RequestVoteResponse requestVote(int term,int condidateId,int lastLogIndex,int lastLogTerm);
    public AppendEntriesResponse appendEntries(
            int term, int leaderId, int prevLogIndex, int prevLogTerm, Entry[] entries,int leaderCommit);
}
