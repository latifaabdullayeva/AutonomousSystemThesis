package com.autonomoussystemserver.server.repository;

import com.autonomoussystemserver.server.domainModel.Distances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistancesRepository extends JpaRepository<Distances, Long> {
    //    List<Devices> findByQuestionId(Long questionId); device_id
}
