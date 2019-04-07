package edu.nuaa.o4raft.state;


import edu.nuaa.o4raft.entity.AppendEntriesResponse;
import edu.nuaa.o4raft.entity.Entry;
import edu.nuaa.o4raft.entity.RequestVoteResponse;
import edu.nuaa.o4raft.interfacee.RaftPoint;

import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * 状态的转换全在这里做,但是报文的逻辑处理在外部做
 * */
public class StateMachine implements Runnable {
    public static final int INITIAL_STATE= -1 ;
    public static final int FOLLOWER_STATE= 1;
    public static final int CONDIDATE_STATE= 2;
    public static final int LEADER_STATE= 3;
    private int cuttentState=0;



    RaftPoint raftPoint;


    private static Random random=new Random(17);

    private int  waitTime(){
        return 300+random.nextInt(200);
    }

    public StateMachine(RaftPoint raftPoint){
        cuttentState=-1;
        this.raftPoint=raftPoint;
    }

    public static void start(RaftPoint raftPoint){
        new Thread(new StateMachine(raftPoint)).start();
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {

            switch (cuttentState){
                case INITIAL_STATE:
                    init();
                    break;
                case FOLLOWER_STATE:
                    workAsFollower();
                    break;
                case LEADER_STATE:
                    workAsLeader();
                    break;
                case CONDIDATE_STATE:
                    workAsCondidate();
                    break;
                default:
                        System.out.println("错误的状态");
            }

            /**
             * 状态机的状态还是依靠于本地的状态,状态单独使用一个类来维护好了
             * 状态机里只描述状态检查与转换的动作
             * */
//            try {
//                Thread.sleep(1000);//睡眠的时间应该是个可靠的时间
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }

    }
    /**
     * 原地阻塞一会
     * 如果能获得信号量,说明收到了一个确定的entry心跳
     *
     * 1 等待超时,进入选举态
     * 2 获得报文,进入跟随态
     *
     * */
    private void init() {
        AppendEntriesResponse res=null;
        try {
            res=StateHolder.appendBlockingQueue.poll(20000+waitTime(), TimeUnit.MILLISECONDS);//这里要等多久呢
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (res==null)
            cuttentState = CONDIDATE_STATE;
        else
            cuttentState = FOLLOWER_STATE;
    }


    /**
     * 发出vote请求,并检测获取到的同意选举报文是否达到法定
     * 0. trade off,所有结点不管投没投票都会返回投票报文
     *  阻塞在一个大小为1的blockingQueue上
     * 1. 如果成功,则转换为领导态
     * 2. trade off 如果失败，则变为Follower来等待重新开始vote,好处是不需要额外处理了(失败的 vote response)
     * 3. 【多余】貌似不会存在超时状态,每次发起投票的话要么成功要么失败
     *
     *
     * */
    private void workAsCondidate() {
        int term=++StateHolder.currentTerm ;
        StateHolder.inCondidate=true;
        RequestVoteResponse res=null;
        res=raftPoint.requestVote(term,Config.ID,StateHolder.lastApplied,0);

//        try {
//            StateHolder.voteBlockingQueue.poll(waitTime(),TimeUnit.MILLISECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        if (judge(res)){
//            StateHolder.currentTerm=term;//TODO 线程不安全
            cuttentState = LEADER_STATE;
        }else {
            // 失败 超时 选举的时候已经有其它成功了(这个状态貌似和失败是对立的)
            cuttentState = FOLLOWER_STATE;

        }
        StateHolder.inCondidate=false;

    }

    private boolean judge(RequestVoteResponse res) {
        RequestVoteResponse[] responses = res.getResponses();
        int n=Config.halfN;
        int ok=1;
        for (int i = 0; i < responses.length; i++) {
            if (responses[i].isVoteGranted())
                ok++;
        }
        return ok>=n;
    }

    /**
     * 暂时唯一要做的就是不停地向所有follwer发送entry ,有一个合适的时间间隔； 这里暂时不考虑leader被取代
     * //TODO 分区后的状态转换
     * */
    private void workAsLeader()  {
        while (true){
            raftPoint.appendEntries(StateHolder.currentTerm
                            ,Config.ID
                            ,0
                            ,0
                            ,new Entry[0]
                            ,StateHolder.commitIndex);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 不停地定时阻塞在某个blockingQueue中取心跳or报文(初始情况下不做区分)
     *          -- 这里貌似还需要添加一个转换报文,如果阻塞在原地的时候收到一份RequestVote请求,需要重新开始取报文（投票报文rpc会取做）
     *          （不用转换，在逻辑处理的时候添加到appendBlockingQueue就好）
     * 1. 如果超时没有取到有效报文,则将结点状态转换为 CONDIDATE_STATE 结束本轮工作；
     * 2. 若在给定时间内收到的心跳or报文，则重新进入等待
     *
     * */
    private void workAsFollower() {
        AppendEntriesResponse res=null;
        try {
            res=StateHolder.appendBlockingQueue.poll(waitTime(), TimeUnit.MILLISECONDS);//这里要等多久呢
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (res==null)
            cuttentState = CONDIDATE_STATE;

    }
}
