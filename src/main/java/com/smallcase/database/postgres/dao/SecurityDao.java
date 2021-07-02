package com.smallcase.database.postgres.dao;

import com.smallcase.database.postgres.entity.SecurityEntity;
import org.springframework.data.repository.CrudRepository;

public interface SecurityDao extends CrudRepository<SecurityEntity, Long> {
}
