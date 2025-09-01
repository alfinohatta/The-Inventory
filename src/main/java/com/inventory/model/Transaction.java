package com.inventory.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class Transaction {
    private UUID id;
    private TransactionType txType;
    private LocalDate txDate;
    private String reference;
    private UUID createdBy;
    private String notes;
    private LocalDateTime createdAt;
    private List<TransactionLine> lines;
    
    public enum TransactionType {
        IN, OUT, ADJUST, DISPOSITION
    }
    
    // Constructors
    public Transaction() {
        this.id = UUID.randomUUID();
        this.lines = new ArrayList<>();
        this.txDate = LocalDate.now();
        this.createdAt = LocalDateTime.now();
    }
    
    public Transaction(TransactionType txType, String reference, UUID createdBy) {
        this();
        this.txType = txType;
        this.reference = reference;
        this.createdBy = createdBy;
    }
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public TransactionType getTxType() { return txType; }
    public void setTxType(TransactionType txType) { this.txType = txType; }
    
    public LocalDate getTxDate() { return txDate; }
    public void setTxDate(LocalDate txDate) { this.txDate = txDate; }
    
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    
    public UUID getCreatedBy() { return createdBy; }
    public void setCreatedBy(UUID createdBy) { this.createdBy = createdBy; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public List<TransactionLine> getLines() { return lines; }
    public void setLines(List<TransactionLine> lines) { this.lines = lines; }
    
    public void addLine(TransactionLine line) {
        this.lines.add(line);
    }
    
    public boolean isInbound() {
        return txType == TransactionType.IN;
    }
    
    public boolean isOutbound() {
        return txType == TransactionType.OUT || txType == TransactionType.DISPOSITION;
    }
} 