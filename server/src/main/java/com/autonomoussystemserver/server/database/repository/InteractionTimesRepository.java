package com.autonomoussystemserver.server.database.repository;

import com.autonomoussystemserver.server.database.model.Devices;
import com.autonomoussystemserver.server.database.model.InteractionTimes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface InteractionTimesRepository extends JpaRepository<InteractionTimes, Integer>, CrudRepository<InteractionTimes, Integer> {

    @Query("select interaction from InteractionTimes interaction where mascotId=:mascot")
    InteractionTimes findByMascot(Devices mascot);
}
