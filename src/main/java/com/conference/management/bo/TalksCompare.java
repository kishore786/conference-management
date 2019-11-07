package com.conference.management.bo;

import java.util.Comparator;

/**
 * @author kishore
 *
 */
public class TalksCompare implements Comparator<Talk>{

    @Override
    public int compare(Talk t1, Talk t2) {
        if(t1.getDurationInMinutes() < t2.getDurationInMinutes()){
            return 1;
        } else {
            return -1;
        }
    }
}
