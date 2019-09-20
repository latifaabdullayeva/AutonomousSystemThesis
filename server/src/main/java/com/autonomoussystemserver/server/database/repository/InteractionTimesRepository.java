package com.autonomoussystemserver.server.database.repository;

import com.autonomoussystemserver.server.database.model.Devices;
import com.autonomoussystemserver.server.database.model.InteractionTimes;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InteractionTimesRepository extends JpaRepository<InteractionTimes, Integer>, CrudRepository<InteractionTimes, Integer> {

    @Query("select interaction from InteractionTimes interaction where mascot=:mascot ORDER BY interactionId ASC")
    InteractionTimes findByMascot(Devices mascot);

    @Query("SELECT interaction.mascot FROM InteractionTimes interaction WHERE interaction.interactionTimes = " +
            "(SELECT MAX(interaction.interactionTimes) FROM InteractionTimes interaction)")
    List<Devices> findMaximumInteractedMascot(Pageable pageable);
}
