package com.inventory.controller;

import com.inventory.model.*;
import com.inventory.service.SupabaseService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.inventory.util.ConfigManager;
import com.inventory.model.Item;
import com.inventory.model.Transaction;

public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    
    // Services
    private final SupabaseService supabaseService = new SupabaseService(ConfigManager.getSupabaseUrl(), ConfigManager.getSupabaseApiKey());
    
    // UI Components
    @FXML private Label statusLabel;
    @FXML private TabPane mainTabPane;
    
    // Items Tab
    @FXML private TableView<Item> itemsTable;
    @FXML private TableColumn<Item, String> codeColumn;
    @FXML private TableColumn<Item, String> nameColumn;
    @FXML private TableColumn<Item, String> categoryColumn;
    @FXML private TableColumn<Item, String> unitColumn;
    @FXML private TableColumn<Item, Integer> minStockColumn;
    @FXML private TableColumn<Item, Boolean> palindromeColumn;
    @FXML private TableColumn<Item, Integer> currentStockColumn;
    @FXML private TextField searchField;
    
    // Receive Tab
    @FXML private TextField receiveReferenceField;
    @FXML private DatePicker receiveDatePicker;
    @FXML private ComboBox<Item> receiveItemComboBox;
    @FXML private TextField receiveQtyField;
    @FXML private TextField receivePriceField;
    @FXML private ComboBox<String> receiveLocationComboBox;
    @FXML private TextArea receiveNotesArea;
    
    // Issue Tab
    @FXML private TextField issueReferenceField;
    @FXML private DatePicker issueDatePicker;
    @FXML private ComboBox<Item> issueItemComboBox;
    @FXML private TextField issueQtyField;
    @FXML private ComboBox<String> issueLocationComboBox;
    @FXML private ComboBox<String> issueConditionComboBox;
    @FXML private TextArea issueNotesArea;
    
    // Reports Tab
    @FXML private DatePicker reportStartDate;
    @FXML private DatePicker reportEndDate;
    @FXML private TableView<SupabaseService.TransactionReportRow> reportTable;
    @FXML private TableColumn<SupabaseService.TransactionReportRow, LocalDate> reportDateColumn;
    @FXML private TableColumn<SupabaseService.TransactionReportRow, String> reportReferenceColumn;
    @FXML private TableColumn<SupabaseService.TransactionReportRow, String> reportTypeColumn;
    @FXML private TableColumn<SupabaseService.TransactionReportRow, String> reportItemColumn;
    @FXML private TableColumn<SupabaseService.TransactionReportRow, Integer> reportQtyColumn;
    @FXML private TableColumn<SupabaseService.TransactionReportRow, String> reportNotesColumn;
    
    // Settings Tab
    @FXML private CheckBox countNonUsableCheckBox;
    @FXML private CheckBox allowNegativeStockCheckBox;
    @FXML private TextField supabaseUrlField;
    @FXML private TextField supabaseApiKeyField;
    
    // Data
    private ObservableList<Item> itemsList = FXCollections.observableArrayList();
    private FilteredList<Item> filteredItems;
    
    @FXML
    public void initialize() {
        try {
            setupItemsTable();
            setupReceiveTab();
            setupIssueTab();
            setupReportsTab();
            setupSettingsTab();
            loadItems();
            updateStatus("Ready - Connected to Supabase");
        } catch (Exception e) {
            logger.error("Error initializing main controller", e);
            showError("Initialization Error", "Failed to initialize application: " + e.getMessage());
        }
    }
    
    private void setupItemsTable() {
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        minStockColumn.setCellValueFactory(new PropertyValueFactory<>("minStock"));
        palindromeColumn.setCellValueFactory(new PropertyValueFactory<>("palindrome"));
        currentStockColumn.setCellValueFactory(new PropertyValueFactory<>("currentStock"));
        
        // Add current stock column (computed)
        currentStockColumn.setCellValueFactory(cellData -> {
            Item item = cellData.getValue();
            try {
                int stock = supabaseService.getCurrentStock(item.getId());
                return new javafx.beans.property.SimpleIntegerProperty(stock).asObject();
            } catch (Exception e) {
                return new javafx.beans.property.SimpleIntegerProperty(0).asObject();
            }
        });
        
        filteredItems = new FilteredList<>(itemsList, p -> true);
        itemsTable.setItems(filteredItems);
        
        // Search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredItems.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return item.getItemCode().toLowerCase().contains(lowerCaseFilter) ||
                       item.getName().toLowerCase().contains(lowerCaseFilter) ||
                       item.getCategory().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }
    
    private void setupReceiveTab() {
        receiveDatePicker.setValue(LocalDate.now());
        receiveLocationComboBox.getItems().addAll("Main Warehouse", "Storage A", "Storage B");
        receiveLocationComboBox.setValue("Main Warehouse");
        
        // Load items into combo box
        receiveItemComboBox.setItems(itemsList);
        receiveItemComboBox.setCellFactory(param -> new ListCell<Item>() {
            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getItemCode() + " - " + item.getName());
                }
            }
        });
        receiveItemComboBox.setButtonCell(receiveItemComboBox.getCellFactory().call(null));
    }
    
    private void setupIssueTab() {
        issueDatePicker.setValue(LocalDate.now());
        issueLocationComboBox.getItems().addAll("Main Warehouse", "Storage A", "Storage B");
        issueLocationComboBox.setValue("Main Warehouse");
        issueConditionComboBox.getItems().addAll("USABLE", "NOT_USABLE");
        issueConditionComboBox.setValue("USABLE");
        
        // Load items into combo box
        issueItemComboBox.setItems(itemsList);
        issueItemComboBox.setCellFactory(param -> new ListCell<Item>() {
            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getItemCode() + " - " + item.getName());
                }
            }
        });
        issueItemComboBox.setButtonCell(issueItemComboBox.getCellFactory().call(null));
    }
    
    private void setupReportsTab() {
        reportStartDate.setValue(LocalDate.now().minusDays(7));
        reportEndDate.setValue(LocalDate.now());
        
        reportDateColumn.setCellValueFactory(new PropertyValueFactory<>("txDate"));
        reportReferenceColumn.setCellValueFactory(new PropertyValueFactory<>("reference"));
        reportTypeColumn.setCellValueFactory(new PropertyValueFactory<>("txType"));
        reportItemColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        reportQtyColumn.setCellValueFactory(new PropertyValueFactory<>("qty"));
        reportNotesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
    }
    
    private void setupSettingsTab() {
        countNonUsableCheckBox.setSelected(ConfigManager.isCountNonUsableAsOutgoing());
        allowNegativeStockCheckBox.setSelected(ConfigManager.isAllowNegativeStock());
        supabaseUrlField.setText(ConfigManager.getSupabaseUrl());
        supabaseApiKeyField.setText(ConfigManager.getSupabaseApiKey());
    }
    
    private void loadItems() {
        try {
            List<Item> items = supabaseService.getAllItems();
            itemsList.clear();
            itemsList.addAll(items);
            updateStatus("Loaded " + items.size() + " items from Supabase");
        } catch (Exception e) {
            logger.error("Error loading items", e);
            showError("Error", "Failed to load items from Supabase: " + e.getMessage());
        }
    }
    
    @FXML
    private void showAddItemDialog() {
        showItemDialog(null);
    }
    
    @FXML
    private void editSelectedItem() {
        Item selectedItem = itemsTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showWarning("No Selection", "Please select an item to edit.");
            return;
        }
        showItemDialog(selectedItem);
    }
    
    @FXML
    private void deleteSelectedItem() {
        Item selectedItem = itemsTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showWarning("No Selection", "Please select an item to delete.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Item");
        alert.setContentText("Are you sure you want to delete item: " + selectedItem.getItemCode() + "?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                supabaseService.deleteItem(selectedItem.getId());
                loadItems();
                updateStatus("Item deleted successfully");
            } catch (Exception e) {
                logger.error("Error deleting item", e);
                showError("Error", "Failed to delete item: " + e.getMessage());
            }
        }
    }
    
    private void showItemDialog(Item item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ItemDialog.fxml"));
            VBox dialogPane = loader.load();
            
            ItemDialogController controller = loader.getController();
            controller.setItem(item);
            
            Stage dialog = new Stage();
            dialog.setTitle(item == null ? "Add New Item" : "Edit Item");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(itemsTable.getScene().getWindow());
            dialog.setScene(new Scene(dialogPane));
            
            dialog.showAndWait();
            
            // Refresh items list
            loadItems();
            
        } catch (Exception e) {
            logger.error("Error showing item dialog", e);
            showError("Error", "Failed to show item dialog: " + e.getMessage());
        }
    }
    
    @FXML
    private void receiveItems() {
        try {
            // Validation
            if (receiveItemComboBox.getValue() == null) {
                showWarning("Validation Error", "Please select an item.");
                return;
            }
            
            String qtyText = receiveQtyField.getText();
            if (qtyText.isEmpty() || !qtyText.matches("\\d+")) {
                showWarning("Validation Error", "Please enter a valid quantity.");
                return;
            }
            
            int qty = Integer.parseInt(qtyText);
            if (qty <= 0) {
                showWarning("Validation Error", "Quantity must be greater than 0.");
                return;
            }
            
            // Create transaction
            Transaction transaction = new Transaction(Transaction.TransactionType.IN, 
                                                   receiveReferenceField.getText(), UUID.randomUUID()); // Default user ID
            transaction.setTxDate(receiveDatePicker.getValue());
            transaction.setNotes(receiveNotesArea.getText());
            
            // Create transaction line
            Item selectedItem = receiveItemComboBox.getValue();
            TransactionLine line = new TransactionLine(selectedItem.getId(), qty);
            
            String priceText = receivePriceField.getText();
            if (!priceText.isEmpty()) {
                try {
                    line.setUnitPrice(new BigDecimal(priceText));
                } catch (NumberFormatException e) {
                    showWarning("Validation Error", "Invalid unit price format.");
                    return;
                }
            }
            
            line.setLocationId(UUID.randomUUID()); // Default location ID
            transaction.addLine(line);
            
            // Save transaction via Supabase
            supabaseService.createTransaction(transaction);
            
            // Clear form
            clearReceiveForm();
            loadItems();
            
            updateStatus("Items received successfully via Supabase");
            showInfo("Success", "Items received successfully!");
            
        } catch (Exception e) {
            logger.error("Error receiving items", e);
            showError("Error", "Failed to receive items: " + e.getMessage());
        }
    }
    
    @FXML
    private void issueItems() {
        try {
            // Validation
            if (issueItemComboBox.getValue() == null) {
                showWarning("Validation Error", "Please select an item.");
                return;
            }
            
            String qtyText = issueQtyField.getText();
            if (qtyText.isEmpty() || !qtyText.matches("\\d+")) {
                showWarning("Validation Error", "Please enter a valid quantity.");
                return;
            }
            
            int qty = Integer.parseInt(qtyText);
            if (qty <= 0) {
                showWarning("Validation Error", "Quantity must be greater than 0.");
                return;
            }
            
            // Check stock availability
            Item selectedItem = issueItemComboBox.getValue();
            int currentStock = supabaseService.getCurrentStock(selectedItem.getId());
            if (currentStock < qty && !ConfigManager.isAllowNegativeStock()) {
                showWarning("Insufficient Stock", 
                           "Current stock: " + currentStock + ", Requested: " + qty);
                return;
            }
            
            // Determine transaction type based on condition
            Transaction.TransactionType txType = Transaction.TransactionType.OUT;
            if ("NOT_USABLE".equals(issueConditionComboBox.getValue()) && 
                ConfigManager.isCountNonUsableAsOutgoing()) {
                txType = Transaction.TransactionType.DISPOSITION;
            }
            
            // Create transaction
            Transaction transaction = new Transaction(txType, issueReferenceField.getText(), UUID.randomUUID());
            transaction.setTxDate(issueDatePicker.getValue());
            transaction.setNotes(issueNotesArea.getText());
            
            // Create transaction line
            TransactionLine line = new TransactionLine(selectedItem.getId(), qty);
            line.setLocationId(UUID.randomUUID()); // Default location ID
            line.setConditionStatus(TransactionLine.ConditionStatus.valueOf(
                issueConditionComboBox.getValue()));
            transaction.addLine(line);
            
            // Save transaction via Supabase
            supabaseService.createTransaction(transaction);
            
            // Clear form
            clearIssueForm();
            loadItems();
            
            updateStatus("Items issued successfully via Supabase");
            showInfo("Success", "Items issued successfully!");
            
        } catch (Exception e) {
            logger.error("Error issuing items", e);
            showError("Error", "Failed to issue items: " + e.getMessage());
        }
    }
    
    @FXML
    private void generateReport() {
        try {
            LocalDate startDate = reportStartDate.getValue();
            LocalDate endDate = reportEndDate.getValue();
            
            if (startDate == null || endDate == null) {
                showWarning("Validation Error", "Please select both start and end dates.");
                return;
            }
            
            if (startDate.isAfter(endDate)) {
                showWarning("Validation Error", "Start date cannot be after end date.");
                return;
            }
            
            List<SupabaseService.TransactionReportRow> reportRows = supabaseService.getTransactionReport(startDate, endDate);
            
            reportTable.setItems(FXCollections.observableArrayList(reportRows));
            updateStatus("Report generated from Supabase: " + reportRows.size() + " transactions");
            
        } catch (Exception e) {
            logger.error("Error generating report", e);
            showError("Error", "Failed to generate report from Supabase: " + e.getMessage());
        }
    }
    
    @FXML
    private void exportToCSV() {
        // Implementation for CSV export
        showInfo("Info", "CSV export functionality will be implemented.");
    }
    
    @FXML
    private void saveSettings() {
        try {
            ConfigManager.setProperty("count_non_usable_as_outgoing", countNonUsableCheckBox.isSelected());
            ConfigManager.setProperty("allow_negative_stock", allowNegativeStockCheckBox.isSelected());
            ConfigManager.setProperty("supabase.url", supabaseUrlField.getText());
            ConfigManager.setProperty("supabase.api_key", supabaseApiKeyField.getText());
            
            updateStatus("Settings saved successfully");
            showInfo("Success", "Settings saved successfully!");
            
        } catch (Exception e) {
            logger.error("Error saving settings", e);
            showError("Error", "Failed to save settings: " + e.getMessage());
        }
    }
    
    @FXML
    private void testSupabaseConnection() {
        try {
            // Test connection by trying to fetch items
            List<Item> items = supabaseService.getAllItems();
            showInfo("Connection Test", "Successfully connected to Supabase! Found " + items.size() + " items.");
            updateStatus("Supabase connection test successful");
        } catch (Exception e) {
            showError("Connection Test Failed", "Could not connect to Supabase: " + e.getMessage());
            updateStatus("Supabase connection test failed");
        }
    }
    
    private void clearReceiveForm() {
        receiveReferenceField.clear();
        receiveQtyField.clear();
        receivePriceField.clear();
        receiveNotesArea.clear();
        receiveItemComboBox.setValue(null);
    }
    
    private void clearIssueForm() {
        issueReferenceField.clear();
        issueQtyField.clear();
        issueNotesArea.clear();
        issueItemComboBox.setValue(null);
    }
    
    private void updateStatus(String message) {
        statusLabel.setText(message);
        logger.info("Status: {}", message);
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}