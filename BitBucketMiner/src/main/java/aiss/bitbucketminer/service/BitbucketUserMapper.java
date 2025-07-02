package aiss.bitbucketminer.service;

import aiss.bitbucketminer.models.User;

import java.util.Map;

public class BitbucketUserMapper {
    public User mapUser(Map<String, Object> raw) {
        if (raw == null) return null;
        User user = new User();
        user.setId((String) raw.get("uuid"));
        user.setUsername((String) raw.get("username"));
        user.setName((String) raw.get("display_name"));

        Map<String, Object> links = (Map<String, Object>) raw.get("links");
        if (links != null && links.containsKey("avatar")) {
            Map<String, Object> avatar = (Map<String, Object>) links.get("avatar");
            user.setAvatar_url((String) avatar.get("href"));
        }

        // Bitbucket no da directamente url de usuarios (???).
        user.setWeb_url(null);
        return user;
    }
}
