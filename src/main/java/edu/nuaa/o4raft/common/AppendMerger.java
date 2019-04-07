package edu.nuaa.o4raft.common;

import edu.nuaa.o4raft.entity.AppendEntriesResponse;
import org.apache.dubbo.rpc.cluster.Merger;

public class AppendMerger implements Merger<AppendEntriesResponse> {
    public AppendEntriesResponse merge(AppendEntriesResponse... appendEntriesResponses) {
        System.out.println("AppendMerger"+appendEntriesResponses.length);
        AppendEntriesResponse r=new AppendEntriesResponse();
        AppendEntriesResponse[] rs=new AppendEntriesResponse[appendEntriesResponses.length];


        for (int i = 0; i < appendEntriesResponses.length; i++) {
            rs[i]=appendEntriesResponses[i];
        }
        r.setResponses(rs);
        return r;
    }
}
