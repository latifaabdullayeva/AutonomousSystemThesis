package com.autonomoussystemserver.server.repository;

import com.autonomoussystemserver.server.domainModel.Distances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DistancesRepository extends JpaRepository<Distances, UUID> {
    List<Distances> findByQuestionId(UUID device_id);
}
