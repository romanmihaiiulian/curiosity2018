package com.ing.hackathon2018.web;

import com.ing.hackathon2018.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RestController
public class EnrollHandler {
    @Autowired
    private RegisterService registerService;

    @RequestMapping(value = "/api/enroll/{id}", method = RequestMethod.POST)
    public Mono register(@PathVariable(value="id") String id, @RequestBody String base64Record) {
        registerService.registerUser(id, base64Record);

        return Mono.just
                (ServerResponse.ok().build());
    }
}
