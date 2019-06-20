package com.autonomoussystemserver.server.database.repository;

import com.autonomoussystemserver.server.database.model.Devices;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DevicesRepository extends JpaRepository<Devices, Integer>, CrudRepository<Devices, Integer> {

    @Query("select dev from Devices dev where beacon_tag=:tag")
    Devices findByBeacon(String tag);

//    @Modifying
//    @Transactional
//    @Query("select count(e)>0 from Devices e where beacon_tag=:beacon_tag")
//        //HQL
////    void delete(String beacon_tag);
//    int delete(@Param("beacon_tag") String beacon_tag);
}

//    @Query("DELETE FROM Devices WHERE beacon_tag=:beacon_tag and device_name=:device_name")

//    @Query("UPDATE Devices SET beacon_tag=:beacon_tag WHERE EXISTS (SELECT beacon_tag FROM Devices WHERE beacon_tag=:beacon_tag)")
