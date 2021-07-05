package com.smallcase.repository;

import com.smallcase.LogFactory;
import com.smallcase.database.postgres.dao.TradeDao;
import com.smallcase.database.postgres.entity.TradeEntity;
import com.smallcase.domainentity.Trade;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TradeRepository implements DomainPersistence<Trade> {

    private static final Logger logger = LogFactory.getLogger(TradeRepository.class);

    @Autowired
    TradeDao tradeDao;

    @Override
    public Object add(Trade trade) {
        TradeEntity tradeEntity = convertTradeToTradeEntity(trade);
        TradeEntity tradeEntityFromDB =  tradeDao.save(tradeEntity);

        if (Objects.nonNull(tradeEntityFromDB.getId()))
            logger.info("Successfully saved trade entry in DB");

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
    public void update(Trade trade) {
        TradeEntity tradeEntity = convertTradeToTradeEntity(trade);
        TradeEntity tradeEntityFromDB = tradeDao.save(tradeEntity);

        if (Objects.nonNull(tradeEntityFromDB.getId()))
            logger.info("Successfully saved trade entry in DB");
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

        logger.info("Successfully fetched trades from DB");
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
