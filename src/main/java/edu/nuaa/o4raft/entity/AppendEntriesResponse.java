package edu.nuaa.o4raft.entity;

import java.io.Serializable;

public class AppendEntriesResponse implements Serializable {
    private int term;
    private boolean success;
    private AppendEntriesResponse [] responses;

    public AppendEntriesResponse[] getResponses() {
        AppendEntriesResponse [] r=responses;
        responses=null;
        return r;
    }

    public void setResponses(AppendEntriesResponse[] responses) {
        this.responses = responses;
    }

    @Override
    public String toString() {
        if (responses!=null){
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("AppendEntriesResponse[term,success]: ");
            for (AppendEntriesResponse respons : responses) {
                stringBuilder.append(respons);
            }
            return stringBuilder.toString();
        }
        return "("+ term +","+success+")" ;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
