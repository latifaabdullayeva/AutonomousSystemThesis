package com.autonomoussystemserver.server.database.repository;

import com.autonomoussystemserver.server.database.model.Personality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PersonalityRepository extends JpaRepository<Personality, Integer>, CrudRepository<Personality, Integer> {
    @Query("select per from Personality per where personality_name=:personalityName")
    Personality findByPersonalityName(String personalityName);
}
