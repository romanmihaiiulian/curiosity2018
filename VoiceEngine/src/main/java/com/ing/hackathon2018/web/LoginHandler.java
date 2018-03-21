package com.ing.hackathon2018.web;

import com.ing.hackathon2018.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@RestController
public class LoginHandler {
    @Autowired
    private LoginService loginService;

    @CrossOrigin
    @RequestMapping(value = "/api/login/{id}", method = RequestMethod.POST)
    public Mono login(@PathVariable(value="id") String id, @RequestBody String base64Record) {
        loginService.verifyRecord(id, base64Record);

        return Mono.just
                (ServerResponse.ok().build());
    }

}
