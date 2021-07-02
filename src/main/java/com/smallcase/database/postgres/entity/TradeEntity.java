package com.smallcase.database.postgres.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "trades")
@Data
public class TradeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(name = "trade_id")
    private Long tradeId;

    @Column(name = "trade_type")
    private Character tradeType;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "security_type")
    private Character securityType;

    @Column(name = "security_id")
    private Long securityId;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "price")
    private Double price;

    @Column(name = "deleted_at")
    private Integer deletedAt;

    @Column(name = "created_at")
    private Integer createdAt;

    @Column(name = "updated_at")
    private Integer updatedAt;

}
