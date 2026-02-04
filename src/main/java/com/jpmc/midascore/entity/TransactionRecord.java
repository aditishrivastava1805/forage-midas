package com.jpmc.midascore.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String senderId;
    private String recipientId;
    private BigDecimal amount;

    @Column(nullable = false)
    private float incentive;

    protected TransactionRecord() {}

    public TransactionRecord(
            String senderId,
            String recipientId,
            BigDecimal amount,
            float incentive
    ) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.amount = amount;
        this.incentive = incentive;
    }
}
