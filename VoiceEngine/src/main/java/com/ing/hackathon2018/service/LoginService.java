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
public class LoginService {
    @Autowired
    private VoiceProvider voiceProvider;

    public String verifyRecord(String userId, String base64Record) {
        String result = voiceVerification(userId, "en-US", Base64.decodeBase64(base64Record));

        System.out.println("RESULT: " + result);
        return result;
    }

    private String voiceVerification(String userId, String contentLanguage, byte[] recording) {

        HttpEntity entity = MultipartEntityBuilder
                .create()
                .addTextBody("userId", userId)
                .addTextBody("contentLanguage", contentLanguage)
                .addBinaryBody("recording", recording, ContentType.create("application/octet-stream"), "recording")
                .build();
        HttpPost httpPost = new HttpPost(voiceProvider.getAPI_URL() + "/verification");
        httpPost.setEntity(entity);

        try {
            return EntityUtils.toString(voiceProvider.getHttpClient().execute(httpPost).getEntity());
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
