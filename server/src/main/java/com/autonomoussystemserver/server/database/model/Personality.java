package com.autonomoussystemserver.server.database.model;

import javax.persistence.*;

@Table(name = "personality")
@Entity
public class Personality {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "personality_id", unique = true, nullable = false, updatable = false)
    private Integer id;

    @Column(name = "hue_color", nullable = false, updatable = false)
    private String hue_color;

    @Column(name = "screen_color", nullable = false, updatable = false)
    private String screen_color;

    @Column(name = "vibration_level", nullable = false, updatable = false)
    private String vibration_level;

    @Column(name = "music_genre", nullable = false, updatable = false)
    private String music_genre;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHue_color() {
        return hue_color;
    }

    public void setHue_color(String hue_color) {
        this.hue_color = hue_color;
    }

    public String getScreen_color() {
        return screen_color;
    }

    public void setScreen_color(String screen_color) {
        this.screen_color = screen_color;
    }

    public String getVibration_level() {
        return vibration_level;
    }

    public void setVibration_level(String vibration_level) {
        this.vibration_level = vibration_level;
    }

    public String getMusic_genre() {
        return music_genre;
    }

    public void setMusic_genre(String music_genre) {
        this.music_genre = music_genre;
    }
}
