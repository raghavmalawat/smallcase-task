package com.smallcase.database.postgres.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.smallcase.enums.Status;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_securities")
@Data
public class UserSecurityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "security_id")
    private Long securityId;

    @Column(name = "security_type")
    private Character securityType;

    @Column(name = "status")
    private Character status;

    @Column(name = "created_at")
    private Integer createdAt;

    @Column(name = "updated_at")
    private Integer updatedAt;

    @Column(name = "current_quantity")
    private Long currentQuantity;

    @Column(name = "avg_price")
    private Double averagePrice;

}
