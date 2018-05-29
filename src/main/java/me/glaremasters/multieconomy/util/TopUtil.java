package me.glaremasters.multieconomy.util;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by GlareMasters on 5/29/2018.
 */
public class TopUtil implements Comparator {
    Map map;

    public TopUtil(Map map) {
        this.map = map;
    }

    public int compare(Object o1, Object o2) {
        return ((Integer) map.get(o2)).compareTo((Integer) map.get(o1));
    }

}
