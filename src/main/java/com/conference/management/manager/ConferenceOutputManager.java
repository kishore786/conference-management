package com.conference.management.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.conference.management.bo.Conference;
import com.conference.management.bo.Event;
import com.conference.management.bo.Slot;
import com.conference.management.bo.Track;
import com.conference.management.util.ConferenceConstants;

/**
 * @author kishore
 *
 */
public class ConferenceOutputManager {
	
	/**
	 * This method will print the talks based on total talks
	 * @param conference
	 * @return
	 */
	public List<String> printSchedule(Conference conference) {

		SimpleDateFormat sdf = ConferenceConstants.DATE_FORMAT;
		List<String> outputList= new ArrayList<String>();
		outputList.add("Output: Conference Schedule :");
		for (Track track : conference.getTracks()) {			
			outputList.add("Track " + track.getTrackId());
			List<Slot> slots = track.getSlots();
			for (Slot slot : slots) {
				for (Event event : slot.getEvents()) {	
					outputList.add(sdf.format(event.getStartTime().getTime()) + " " + event.getTitle() + " "
							+ event.getDurationInMinutes());
				}
			}
			outputList.add("--------------------------------------------------------");
		}
		
		return outputList;
	}

}
