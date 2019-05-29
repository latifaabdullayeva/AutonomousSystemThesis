package com.autonomoussystemserver.server.database.repository;

import com.autonomoussystemserver.server.database.model.Devices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevicesRepository extends JpaRepository<Devices, Integer>{
}
