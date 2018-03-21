package com.ing.hackathon2018.web;

import com.ing.hackathon2018.voiceit.VoiceIt2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class RegisterHandler {
    @Autowired
    private VoiceIt2 voiceIt2;

    @CrossOrigin
    @RequestMapping(value = "/api/register", method = RequestMethod.POST)
    public Mono register() {

        String response = voiceIt2.createUser();
        System.out.println(voiceIt2.createUser());

        Pattern pattern = Pattern.compile("userId\":\"(.*)\",\"timeTaken");
        Matcher matcher = pattern.matcher(response);
//        matcher.matches();

        String userId = "";
        if (matcher.find()) {
            userId = matcher.group(1);
        }

        System.out.println(userId);

        return Mono.just(userId);
    }
}
