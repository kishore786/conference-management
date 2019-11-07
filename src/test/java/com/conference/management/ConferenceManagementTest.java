package com.conference.management;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.conference.management.bo.Talk;
import com.conference.management.manager.ConferenceSourceManager;

/**
 * @author kishore
 *
 */
public class ConferenceManagementTest {

    ConferenceSourceManager manager = new ConferenceSourceManager();
    
    @Test(expected = FileNotFoundException.class)
    public void testFetchTalksFileNotFound() throws FileNotFoundException {
        manager.fetchTalks(null,"input-test-talks-not-found.txt");
    }
    
    @Test
    public void testFetchTalksSuccess() throws FileNotFoundException {
    	List<Talk> talks = manager.fetchTalks(null,"input-talks.txt");
        Assert.assertEquals(19, talks.size());
    }


    @Test
    public void testFetchTalksValidFile() throws FileNotFoundException {
        List<Talk> talks = manager.fetchTalks(null,"input-test-two-talks.txt");
        Assert.assertEquals(2, talks.size());
    }


    @Test
    public void testFetchTalksEmptyFile() throws FileNotFoundException {
        List<Talk> talks = manager.fetchTalks(null,"input-test-talks-empty.txt");
        Assert.assertEquals(0, talks.size());
    }


    @Test(expected = NumberFormatException.class)
    public void testFetchTalksInvalidFile() throws FileNotFoundException {
        List<Talk> talks = manager.fetchTalks(null,"input-test-invalid-talks.txt");
    }

}