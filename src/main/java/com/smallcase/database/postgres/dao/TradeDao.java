package com.smallcase.database.postgres.dao;

import com.smallcase.database.postgres.entity.TradeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TradeDao extends CrudRepository<TradeEntity, Long> {

    Optional<TradeEntity> findByIdAndDeletedAt(Long tradeId, Integer deletedAt);

    List<TradeEntity> findAllByUserIdAndDeletedAt(Long userId, Integer deletedAt);

    List<TradeEntity> findAllByIdInAndDeletedAt(List<Long> tradeIds, Integer deletedAt);
}
