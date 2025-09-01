package com.inventory.controller;

import com.inventory.model.Item;
import com.inventory.service.SupabaseService;
import com.inventory.util.PalindromeUtil;
import com.inventory.util.ConfigManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDateTime;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemDialogController {
    private static final Logger logger = LoggerFactory.getLogger(ItemDialogController.class);
    
    @FXML private TextField itemCodeField;
    @FXML private TextField nameField;
    @FXML private TextField categoryField;
    @FXML private TextField unitField;
    @FXML private TextField minStockField;
    @FXML private Label palindromeLabel;
    
    private Item item;
    private final SupabaseService supabaseService = new SupabaseService(ConfigManager.getSupabaseUrl(), ConfigManager.getSupabaseApiKey());
    
    public void setItem(Item item) {
        this.item = item;
        if (item != null) {
            // Edit mode
            itemCodeField.setText(item.getItemCode());
            nameField.setText(item.getName());
            categoryField.setText(item.getCategory());
            unitField.setText(item.getUnit());
            minStockField.setText(String.valueOf(item.getMinStock()));
            itemCodeField.setDisable(true); // Don't allow editing item code
            updatePalindromeLabel(item.getName());
        } else {
            // Add mode
            itemCodeField.setDisable(false);
            updatePalindromeLabel("");
        }
    }
    
    @FXML
    private void checkPalindrome() {
        String name = nameField.getText();
        updatePalindromeLabel(name);
    }
    
    private void updatePalindromeLabel(String name) {
        if (name == null || name.trim().isEmpty()) {
            palindromeLabel.setText("Palindrome: NO");
            palindromeLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        } else {
            boolean isPalindrome = PalindromeUtil.isPalindrome(name);
            palindromeLabel.setText(PalindromeUtil.getPalindromeStatus(name));
            if (isPalindrome) {
                palindromeLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
            } else {
                palindromeLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
            }
        }
    }
    
    @FXML
    private void save() {
        try {
            // Validation
            if (itemCodeField.getText().trim().isEmpty()) {
                showError("Validation Error", "Item code is required.");
                return;
            }
            
            if (nameField.getText().trim().isEmpty()) {
                showError("Validation Error", "Item name is required.");
                return;
            }
            
            String minStockText = minStockField.getText().trim();
            int minStock = 0;
            if (!minStockText.isEmpty()) {
                try {
                    minStock = Integer.parseInt(minStockText);
                    if (minStock < 0) {
                        showError("Validation Error", "Minimum stock cannot be negative.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    showError("Validation Error", "Invalid minimum stock value.");
                    return;
                }
            }
            
            // Create or update item
            if (item == null) {
                // New item
                item = new Item();
                item.setId(UUID.randomUUID());
                item.setCreatedAt(LocalDateTime.now());
            }
            
            item.setItemCode(itemCodeField.getText().trim());
            item.setName(nameField.getText().trim());
            item.setCategory(categoryField.getText().trim());
            item.setUnit(unitField.getText().trim());
            item.setMinStock(minStock);
            item.setPalindrome(PalindromeUtil.isPalindrome(item.getName()));
            item.setActive(true);
            
            // Save to Supabase
            if (item.getId() == null) {
                supabaseService.createItem(item);
            } else {
                supabaseService.updateItem(item);
            }
            
            // Close dialog
            closeDialog();
            
        } catch (Exception e) {
            logger.error("Error saving item", e);
            showError("Error", "Failed to save item to Supabase: " + e.getMessage());
        }
    }
    
    @FXML
    private void cancel() {
        closeDialog();
    }
    
    private void closeDialog() {
        Stage stage = (Stage) itemCodeField.getScene().getWindow();
        stage.close();
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}