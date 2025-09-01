package com.inventory.model;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionLine {
    private UUID id;
    private UUID transactionId;
    private UUID itemId;
    private UUID locationId;
    private int qty;
    private ConditionStatus conditionStatus;
    private BigDecimal unitPrice;
    
    public enum ConditionStatus {
        USABLE, NOT_USABLE
    }
    
    // Constructors
    public TransactionLine() {
        this.id = UUID.randomUUID();
        this.conditionStatus = ConditionStatus.USABLE;
    }
    
    public TransactionLine(UUID itemId, int qty) {
        this();
        this.itemId = itemId;
        this.qty = qty;
    }
    
    public TransactionLine(UUID itemId, int qty, BigDecimal unitPrice) {
        this(itemId, qty);
        this.unitPrice = unitPrice;
    }
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public UUID getTransactionId() { return transactionId; }
    public void setTransactionId(UUID transactionId) { this.transactionId = transactionId; }
    
    public UUID getItemId() { return itemId; }
    public void setItemId(UUID itemId) { this.itemId = itemId; }
    
    public UUID getLocationId() { return locationId; }
    public void setLocationId(UUID locationId) { this.locationId = locationId; }
    
    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }
    
    public ConditionStatus getConditionStatus() { return conditionStatus; }
    public void setConditionStatus(ConditionStatus conditionStatus) { this.conditionStatus = conditionStatus; }
    
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    
    public boolean isUsable() {
        return conditionStatus == ConditionStatus.USABLE;
    }
    
    public boolean isNotUsable() {
        return conditionStatus == ConditionStatus.NOT_USABLE;
    }
} 