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
    @Query("DELETE FROM Distances WHERE from_device=:from_device and to_device=:to_device")
        // HQL
    void delete(Integer from_device, Integer to_device);
}