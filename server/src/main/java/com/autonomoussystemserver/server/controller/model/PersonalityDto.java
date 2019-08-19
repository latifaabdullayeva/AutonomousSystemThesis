package com.autonomoussystemserver.server.controller.model;

public class PersonalityDto {
    private String personality_name;
    private String hue_color;
    private Integer bri;
    private Integer hue;
    private Integer sat;
    private String screen_color;
    private Integer vibration_level;
    private String music_genre;

    public PersonalityDto(String personality_name, String hue_color, Integer bri, Integer hue, Integer sat, String screen_color, Integer vibration_level, String music_genre) {
        this.personality_name = personality_name;
        this.hue_color = hue_color;
        this.bri = bri;
        this.hue = hue;
        this.sat = sat;
        this.screen_color = screen_color;
        this.vibration_level = vibration_level;
        this.music_genre = music_genre;
        System.out.println("Backend: " + "PersonalityDto constructor = " + personality_name + hue_color + bri + hue + sat + screen_color + vibration_level + music_genre);
    }

    public String getPersonality_name() {
        System.out.println("Backend: " + "PersonalityDto getPersonality_name() = " + personality_name);
        return personality_name;
    }

    public String getHue_color() {
        System.out.println("Backend: " + "PersonalityDto getHue_color() = " + hue_color);
        return hue_color;
    }

    public Integer getBri() {
        System.out.println("Backend: " + "PersonalityDto getBri() = " + bri);
        return bri;
    }

    public Integer getHue() {
        System.out.println("Backend: " + "PersonalityDto getHue() = " + hue);
        return hue;
    }

    public Integer getSat() {
        System.out.println("Backend: " + "PersonalityDto getSat() = " + sat);
        return sat;
    }

    public String getScreen_color() {
        System.out.println("Backend: " + "PersonalityDto getScreen_color() = " + screen_color);
        return screen_color;
    }

    public Integer getVibration_level() {
        System.out.println("Backend: " + "PersonalityDto getVibration_level() = " + vibration_level);
        return vibration_level;
    }

    public String getMusic_genre() {
        System.out.println("Backend: " + "PersonalityDto getMusic_genre() = " + music_genre);
        return music_genre;
    }

}