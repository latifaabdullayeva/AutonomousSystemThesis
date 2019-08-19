package com.example.autonomoussystemthesis.network.api.personality;

import java.util.List;

public class ApiPersonalityResponse {

    private List<Personality> content;

    public ApiPersonalityResponse(List<Personality> content) {
        this.content = content;
    }

    public List<Personality> getContent() {
        return content;
    }
}
