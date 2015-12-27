package com.sueandphilip.randy;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import com.amazon.speech.ui.Reprompt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandySpeechlet implements Speechlet {
    private static final Logger log = LoggerFactory.getLogger(RandySpeechlet.class);
    static HashMap<String, int[]> showHash;
    static String[] listOfShows = {"south park", "king of the hill", "frasier", "futurama", "portlandia", "daria"};

    static {
        showHash = new HashMap<>();
        int[] koth = { 12, 23, 25, 24, 20, 22, 23, 22, 15, 15, 12, 19, 20 };
        int[] sp = {13, 18, 17, 17, 14, 17, 15, 15, 14, 14, 14, 14, 14, 9, 10, 10 };
        int[] frasier = {24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24};
        int[] fut = {13, 19, 22, 18};
        int[] port = {6, 10, 11, 10};
        int[] daria = {13, 13, 13, 14, 13};
        showHash.put("south park", sp);
        showHash.put("king of the hill", koth);
        showHash.put("frasier", frasier);
        showHash.put("futurama", fut);
        showHash.put("portlandia", port);
        showHash.put("daria", daria);
    }

    public SpeechletResponse onIntent(final IntentRequest request, final Session session) throws SpeechletException {
        Intent intent = request.getIntent();
        if ("EpisodeIntent".equals(intent.getName())) {
            String show = intent.getSlot("show").getValue();
            if (show == null) {
                show = "";
            }
            return getEpisode(show.toLowerCase());
        } else {
            throw new SpeechletException("Invalid Intent");
        }
    }

    private SpeechletResponse getEpisode(String show) {
        log.info("Received request for show '" + show + "'");
        PlainTextOutputSpeech mySpeech = new PlainTextOutputSpeech();
        Random randy = new Random();
        if (showHash.containsKey(show)) {
            int season = randy.nextInt(showHash.get(show).length);
            int episode = randy.nextInt(showHash.get(show)[season]) + 1;
            log.info("Randomly generated season " + season + " and episode " + episode);
            mySpeech.setText("Watch " + show + " season " + (season + 1) + " episode " + episode);
            return SpeechletResponse.newTellResponse(mySpeech);
        } else if ("any show".equals(show)) {
            String tv_show = listOfShows[randy.nextInt(listOfShows.length)];
            return getEpisode(tv_show);
        }
        else if (show.isEmpty()){
            mySpeech.setText("Which show?");
            PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
            repromptSpeech.setText("Which show? Choose between " + String.join(", ", Arrays.copyOfRange(listOfShows, 0, listOfShows.length-1)) + " and " + listOfShows[listOfShows.length-1]);
            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(repromptSpeech);
            return SpeechletResponse.newAskResponse(mySpeech, reprompt);
        }
        else {
            mySpeech.setText("I don't know about that show");
            return SpeechletResponse.newTellResponse(mySpeech);
        }

    }

    public SpeechletResponse onLaunch(LaunchRequest request, Session session) {
        PlainTextOutputSpeech mySpeech = new PlainTextOutputSpeech();
        mySpeech.setText("Ask for a random episode of a show");
        return SpeechletResponse.newTellResponse(mySpeech);
    }

    public void onSessionStarted(SessionStartedRequest request, Session session) {}

    public void onSessionEnded(SessionEndedRequest request, Session session) {}
}
