package edu.nuaa.o4raft.interfacee.impl;

import edu.nuaa.o4raft.entity.AppendEntriesResponse;
import edu.nuaa.o4raft.entity.Entry;
import edu.nuaa.o4raft.entity.RequestVoteResponse;
import edu.nuaa.o4raft.interfacee.RaftPoint;
import edu.nuaa.o4raft.state.StateHolder;
import edu.nuaa.o4raft.state.StateMachine;

import static edu.nuaa.o4raft.state.StateHolder.appendBlockingQueue;
import static edu.nuaa.o4raft.state.StateHolder.currentTerm;
import static edu.nuaa.o4raft.state.StateHolder.voteBlockingQueue;

public class RaftPointService implements RaftPoint {



    /**
     * 关于返回的报文
     *  初步就只判断,是否当前本地的term < 请求的term , 如果是就返回true不是就返回null
     *  lastLogIndex,lastLogTerm暂时不管
     *
     *
     * 关于本地的状态处理
     *
     *  作为一个follower收到了requestVote,则需要刷新自己的等待时间 -- > 在appendBlockingQueue插入这个response 告诉它
     *  作为一个condidate收到了requestVote请求,则让状态机忽律它，返回一个false
     *  初步让 leader 不会收到 requestVote
     * */

    public RequestVoteResponse requestVote(int term, int condidateId, int lastLogIndex, int lastLogTerm) {

        RequestVoteResponse response=new RequestVoteResponse();
        response.setTerm(term);

        if(!StateHolder.inCondidate && StateHolder.currentTerm < term){
            StateHolder.currentTerm=term;
            response.setVoteGranted(true);
            appendBlockingQueue.offer(StateHolder.FLAG_VOTE_REQUEST_COMMING);
        }else {
            response.setVoteGranted(false);
        }

        System.out.println(response);
        return response;
    }


    /**
     *
     * 关于返回的报文
     *  不管如何都直接返回
     *
     *
     * 关于本地状态的处理
     *  作为一个follower收到了一个appendEntries,则填入blockingQueue这次要返回的response,组织自己等待超时
     *  作为一个condidate 收到了一个appendEntries,则填入requestQueue,使其放弃该轮选举,并成为follower
     *
     * */
    public AppendEntriesResponse appendEntries(int term, int leaderId, int prevLogIndex, int prevLogTerm, Entry[] entries, int leaderCommit) {
        AppendEntriesResponse response=new AppendEntriesResponse();
        response.setSuccess(true);
        response.setTerm(term);


        if (currentTerm<term){
            currentTerm=term;
        }else if (currentTerm>term){
            response.setSuccess(false);
            response.setTerm(currentTerm);
        }
        appendBlockingQueue.offer(StateHolder.FLAG_ENTRIES_COMMING);
        System.out.println(response);
        return response;
    }

//    public RequestVoteResponse requestVote(int term, int condidateId, long lastLogIndex, int lastLogTerm) {
//        // 检测并回复是否要为调用该服务的老哥投票
//        return null;
//    }
//
//    public AppendEntriesResponse appendEntries(int term, int leaderId, long prevLogIndex, int prevLogTerm, Entry[] entries, long leaderCommit) {
//        // 检测并记录,更新心跳状态,同时如果该结点是新结点,则会从初始状态进入follwer状态
//        return null;
//    }
}
