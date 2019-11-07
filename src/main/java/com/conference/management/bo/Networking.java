package com.conference.management.bo;

import com.conference.management.util.ConferenceConstants;

/**
 * @author kishore
 *
 */
public class Networking extends Event {

    public Networking () {
        super(ConferenceConstants.NETWORKING_START_TIME, "Networking Event", 0);
    }
}
