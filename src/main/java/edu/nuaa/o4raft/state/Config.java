package edu.nuaa.o4raft.state;

public class Config {
    public static final int ID = Integer.valueOf(System.getProperty("pointId","-1"));
    public static final int N = Integer.valueOf(System.getProperty("nodes","5"));
    public static final int halfN = N/2 + 1;
}
