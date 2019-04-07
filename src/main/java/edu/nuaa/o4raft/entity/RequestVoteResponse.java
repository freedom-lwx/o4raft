package edu.nuaa.o4raft.entity;

import java.io.Serializable;

public class RequestVoteResponse implements Serializable {

    private RequestVoteResponse [] responses;

    public RequestVoteResponse[] getResponses() {
        RequestVoteResponse [] r=responses;
        responses=null;
        return r;
    }

    public void setResponses(RequestVoteResponse[] responses) {
        this.responses = responses;
    }

    @Override
    public String toString() {
        if (responses!=null){
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("RequestVoteResponse[term,voteGranted]: ");
            for (RequestVoteResponse respons : responses) {
                stringBuilder.append(respons);
            }
            return stringBuilder.toString();
        }
        return "("+term +
                "," + voteGranted +
                ')';
    }

    private int term;
    private boolean voteGranted;

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public boolean isVoteGranted() {
        return voteGranted;
    }

    public void setVoteGranted(boolean voteGranted) {
        this.voteGranted = voteGranted;
    }
}
