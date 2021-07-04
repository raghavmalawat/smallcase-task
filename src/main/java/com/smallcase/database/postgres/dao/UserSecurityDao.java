package com.smallcase.database.postgres.dao;

import com.smallcase.database.postgres.entity.UserSecurityEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSecurityDao extends CrudRepository<UserSecurityEntity, Long> {

    Optional<UserSecurityEntity> findByUserIdAndSecurityIdAndStatus(Long userId, Long securityId, Character status);

    Optional<UserSecurityEntity> findByUserIdAndSecurityId(Long userId, Long securityId);
}
