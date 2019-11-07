package com.conference.management.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.conference.management.bo.Talk;
import com.conference.management.util.ConferenceConstants;

/**
 * @author kishore
 *
 */
public class ConferenceSourceManager {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	/**
	 * This method will fetch talks from provided file
	 * @param file
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	public List<Talk> fetchTalks(MultipartFile file, String fileName) throws FileNotFoundException {

		List<Talk> talkList = new ArrayList<>();
		InputStream in = null;
		if(null == file) {
			in = this.getClass().getClassLoader().getResourceAsStream(fileName);
			if(null == in)
				throw new FileNotFoundException();
		}
		  
		else {
			try {
				in = file.getInputStream();
			} catch (IOException e1) {
				logger.error("IOException: ", e1.getMessage());
			}
		}
		
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String strLine;
		int intMinutes;

		try {
			while ((strLine = br.readLine()) != null) {
				if (strLine.contains("//") || strLine.isEmpty())
					continue;

				String title = strLine.substring(0, strLine.lastIndexOf(" "));
				String minutesString = strLine.substring(strLine.lastIndexOf(" ") + 1);
				String minutes = strLine.replaceAll("\\D+", "");
				if (ConferenceConstants.LIGHTNING_TALK.equals(minutesString)) {
					intMinutes = ConferenceConstants.LIGHTNING_TALK_DURATION_MINUTES;
				} else {
					try {
						intMinutes = Integer.parseInt(minutes);
					} catch (NumberFormatException e) {
						logger.error("Could not parse the line : " + strLine);
						try {
							br.close();							
						} catch (IOException ex) {
							logger.error(ex.getMessage());							
						}
						throw e;
					}
				}
				Talk singleTalk = new Talk(title, intMinutes);
				talkList.add(singleTalk);

			}
		} catch (IOException e) {
			logger.error("IOException : " + e.getMessage());
		} finally {
			try {
				in.close();
				br.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return talkList;
	}

}
