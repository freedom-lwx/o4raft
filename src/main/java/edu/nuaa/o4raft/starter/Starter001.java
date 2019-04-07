package edu.nuaa.o4raft.starter;

import edu.nuaa.o4raft.interfacee.RaftPoint;
import edu.nuaa.o4raft.state.StateMachine;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
//-Djava.net.preferIPv4Stack=true
public class Starter001 {

    public static void main(String[] args) throws IOException {
        System.setProperty("java.net.preferIPv4Stack", "true");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"start-config001.xml"});
        context.start();
        System.out.println("Provider started.");
        RaftPoint raftPoints=(RaftPoint)context.getBean("otherPoint");
        StateMachine.start(raftPoints);
        System.in.read(); // press any key to exit
    }
}
