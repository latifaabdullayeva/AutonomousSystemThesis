package com.autonomoussystemserver.server.database.repository;

import com.autonomoussystemserver.server.database.model.Devices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevicesRepository extends JpaRepository<Devices, Integer>, CrudRepository<Devices, Integer> {

    @Query("select dev from Devices dev where beaconUuid=:beaconUuid")
    Devices findByBeacon(String beaconUuid);
}