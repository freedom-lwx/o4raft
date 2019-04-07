import edu.nuaa.o4raft.entity.AppendEntriesResponse;
import edu.nuaa.o4raft.entity.Entry;
import edu.nuaa.o4raft.entity.RequestVoteResponse;
import edu.nuaa.o4raft.interfacee.RaftPoint;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConsumeTester {
    public static void main(String[] args) throws InterruptedException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"consumer-test.xml"});
        context.start();
        // Obtaining a remote service proxy
        RaftPoint point=(RaftPoint)context.getBean("raftPoint");
        Entry[] entries=new Entry[0];
        int idx=0;
        while (true){
            AppendEntriesResponse ar=point.appendEntries(idx,idx,idx,idx,entries,idx);
            RequestVoteResponse rr=point.requestVote(idx,idx,idx,idx);
            System.out.println("ar : " + ar +" rr: "+rr);
            Thread.sleep(1000);
            idx++;
        }

    }
}
