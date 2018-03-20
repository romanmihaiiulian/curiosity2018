package com.ing.hackathon2018.service;

import com.ing.hackathon2018.voiceit.VoiceProvider;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    @Autowired
    private VoiceProvider voiceProvider;

    public void registerUser(String base64RecordFingerprint) {
        String userId = createUser();
        System.out.println("USER ID: " + userId);

        String result = createVoiceEnrollment(userId, "en-US", Base64.decodeBase64(base64RecordFingerprint));
        System.out.println("Voice enroll: " + result);
    }

    private String createUser() {
        try {
            return EntityUtils.toString(voiceProvider.getHttpClient().execute(
                    new HttpPost(voiceProvider.getAPI_URL() + "/users")).getEntity());
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String createVoiceEnrollment(String userId, String contentLanguage, byte[] recording) {

        HttpEntity entity = MultipartEntityBuilder
                .create()
                .addTextBody("userId", userId)
                .addTextBody("contentLanguage", contentLanguage)
                .addBinaryBody("recording", recording, ContentType.create("application/octet-stream"), "recording")
                .build();
        HttpPost httpPost = new HttpPost(voiceProvider.getAPI_URL() + "/enrollments");
        httpPost.setEntity(entity);

        try {
            return EntityUtils.toString(voiceProvider.getHttpClient().execute(httpPost).getEntity());
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
