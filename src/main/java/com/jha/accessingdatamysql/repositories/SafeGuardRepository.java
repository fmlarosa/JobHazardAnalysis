package com.jha.accessingdatamysql.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.jha.accessingdatamysql.Entities.Safeguard;
import jakarta.transaction.Transactional;

public interface SafeGuardRepository extends CrudRepository<Safeguard, Integer>{
    List<Safeguard> findByTaskTaskID(Integer TaskID);
    Safeguard findBySafeguardID(Integer safeguardID);
    @Transactional
    Integer deleteBySafeguardID(Integer safeguardID);
}
