package com.autonomoussystemserver.server.database.repository;

import com.autonomoussystemserver.server.database.model.Distances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DistancesRepository extends JpaRepository<Distances, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Distances WHERE fk_from=$1 and fk_to=$2")
    void deleteUsingSingleQuery(Integer from, Integer to);
}