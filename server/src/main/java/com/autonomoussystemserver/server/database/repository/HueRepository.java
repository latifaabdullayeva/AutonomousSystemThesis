package com.autonomoussystemserver.server.database.repository;

import com.autonomoussystemserver.server.database.model.Hue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface HueRepository extends JpaRepository<Hue, Integer>, CrudRepository<Hue, Integer> {

    @Query("select h from Hue h where ip_address=:ipAddress")
    Hue findByIpAddress(String ipAddress);

}