package com.remimichel.utils;

import com.remimichel.model.Torrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Torrents extends ArrayList<Torrent> implements Comparable<Torrents> {

    public Torrents(Collection<Torrent> c){
        super(c);
    }
    public Torrents(Torrent t){
        this.add(t);
    }

    public String[] toStringArray(){
        String[] s = new String[this.size()];
        int i = 0;
        Iterator<Torrent> it = this.iterator();
        System.out.println(this.size());
        while(it.hasNext()){
            s[i] = this.get(i).toString();
            it.next();
            i++;
        }
        return s;
    }

    @Override
    public int compareTo(Torrents torrents) {
        if(this == null && torrents == null) return 0;
        if(this == null && torrents != null) return -1;
        if(this != null && torrents == null) return -1;
        return this.get(0).compareTo(torrents.get(0));
    }
}
