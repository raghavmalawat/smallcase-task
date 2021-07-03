package com.smallcase.repository;

import com.smallcase.database.postgres.dao.TradeDao;
import com.smallcase.database.postgres.entity.TradeEntity;
import com.smallcase.domainentity.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeRepository implements DomainPersistence<Trade> {

    @Autowired
    TradeDao tradeDao;

    @Override
    public Object add(Trade trade) {
        TradeEntity tradeEntity = convertTradeToTradeEntity(trade);
        TradeEntity tradeEntityFromDB =  tradeDao.save(tradeEntity);
        return tradeEntityFromDB.getId();
    }

    @Override
    public Trade get(Trade trade) {
        return null;
    }

    @Override
    public void delete(Trade trade) {

    }

    @Override
    public Trade update(Trade trade) {
        return null;
    }


    private TradeEntity convertTradeToTradeEntity(Trade trade) {
        TradeEntity tradeEntity = new TradeEntity();

        tradeEntity.setId(trade.getTradeId());
        tradeEntity.setTradeType(trade.getTradeType().getType());
        tradeEntity.setUserId(trade.getUserId());
        tradeEntity.setSecurityId(trade.getSecurity().getSecurityId());
        tradeEntity.setSecurityType(trade.getSecurity().getSecurityType().getType());
        tradeEntity.setQuantity(trade.getQuantity());
        tradeEntity.setPrice(trade.getPrice());
        tradeEntity.setCreatedAt(trade.getCreatedAt());
        tradeEntity.setUpdatedAt(trade.getUpdatedAt());
        tradeEntity.setDeletedAt(trade.getDeletedAt());

        return tradeEntity;
    }
}
