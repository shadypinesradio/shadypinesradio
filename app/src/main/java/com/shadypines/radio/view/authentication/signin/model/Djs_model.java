package com.shadypines.radio.view.authentication.signin.model;

public class Djs_model {

    private String id;
    private String ShowName;
    private String DjName;
    private String subscribers;
    private String pushTitle;
    private String pushMessage;
    private String defaultPushTitle;
    private String defaultPushMessage;
    private String image;
    private Boolean is_temporary_music_recognition;

    public Djs_model(String id, String showName, String djName, String subscribers, String pushTitle, String pushMessage, String defaultPushTitle, String defaultPushMessage, String image, Boolean is_temporary_music_recognition) {
        this.id = id;
        ShowName = showName;
        DjName = djName;
        this.subscribers = subscribers;
        this.pushTitle = pushTitle;
        this.pushMessage = pushMessage;
        this.defaultPushTitle = defaultPushTitle;
        this.defaultPushMessage = defaultPushMessage;
        this.image = image;
        this.is_temporary_music_recognition = is_temporary_music_recognition;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShowName() {
        return ShowName;
    }

    public void setShowName(String showName) {
        ShowName = showName;
    }

    public String getDjName() {
        return DjName;
    }

    public void setDjName(String djName) {
        DjName = djName;
    }

    public String getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(String subscribers) {
        this.subscribers = subscribers;
    }

    public String getPushTitle() {
        return pushTitle;
    }

    public void setPushTitle(String pushTitle) {
        this.pushTitle = pushTitle;
    }

    public String getPushMessage() {
        return pushMessage;
    }

    public void setPushMessage(String pushMessage) {
        this.pushMessage = pushMessage;
    }

    public String getDefaultPushTitle() {
        return defaultPushTitle;
    }

    public void setDefaultPushTitle(String defaultPushTitle) {
        this.defaultPushTitle = defaultPushTitle;
    }

    public String getDefaultPushMessage() {
        return defaultPushMessage;
    }

    public void setDefaultPushMessage(String defaultPushMessage) {
        this.defaultPushMessage = defaultPushMessage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public Boolean getIs_temporary_music_recognition() {
        return is_temporary_music_recognition;
    }

    public void setIs_temporary_music_recognition(Boolean is_temporary_music_recognition) {
        this.is_temporary_music_recognition = is_temporary_music_recognition;
    }
}
