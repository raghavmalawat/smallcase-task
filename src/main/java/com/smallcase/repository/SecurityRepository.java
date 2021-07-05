package com.smallcase.repository;

import com.smallcase.LogFactory;
import com.smallcase.database.postgres.dao.SecurityDao;
import com.smallcase.database.postgres.entity.SecurityEntity;
import com.smallcase.domainentity.Security;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SecurityRepository implements DomainPersistence<Security>  {

    private static final Logger logger = LogFactory.getLogger(SecurityRepository.class);

    @Autowired
    SecurityDao securityDao;

    @Override
    public Object add(Security security) {
        SecurityEntity securityEntity = convertSecurityToSecurityEntity(security);
        SecurityEntity securityEntityFromDB = securityDao.save(securityEntity);

        if (Objects.nonNull(securityEntityFromDB.getId()))
            logger.info("Successfully saved security in DB");

        return securityEntityFromDB.getId();
    }

    @Override
    public Security get(Security security) {
        return null;
    }

    @Override
    public void delete(Security security) {

    }

    @Override
    public void update(Security security) {

    }

    @Override
    public List<Security> bulkGet(Long userId, List<Security> securities) {
        List<Security> filteredResult = new ArrayList<>();
        List<Long> securityIds = securities.stream().map(Security::getSecurityId).collect(Collectors.toList());
        List<SecurityEntity> securitiesFromDB;

        if (CollectionUtils.isEmpty(securityIds))
            securitiesFromDB = securityDao.findAllByDeletedAt(0);
        else
            securitiesFromDB = securityDao.findAllByIdInAndDeletedAt(securityIds, 0);

        if (Objects.nonNull(securitiesFromDB))
            filteredResult = securitiesFromDB.stream().map(Security::new).collect(Collectors.toList());

        logger.info("Successfully fetched securities from DB");
        return filteredResult;
    }

    private SecurityEntity convertSecurityToSecurityEntity(Security security) {
        SecurityEntity securityEntity = new SecurityEntity();

        securityEntity.setId(security.getSecurityId());
        securityEntity.setSecurityType(security.getSecurityType().getType());
        securityEntity.setName(security.getSecurityName());
        securityEntity.setTickerSymbol(security.getTickerSymbol());
        securityEntity.setCreatedAt(security.getCreatedAt());
        securityEntity.setUpdatedAt(security.getUpdatedAt());
        securityEntity.setDeletedAt(security.getDeletedAt());

        return securityEntity;
    }
}
