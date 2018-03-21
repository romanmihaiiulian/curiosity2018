package com.ing.hackathon2018.web;

import com.ing.hackathon2018.voiceit.VoiceIt2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RestController
public class RegisterHandler {
    @Autowired
    private VoiceIt2 voiceIt2;

    @RequestMapping(value = "/api/register", method = RequestMethod.POST)
    public Mono register() {
        System.out.println(voiceIt2.createUser());

        return Mono.just
                (ServerResponse.ok().build());
    }
}
