package com.sueandphilip.randy;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

import java.util.HashSet;
import java.util.Set;

public final class RandySpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
    private static final Set<String> supportedApplicationIds = new HashSet<>();
    static {
        /*
         * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
         * Alexa Skill and put the relevant Application Ids in this Set.
         */
        supportedApplicationIds.add("amzn1.echo-sdk-ams.app.eb76807a-ccc0-434c-a298-cbfae44e8c65");
    }

    public RandySpeechletRequestStreamHandler() {
        super(new RandySpeechlet(), supportedApplicationIds);
    }
}
