package com.ing.hackathon2018.voiceit;

import lombok.Getter;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class VoiceProvider {
    @Getter
    private final String API_URL = "https://api.voiceit.io";

    @Value("${voiceit.key}")
    private String apiKey;

    @Value("${voiceit.token}")
    private String apiToken;

    @Getter
    private HttpClient httpClient;

    @PostConstruct
    private void init() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(apiKey, apiToken));

        httpClient =  HttpClientBuilder.create()
                .setDefaultCredentialsProvider(credentialsProvider).build();
    }
}
