package com.jha.accessingdatamysql.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.jha.accessingdatamysql.Entities.Hazard;
import jakarta.transaction.Transactional;

public interface HazardRepository extends CrudRepository<Hazard, Integer>{
    List<Hazard> findByTaskTaskID(Integer taskID);
    Hazard findByHazardID(Integer hazardID);
    @Transactional
    Integer deleteByHazardID(Integer hazardID);
}
