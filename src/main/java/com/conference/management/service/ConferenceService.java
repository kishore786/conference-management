package com.conference.management.service;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.conference.management.bo.Conference;
import com.conference.management.bo.Event;
import com.conference.management.bo.Lunch;
import com.conference.management.bo.Networking;
import com.conference.management.bo.Slot;
import com.conference.management.bo.Talk;
import com.conference.management.bo.TalksCompare;
import com.conference.management.bo.Track;
import com.conference.management.dto.ConferenceResponse;
import com.conference.management.enums.DataOutputEnum;
import com.conference.management.enums.DataSourceEnum;
import com.conference.management.exceptions.ConferenceDestinationException;
import com.conference.management.manager.ConferenceSourceManager;
import com.conference.management.manager.ConferenceOutputManager;
import com.conference.management.exceptions.ConferenceSourceException;
import com.conference.management.util.ConferenceConstants;
import com.conference.management.util.ConferenceUtils;

/**
 * @author kishore
 *
 */
@Service
public class ConferenceService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	/** 
	 * This will fetch the input talk list.
	 * @param file
	 * @return	 
	 * @throws Exception
	 *  @throws ConferenceSourceException 
	 */
	public ConferenceResponse getDetails(MultipartFile file) throws ConferenceSourceException {
		ConferenceResponse response = new ConferenceResponse();

		List<Talk> talkList = null;
		try {
			talkList = fetchTalksListFromSource(DataSourceEnum.FILE, file);
		} catch (FileNotFoundException | ConferenceSourceException | NumberFormatException e) {
			logger.error("Error: ", e.getMessage());
			throw new ConferenceSourceException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid talk file.");
		}

		if (talkList == null || talkList.size() == 0)
			throw new ConferenceSourceException(HttpStatus.INTERNAL_SERVER_ERROR, "Source not found.");

		ConferenceUtils.printTalks(talkList);
		Conference conference = processAndScheduleTalks(talkList);

		try {
			response.setOutput(outputConferenceSchedule(conference, DataOutputEnum.CONSOLE));
		} catch (ConferenceDestinationException e) {
			logger.error("Error : ",e.getMessage());
		}

		return response;
	}

	/**
	 * This method will create an instance of required source
	 * @param dataSourceEnum
	 * @return
	 * @throws ConferenceSourceException
	 * @throws FileNotFoundException
	 */
	private List<Talk> fetchTalksListFromSource(DataSourceEnum dataSourceEnum, MultipartFile file)
			throws ConferenceSourceException, FileNotFoundException {

		if (dataSourceEnum.equals(DataSourceEnum.FILE)) {
			ConferenceSourceManager sourceManager = new ConferenceSourceManager();
			try {
				return sourceManager.fetchTalks(file,null);
			} catch (FileNotFoundException | NumberFormatException e) {
				throw e;
			}
		} else {
			throw new ConferenceSourceException(HttpStatus.INTERNAL_SERVER_ERROR, "Source type not valid");
		}
	}

	/**
	 * This method will create an instance of the required output
	 * @param conference
	 * @param dataOutputEnum
	 * @return
	 * @throws ConferenceDestinationException
	 */
	private List<String> outputConferenceSchedule(Conference conference, DataOutputEnum dataOutputEnum)
			throws ConferenceDestinationException {

		if (dataOutputEnum.equals(DataOutputEnum.CONSOLE.CONSOLE)) {
			ConferenceOutputManager outputManager = new ConferenceOutputManager();
			return outputManager.printSchedule(conference);
		} else {
			throw new ConferenceDestinationException("Destination not valid.");
		}
	}

	/**
	 * This method will process and schedule talks
	 * @param talkList
	 * @return
	 */
	private Conference processAndScheduleTalks(List<Talk> talkList) {
		Conference conference = new Conference();

		Collections.sort(talkList, new TalksCompare());
		int trackCount = 0;

		while (0 != talkList.size()) {

			Slot morningSlot = new Slot(ConferenceConstants.MORNING_SLOT_DURATION_MINUTES,
					ConferenceConstants.TRACK_START_TIME);
			fillSlot(morningSlot, talkList);

			Slot lunchSlot = new Slot(ConferenceConstants.LUNCH_DURATION_MINUTES, ConferenceConstants.LUNCH_START_TIME);
			lunchSlot.addEvent(new Lunch());

			Slot afternoonSlot = new Slot(ConferenceConstants.AFTERNOON_SLOT_DURATION_MINUTES,
					ConferenceConstants.POST_LUNCH_SLOT_START_TIME);
			fillSlot(afternoonSlot, talkList);

			Slot networkingSlot = new Slot(ConferenceConstants.NETWORKING_DURATION_MINUTES,
					ConferenceConstants.NETWORKING_START_TIME);
			networkingSlot.addEvent(new Networking());

			Track track = new Track(++trackCount);
			track.addSlot(morningSlot);
			track.addSlot(lunchSlot);
			track.addSlot(afternoonSlot);
			track.addSlot(networkingSlot);
			conference.addTrack(track);
		}

		return conference;
	}

	/**
	 * This method will fill the slot from start to end
	 * @param slot
	 * @param talks
	 */
	private void fillSlot(Slot slot, List<Talk> talks) {
		
		Calendar currentStartTime = slot.getStartTime();
		for (Iterator<Talk> talksIterator = talks.iterator(); talksIterator.hasNext();) {
			Talk talk = talksIterator.next();
			if (slot.hasRoomFor(talk)) {
				slot.addEvent(new Event(currentStartTime, talk.getTitle(), talk.getDurationInMinutes()));
				currentStartTime = ConferenceUtils.getNextStartTime(currentStartTime, talk);
				talksIterator.remove();
			}
		}
	}

}
