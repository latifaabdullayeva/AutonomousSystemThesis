package com.autonomoussystemserver.server.database.repository;

import com.autonomoussystemserver.server.database.model.Devices;
import com.autonomoussystemserver.server.database.model.Distances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface DistancesRepository extends JpaRepository<Distances, Integer> {

    @Modifying
    @Transactional
//    @Query("DELETE FROM distances WHERE fk_from=$1 and fk_to=$2")
//    void deleteUsingSingleQuery(Integer from, Integer to);

    //TODO: if count>0, then
    @Query("select COUNT(*) from Distances where fk_from=$1 and fk_to=$2")
    int selectMatchingFromAndTo(Integer from, Integer to);

    @Query("UPDATE Distances SET distance=$1 WHERE fk_from=$2 and fk_to=$3")
    void updateMatchingFromAndTo(Integer distance, Integer from, Integer to);
}