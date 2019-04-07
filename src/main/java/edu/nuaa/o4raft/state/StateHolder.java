package edu.nuaa.o4raft.state;

import edu.nuaa.o4raft.entity.AppendEntriesResponse;
import edu.nuaa.o4raft.entity.Entry;
import edu.nuaa.o4raft.entity.RequestVoteResponse;

import javax.swing.plaf.nimbus.State;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;

public class StateHolder {


    public static AppendEntriesResponse FLAG_VOTE_REQUEST_COMMING=new AppendEntriesResponse();
    public static AppendEntriesResponse FLAG_ENTRIES_COMMING=new AppendEntriesResponse();
    public static RequestVoteResponse FLAG_NEW_ENTRY_COMMING=new RequestVoteResponse();




    public static BlockingQueue<AppendEntriesResponse> appendBlockingQueue=new LinkedBlockingDeque<AppendEntriesResponse>();
    public static BlockingQueue<RequestVoteResponse> voteBlockingQueue=new LinkedBlockingDeque<RequestVoteResponse>();



    public static Semaphore stateMachineInit =new Semaphore(0);

    public static boolean inCondidate=false;
    // logs



    // 持久状态
    public static int currentTerm=-1;
    public static int voteFor=-1;
    public static LinkedList<Entry> logs=new LinkedList<Entry>();


    //临时状态
    public static int commitIndex=-1;
    public static int lastApplied=-1;


    //leader的额外临时状态 不需要考虑线程安全

    public static int nextIndex[]=null;
    public static int matchIndex[]=null;
}
