/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

/**
 * FXML Controller class
 *
 * @author francisco
 */
import Model.Inventory;
import static Model.Inventory.getPartInventory;
import static Model.Inventory.getProductInventory;
import Model.Part;
import Model.Product;
import static View_Controller.FJerezMainScreenController.productToModifyIndex;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author francisco
 */
public class ModifyProductController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private ObservableList<Part> currentParts = FXCollections.observableArrayList();
    private int productIndex = productToModifyIndex();
    private String exceptionMessage = new String();
    private int productID;

    @FXML
    private TextField modifyProductSearchTextField;

    @FXML
    private TextField modifyProductIDTextField;

    @FXML
    private TextField modifyProductNameTextField;

    @FXML
    private TextField modifyProductInvTextField;

    @FXML
    private TextField modifyProductPriceTextField;

    @FXML
    private TextField modifyProductMaxTextField;

    @FXML
    private TextField modifyProductMinTextField;

    //End of TextFields, 
    //buttons
    @FXML
    Button modifyProductSearchButton;
    @FXML
    Button modifyProductAddButton;
    @FXML
    Button modifyProductDeleteButton;
    @FXML
    Button modifyProductSaveButton;
    @FXML
    Button modifyProductCancelButton;

    //Tableview columns  modeling Add Product First
    @FXML
    private TableView<Part> modifyProductTableView;

    //modifyProductTableView
    @FXML
    private TableColumn<Part, Integer> modifyProductPartIDColumn;

    @FXML
    private TableColumn<Part, String> modifyProductPartNameColumn;

    @FXML
    private TableColumn<Part, Integer> modifyProductInventoryLevelColumn;

    @FXML
    private TableColumn<Part, Double> modifyProductPricePerUnitColumn;

    //Modeling Tableview Delete Product 
    @FXML
    private TableView<Part> modifyProductDeleteProductTableView; //DeleteProductTableView

    @FXML
    private TableColumn<Part, Integer> modifyProductDeleteProductPartIDColumn;

    @FXML
    private TableColumn<Part, String> modifyProductDeleteProductPartNameColumn;

    @FXML
    private TableColumn<Part, Integer> modifyProductDeleteProductInventoryLevelColumn;

    @FXML
    private TableColumn<Part, Double> modifyProductDeleteProductPricePerUnitColumn;

    @FXML
    void modifyProductCancelledAction(ActionEvent e) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Are you sure you want to cancel?");
        alert.setHeaderText("Confirm");
        alert.setContentText("Are you sure you want to cancel Modifying a Product?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent addProductCancel = FXMLLoader.load(getClass().getResource("FJerezMainScreen.fxml"));
            Scene scene = new Scene(addProductCancel);
            Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            System.out.println("You Cancelled Modifying a Product");
        }

    }

    @FXML
    void handleClearSearch(ActionEvent event) {
        updateAddPartsTableView();
        modifyProductSearchTextField.setText("");
    }

    @FXML
    void handleSearch(ActionEvent event) {
        String searchPart = modifyProductSearchTextField.getText();
        int partIndex = 0;
        if (Inventory.lookupPart(searchPart) == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Part not found");
            alert.setContentText("The search term entered does not match any known parts.");
            alert.showAndWait();
        } else {
            partIndex = Inventory.lookupPart(searchPart);
            Part tempPart = Inventory.getPartInventory().get(partIndex);
            ObservableList<Part> tempPartList = FXCollections.observableArrayList();
            tempPartList.add(tempPart);
            modifyProductTableView.setItems(tempPartList);
        }
    }

    @FXML
    void handleAdd(ActionEvent event) {
        Part part = modifyProductTableView.getSelectionModel().getSelectedItem();
        currentParts.add(part);
        updateDeletePartsTableView();
    }

    @FXML
    void deletePart(ActionEvent event) {
        Part part = modifyProductDeleteProductTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Are you sure you want to delete this Part?");
        alert.setHeaderText("Confirm");
        alert.setContentText("Are you sure you want to delete " + part.getPartName() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            currentParts.remove(part);
        } else {
            System.out.println("Part was not removed");
        }
    }

    @FXML
    private void handleModifyProductSave(ActionEvent event) throws IOException {
        String productName = modifyProductNameTextField.getText();
        String productInv = modifyProductInvTextField.getText();
        String productPrice = modifyProductPriceTextField.getText();
        String productMin = modifyProductMinTextField.getText();
        String productMax = modifyProductMaxTextField.getText();

        try {
            exceptionMessage = Product.validateProduct(productName, Integer.parseInt(productMin),
                    Integer.parseInt(productMax), Integer.parseInt(productInv),
                    Double.parseDouble(productPrice), currentParts, exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error Modifying Product");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            } else {
                System.out.println("Product name: " + productName);
                Product newSavedProduct = new Product();
                newSavedProduct.setProductID(productID);
                newSavedProduct.setProductName(productName);
                newSavedProduct.setProductInStock(Integer.parseInt(productInv));
                newSavedProduct.setProductPrice(Double.parseDouble(productPrice));
                newSavedProduct.setProductMin(Integer.parseInt(productMin));
                newSavedProduct.setProductMax(Integer.parseInt(productMax));
                newSavedProduct.setProductParts(currentParts);
                Inventory.updateProduct(productIndex, newSavedProduct);

                Parent modifyProductSaveParent = FXMLLoader.load(getClass().getResource("FJerezMainScreen.fxml"));
                Scene scene = new Scene(modifyProductSaveParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error Modifying Product");
            alert.setContentText("Invalid Entry, Check All Required Fields");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Product product = getProductInventory().get(productIndex);
        productID = getProductInventory().get(productIndex).getProductID();

        modifyProductIDTextField.setText("Auto-Gen: " + productID);
        modifyProductNameTextField.setText(product.getProductName());
        modifyProductInvTextField.setText(Integer.toString(product.getProductInStock()));
        modifyProductPriceTextField.setText(Double.toString(product.getProductPrice()));
        modifyProductMinTextField.setText(Integer.toString(product.getProductMin()));
        modifyProductMaxTextField.setText(Integer.toString(product.getProductMax()));

        currentParts = product.getProductParts();

        modifyProductPartIDColumn.setCellValueFactory(x
                -> x.getValue().partIDProperty().asObject());
        modifyProductPartNameColumn.setCellValueFactory(x
                -> x.getValue().partNameProperty());

        modifyProductInventoryLevelColumn.setCellValueFactory(x
                -> x.getValue().partInvProperty().asObject());
        modifyProductPricePerUnitColumn.setCellValueFactory(x
                -> x.getValue().partPriceProperty().asObject());

        modifyProductDeleteProductPartIDColumn.setCellValueFactory(x
                -> x.getValue().partIDProperty().asObject());
        modifyProductDeleteProductPartNameColumn.setCellValueFactory(x
                -> x.getValue().partNameProperty());
        modifyProductDeleteProductInventoryLevelColumn.setCellValueFactory(x
                -> x.getValue().partInvProperty().asObject());
        modifyProductDeleteProductPricePerUnitColumn.setCellValueFactory(x
                -> x.getValue().partPriceProperty().asObject());

        updateAddPartsTableView();
        updateDeletePartsTableView();
    }

    public void updateAddPartsTableView() {
        modifyProductTableView.setItems(getPartInventory());
    }

    public void updateDeletePartsTableView() {
        modifyProductDeleteProductTableView.setItems(currentParts);
    }
}
