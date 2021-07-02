package com.smallcase.database.postgres.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "trades")
@Data
public class SecurityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    // TODO :: Generate SQL schema and create tables
    @Column(name = "security_type")
    private Character securityType;

    @Column(name = "name")
    private String name;

    @Column(name = "ticker_symbol")
    private String tickerSymbol;

    @Column(name = "deleted_at")
    private Integer deletedAt;

    @Column(name = "created_at")
    private Integer createdAt;

    @Column(name = "updated_at")
    private Integer updatedAt;

}
