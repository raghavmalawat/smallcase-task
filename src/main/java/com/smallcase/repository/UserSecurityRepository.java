package com.smallcase.repository;

import com.smallcase.LogFactory;
import com.smallcase.database.postgres.dao.UserSecurityDao;
import com.smallcase.database.postgres.entity.UserSecurityEntity;
import com.smallcase.domainentity.UserSecurity;
import com.smallcase.enums.Status;
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
public class UserSecurityRepository implements DomainPersistence<UserSecurity> {

    private static final Logger logger = LogFactory.getLogger(UserSecurityRepository.class);

    @Autowired
    UserSecurityDao userSecurityDao;

    @Override
    public Object add(UserSecurity userSecurity) {
        UserSecurityEntity userSecurityEntity = convertUserSecurityToUserSecurityEntity(userSecurity);
        UserSecurityEntity userSecurityEntityFromDB = userSecurityDao.save(userSecurityEntity);

        if (Objects.nonNull(userSecurityEntityFromDB.getId()))
            logger.info("Successfully saved user security holding in DB");

        return userSecurityEntityFromDB.getId();
    }

    @Override
    public UserSecurity get(UserSecurity userSecurity) {
        Optional<UserSecurityEntity> userSecurityEntity = userSecurityDao.findByUserIdAndSecurityId(userSecurity.getUserId(), userSecurity.getSecurity().getSecurityId());
        return userSecurityEntity.map(UserSecurity::new).orElse(null);

    }

    @Override
    public void delete(UserSecurity userSecurity) {

    }

    @Override
    public void update(UserSecurity userSecurity) {
        UserSecurityEntity userSecurityEntity = convertUserSecurityToUserSecurityEntity(userSecurity);
        UserSecurityEntity userSecurityEntityFromDB = userSecurityDao.save(userSecurityEntity);

        if (Objects.nonNull(userSecurityEntityFromDB.getId()))
            logger.info("Successfully saved user security holding in DB");
    }

    @Override
    public List<UserSecurity> bulkGet(Long userId, List<UserSecurity> userSecurities) {
        List<UserSecurity> filteredResult = new ArrayList<>();
        List<UserSecurityEntity> userSecuritiesFromDB = null;

        if (CollectionUtils.isEmpty(userSecurities))
            userSecuritiesFromDB = userSecurityDao.findAllByUserIdAndStatus(userId, Status.ACTIVE.getType());

        if (Objects.nonNull(userSecuritiesFromDB))
            filteredResult = userSecuritiesFromDB.stream().map(UserSecurity::new).collect(Collectors.toList());

        logger.info("Successfully fetched user securities holdings from DB");
        return filteredResult;
    }

    private UserSecurityEntity convertUserSecurityToUserSecurityEntity(UserSecurity userSecurity) {
        UserSecurityEntity userSecurityEntity = new UserSecurityEntity();

        userSecurityEntity.setId(userSecurity.getUserSecurityId());
        userSecurityEntity.setAveragePrice(userSecurity.getAveragePrice());
        userSecurityEntity.setCurrentQuantity(userSecurity.getCurrentQuantity());
        userSecurityEntity.setCreatedAt(userSecurity.getCreatedAt());
        userSecurityEntity.setStatus(userSecurity.getStatus().getType());
        userSecurityEntity.setUserId(userSecurity.getUserId());
        userSecurityEntity.setUpdatedAt(userSecurity.getUpdatedAt());
        userSecurityEntity.setSecurityId(userSecurity.getSecurity().getSecurityId());
        userSecurityEntity.setSecurityType(userSecurity.getSecurity().getSecurityType().getType());

        return userSecurityEntity;
    }
}
