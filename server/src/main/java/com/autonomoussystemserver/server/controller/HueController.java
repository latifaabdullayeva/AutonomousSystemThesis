package com.autonomoussystemserver.server.controller;

import com.autonomoussystemserver.server.controller.model.HueDto;
import com.autonomoussystemserver.server.database.model.Hue;
import com.autonomoussystemserver.server.database.model.Personality;
import com.autonomoussystemserver.server.database.repository.HueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HueController {
    @Autowired
    private HueRepository hueRepository;

    @GetMapping("/hue")
    public Page<Hue> getHueData(Pageable pageable) {
        System.out.println("Backend: " + "HueController -> GET getHueData() = " + pageable);
        return hueRepository.findAll(pageable);
    }

    @PostMapping("/hue")
    public ResponseEntity<Hue> createHueData(@RequestBody HueDto hueDto) {
        System.out.println("Backend:" + " HueController -> POST HueController()");

        Hue existingHue = hueRepository.findByIpAddress(hueDto.getIpAddress());
        System.out.println("Backend:" + " HueController -> POST existingHue = " + existingHue);

        if (existingHue != null) {
            return ResponseEntity.badRequest()
                    .body(null);
        } else {
            Hue newHue = new Hue();
            newHue.setUserName(hueDto.getUserName());
            newHue.setIpAddress(hueDto.getIpAddress());

            hueRepository.save(newHue);
            System.out.println("Backend:" + " HueController -> POST newHue: " + newHue);
            return ResponseEntity.ok(newHue);
        }
    }
}
