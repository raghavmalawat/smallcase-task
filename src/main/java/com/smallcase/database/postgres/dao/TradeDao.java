package com.smallcase.database.postgres.dao;

import com.smallcase.database.postgres.entity.TradeEntity;
import org.springframework.data.repository.CrudRepository;

public interface TradeDao extends CrudRepository<TradeEntity, Long> {
}
