package edu.nuaa.o4raft.entity;

import java.io.Serializable;

public class Entry implements Serializable {
    final public String datas;
    final public int term;


    public Entry(String datas, int term) {
        this.datas = datas;
        this.term = term;
    }
}
