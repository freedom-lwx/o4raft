package edu.nuaa.o4raft.interfacee.impl;

import edu.nuaa.o4raft.entity.AppendEntriesResponse;
import edu.nuaa.o4raft.entity.Entry;
import edu.nuaa.o4raft.entity.RequestVoteResponse;
import edu.nuaa.o4raft.interfacee.RaftPoint;



public class EchoPointService implements RaftPoint {

    public RequestVoteResponse requestVote(int term, int condidateId, int lastLogIndex, int lastLogTerm) {
        RequestVoteResponse response=new RequestVoteResponse();
        response.setTerm(term);
        response.setVoteGranted(true);
        return response;
    }

    public AppendEntriesResponse appendEntries(int term, int leaderId, int prevLogIndex, int prevLogTerm, Entry[] entries, int leaderCommit) {
        AppendEntriesResponse response=new AppendEntriesResponse();
        response.setSuccess(true);
        response.setTerm(term);
        return response;
    }
}
