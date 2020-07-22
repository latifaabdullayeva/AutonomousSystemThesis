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
        return personalityRepository.findAll(pageable);
    }

    @PostMapping("/personality")
    public ResponseEntity<Personality> createPersonality(@RequestBody PersonalityDto personalityDto) {
        System.out.println("PersonalityController: createPersonality() from DTO: \n" + personalityDto.getPersonality_name() + "; "
                + personalityDto.getHue_color() + "; " + personalityDto.getMusic_genre() + "; " + personalityDto.getVibration_level());

        Personality existingPersonality = personalityRepository.findByPersonalityName(personalityDto.getPersonality_name());

        if (existingPersonality != null) {
            return ResponseEntity.badRequest().body(null);
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

            System.out.println("PersonalityController: createPersonality(): \n" + newPersonality.getPersonality_name() + "; "
                    + newPersonality.getHue_color() + "; " + newPersonality.getMusic_genre() + "; " + newPersonality.getVibration_level());

            return ResponseEntity.ok(newPersonality);
        }
    }
}