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
    @Query("DELETE FROM Distances WHERE fromDevice.deviceId=:fromDevice and toDevice.deviceId=:toDevice")
        // HQL
    void delete(Integer fromDevice, Integer toDevice);
}
