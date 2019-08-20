package com.autonomoussystemserver.server.controller;

import com.autonomoussystemserver.server.controller.model.PersonalityDto;
import com.autonomoussystemserver.server.database.model.Personality;
import com.autonomoussystemserver.server.database.repository.PersonalityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonalityController {
    @Autowired
    private PersonalityRepository personalityRepository;

    @GetMapping("/personality")
    public Page<Personality> getPersonality(Pageable pageable) {
        System.out.println("Backend: " + "PersonalityController -> GET getPersonality() = " + pageable);
        return personalityRepository.findAll(pageable);
    }

    @PostMapping("/personality")
    public ResponseEntity<Personality> createPersonality(@RequestBody PersonalityDto personalityDto) {
        System.out.println("Backend: " + "PersonalityController -> POST createPersonality()");

        Personality existingPersonality = personalityRepository.findByPersonalityName(personalityDto.getPersonality_name());
        System.out.println("Backend: " + "PersonalityController -> POST existingPersonality = " + existingPersonality);

        if (existingPersonality != null) {
            return ResponseEntity.badRequest()
                    .body(null);
        } else {
            Personality newPersonality = new Personality();
            newPersonality.setPersonality_name(personalityDto.getPersonality_name());
            newPersonality.setBri(personalityDto.getBri());
            newPersonality.setHue(personalityDto.getHue());
            newPersonality.setHue_color(personalityDto.getHue_color());
            newPersonality.setSat(personalityDto.getSat());
            newPersonality.setMusic_genre(personalityDto.getMusic_genre());
            newPersonality.setScreen_color(personalityDto.getScreen_color());
            newPersonality.setVibration_level(personalityDto.getVibration_level());


            personalityRepository.save(newPersonality);
            System.out.println("Backend: " + "PersonalityController -> POST newPersonality: " + newPersonality);
            return ResponseEntity.ok(newPersonality);
        }
    }
}