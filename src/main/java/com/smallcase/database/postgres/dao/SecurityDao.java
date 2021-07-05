package com.smallcase.database.postgres.dao;

import com.smallcase.database.postgres.entity.SecurityEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecurityDao extends CrudRepository<SecurityEntity, Long> {

    List<SecurityEntity> findAllByDeletedAt(Integer deletedAt);

    List<SecurityEntity> findAllByIdInAndDeletedAt(List<Long> securityIds, Integer deletedAt);

}
