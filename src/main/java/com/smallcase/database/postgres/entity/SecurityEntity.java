package com.smallcase.database.postgres.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "securities")
@Data
public class SecurityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

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
