package com.conference.management.bo;

import com.conference.management.util.ConferenceConstants;

/**
 * @author kishore
 *
 */
public class Lunch extends Event {
    public Lunch() {
        super(ConferenceConstants.LUNCH_START_TIME, "Lunch", ConferenceConstants.LUNCH_DURATION_MINUTES);
    }
}
