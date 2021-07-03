package com.smallcase.repository;

import com.smallcase.database.postgres.dao.TradeDao;
import com.smallcase.database.postgres.entity.TradeEntity;
import com.smallcase.domainentity.Security;
import com.smallcase.domainentity.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

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
        Optional<TradeEntity> tradeFromDB = tradeDao.findByIdAndDeletedAt(trade.getTradeId(), 0);
        return tradeFromDB.map(Trade::new).orElse(null);
    }

    @Override
    public void delete(Trade trade) {

    }

    @Override
    public Trade update(Trade trade) {
        return null;
    }

    @Override
    public List<Trade> bulkGet(Long userId, List<Trade> trades) {
        List<Trade> filteredResult = new ArrayList<>();
        List<Long> tradeIds = trades.stream().map(Trade::getTradeId).collect(Collectors.toList());
        List<TradeEntity> tradesFromDB;

        if (CollectionUtils.isEmpty(tradeIds))
            tradesFromDB = tradeDao.findAllByUserIdAndDeletedAt(userId, 0);
        else
            tradesFromDB = tradeDao.findAllByIdInAndDeletedAt(tradeIds, 0);

        if (Objects.nonNull(tradesFromDB))
            filteredResult = tradesFromDB.stream().map(Trade::new).collect(Collectors.toList());

        return filteredResult;
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
