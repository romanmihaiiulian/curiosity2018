package com.ing.hackathon2018.web;

import com.ing.hackathon2018.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RestController
public class RegisterHandler {
    @Autowired
    private RegisterService registerService;

    @RequestMapping(value = "/api/register", method = RequestMethod.POST)
    public Mono register(@RequestBody String base64Record) {
        registerService.registerUser(base64Record);

        return Mono.just
                (ServerResponse.ok().build());
    }
}
