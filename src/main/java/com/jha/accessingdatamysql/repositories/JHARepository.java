package com.jha.accessingdatamysql.repositories;

import org.springframework.data.repository.CrudRepository;
import com.jha.accessingdatamysql.Entities.JHA;
import jakarta.transaction.Transactional;


public interface JHARepository extends CrudRepository<JHA, String> {
    //These functions act as the SQL statements being executed to the MySQL DB
    JHA findByTitle(String title);
    @Transactional
    Integer deleteByTitle(String title);
}
