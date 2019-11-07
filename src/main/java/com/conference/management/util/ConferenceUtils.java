package com.conference.management.util;

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.conference.management.bo.Talk;

/**
 * @author kishore
 *
 */
public class ConferenceUtils {
	private static final Logger logger = LoggerFactory.getLogger(ConferenceUtils.class);
	
    public static Calendar getCalendarTime(int hours, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, minutes);
        return cal;
    }

    public static Calendar getNextStartTime(Calendar currentStartTime, Talk currentTalk) {
        Calendar newTime = Calendar.getInstance();
        newTime.set(Calendar.HOUR_OF_DAY, currentStartTime.get(Calendar.HOUR_OF_DAY));
        newTime.set(Calendar.MINUTE, currentStartTime.get(Calendar.MINUTE));
        newTime.add(Calendar.MINUTE, currentTalk.getDurationInMinutes());
        return newTime;
    }

    public static void printTalks(List<Talk> talkList) {
        logger.info("--------------------------------------------------------");
        logger.info("Input talks:");
        for (Talk talk : talkList) {
            logger.info(talk.getTitle() + " " + talk.getDurationInMinutes());
        }
        logger.info("--------------------------------------------------------");
    }

}
