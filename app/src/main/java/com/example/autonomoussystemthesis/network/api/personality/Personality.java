package com.example.autonomoussystemthesis.network.api.personality;

public class Personality {

    private final Integer per_id;
    private final String personality_name;
    private final String hue_color;
    private final Integer bri;
    private final Integer hue;
    private final Integer sat;
    private final String screen_color;
    private final Integer vibration_level;
    private final String music_genre;

    Personality(Integer per_id, String personality_name, String hue_color, Integer bri, Integer hue, Integer sat, String screen_color, Integer vibration_level, String music_genre) {
        this.per_id = per_id;
        this.personality_name = personality_name;
        this.hue_color = hue_color;
        this.bri = bri;
        this.hue = hue;
        this.sat = sat;
        this.screen_color = screen_color;
        this.vibration_level = vibration_level;
        this.music_genre = music_genre;
    }

    public Integer getPer_id() {
        return per_id;
    }

    public String getPersonality_name() {
        return personality_name;
    }

    public String getHue_color() {
        return hue_color;
    }

    public Integer getBri() {
        return bri;
    }

    public Integer getHue() {
        return hue;
    }

    public Integer getSat() {
        return sat;
    }

    public String getScreen_color() {
        return screen_color;
    }

    public Integer getVibration_level() {
        return vibration_level;
    }

    public String getMusic_genre() {
        return music_genre;
    }

}