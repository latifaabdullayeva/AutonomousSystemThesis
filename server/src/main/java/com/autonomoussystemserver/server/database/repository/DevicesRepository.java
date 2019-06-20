package com.autonomoussystemserver.server.database.repository;

import com.autonomoussystemserver.server.database.model.Devices;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DevicesRepository extends JpaRepository<Devices, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Devices WHERE device_name=:device_name")
        // HQL
    void delete(String device_name);
}
