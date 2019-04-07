package edu.nuaa.o4raft.common;

import edu.nuaa.o4raft.entity.RequestVoteResponse;
import org.apache.dubbo.rpc.cluster.Merger;

public class VoteMerger implements Merger<RequestVoteResponse> {


    public RequestVoteResponse merge(RequestVoteResponse... requestVoteResponses) {
        System.out.println("VoteMerger"+requestVoteResponses.length);
        RequestVoteResponse r=new RequestVoteResponse();
        RequestVoteResponse []rs=new RequestVoteResponse[requestVoteResponses.length];
        for (int i = 0; i < requestVoteResponses.length; i++) {
            rs[i]=requestVoteResponses[i];
        }
        r.setResponses(rs);
        return r;
    }
}
