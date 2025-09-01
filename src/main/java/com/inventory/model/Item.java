package com.inventory.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Item {
    private UUID id;
    private String itemCode;
    private String name;
    private String category;
    private String unit;
    private int minStock;
    private boolean isPalindrome;
    private boolean isActive;
    private LocalDateTime createdAt;
    
    // Constructors
    public Item() {}
    
    public Item(String itemCode, String name, String category, String unit, int minStock) {
        this.id = UUID.randomUUID();
        this.itemCode = itemCode;
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.minStock = minStock;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    
    public int getMinStock() { return minStock; }
    public void setMinStock(int minStock) { this.minStock = minStock; }
    
    public boolean isPalindrome() { return isPalindrome; }
    public void setPalindrome(boolean palindrome) { isPalindrome = palindrome; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return itemCode + " - " + name;
    }
} 