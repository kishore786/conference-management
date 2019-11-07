package com.conference.management.controller;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.conference.management.dto.ConferenceResponse;
import com.conference.management.exceptions.ConferenceSourceException;
import com.conference.management.service.ConferenceService;

/**
 * @author kishore
 *
 */
@RestController
@RequestMapping("/conference")
public class ConfMgmtController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ConferenceService conferenceService;

	@PostMapping
	public ResponseEntity<ConferenceResponse> createLegalEntity(@RequestParam("file") MultipartFile file) {
		logger.info("ConfMgmtController...");
		ConferenceResponse response = null;
		try {
			response = conferenceService.getDetails(file);
			if(response == null) {
				return new ResponseEntity<ConferenceResponse>(response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (ConferenceSourceException e) {
			logger.error("Error in ConfMgmtController: "+e.getMessage());
			response=new ConferenceResponse();
			response.setOutput(Arrays.asList(e.getMessage()));
			return new ResponseEntity<ConferenceResponse>(response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ConferenceResponse>(response, new HttpHeaders(), HttpStatus.OK);
	}

}
