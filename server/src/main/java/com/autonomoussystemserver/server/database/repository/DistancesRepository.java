package com.autonomoussystemserver.server.database.repository;

import com.autonomoussystemserver.server.database.model.Distances;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistancesRepository extends JpaRepository<Distances, Integer>, CrudRepository<Distances, Integer> {

    //    @Modifying
//    @Transactional
//    @Query("DELETE FROM Distances WHERE fromDevice.deviceId=:fromDevice and toDevice.deviceId=:toDevice")
    @Query("select dis from Distances dis where fromDevice.deviceId=:fromDevice and toDevice.deviceId=:toDevice")
    // HQL
    Distances findByFromAndTo(Integer fromDevice, Integer toDevice);
}