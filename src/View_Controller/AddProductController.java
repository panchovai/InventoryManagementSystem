/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Inventory;
import static Model.Inventory.getPartInventory;
import Model.Part;
import Model.Product;
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
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author francisco
 */
public class AddProductController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private int productID;
    private ObservableList<Part> currentParts = FXCollections.observableArrayList();
    private String exceptionMessage = new String();

    @FXML
    private TextField addProductSearchTextField;

    @FXML
    private TextField addProductIDTextField;

    @FXML
    private TextField addProductNameTextField;

    @FXML
    private TextField addProductInvTextField;

    @FXML
    private TextField addProductPriceTextField;

    @FXML
    private TextField addProductMaxTextField;

    @FXML
    private TextField addProductMinTextField;

    //End of TextFields, buttons next
    @FXML
    Button addProductSearchButton;
    @FXML
    Button addProductAddButton;
    @FXML
    Button addProductDeleteButton;
    @FXML
    Button addProductSaveButton;
    @FXML
    Button addProductCancelButton;

    //Tableview columns  modeling Add Product First
    @FXML
    private TableView<Part> addProductAddProductTableView;

    //addProductTableView
    @FXML
    private TableColumn<Part, Integer> addProductAddProductPartIDColumn;

    @FXML
    private TableColumn<Part, String> addProductAddProductPartNameColumn;

    @FXML
    private TableColumn<Part, Integer> addProductAddProductInventoryLevelColumn;

    @FXML
    private TableColumn<Part, Double> addProductAddProductPricePerUnitColumn;

    //Modeling Tableview Delete Product 
    @FXML
    private TableView<Part> associatedPartTableView; //Adding part to product

    @FXML
    private TableColumn<Part, Integer> addProductDeleteProductPartIDColumn;

    @FXML
    private TableColumn<Part, String> addProductDeleteProductPartNameColumn;

    @FXML
    private TableColumn<Part, Integer> addProductDeleteProductInventoryLevelColumn;

    @FXML
    private TableColumn<Part, Double> addProductDeleteProductPricePerUnitColumn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Add each column to user generated fields
        //populate product column
        addProductAddProductPartIDColumn.setCellValueFactory(cellData
                -> cellData.getValue().partIDProperty().asObject());
        addProductAddProductPartNameColumn.setCellValueFactory(cellData
                -> cellData.getValue().partNameProperty());
        addProductAddProductInventoryLevelColumn.setCellValueFactory(cellData
                -> cellData.getValue().partInvProperty().asObject());
        addProductAddProductPricePerUnitColumn.setCellValueFactory(cellData
                -> cellData.getValue().partPriceProperty().asObject());

        addProductDeleteProductPartIDColumn.setCellValueFactory(x -> x.getValue().partIDProperty().asObject());
        addProductDeleteProductPartNameColumn.setCellValueFactory(cellData -> cellData.getValue().partNameProperty());
        addProductDeleteProductInventoryLevelColumn.setCellValueFactory(cellData
                -> cellData.getValue().partInvProperty().asObject());
        addProductDeleteProductPricePerUnitColumn.setCellValueFactory(cellData
                -> cellData.getValue().partPriceProperty().asObject());

        updatePartTableView();
        updateAssociatedPartTableView();

        productID = Inventory.getProductIDCount();
        addProductIDTextField.setText("AUTO GEN: " + productID);

    }

    //Add Product using the Add button
    @FXML
    void addPartToProduct(ActionEvent event) {
        Part part = addProductAddProductTableView.getSelectionModel().getSelectedItem();
        currentParts.add(part);
        updateAssociatedPartTableView();
    }

    @FXML
    void deletePart(ActionEvent event) {
        Part part = associatedPartTableView.getSelectionModel().getSelectedItem();
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
    void handleAddProductSave(ActionEvent event) throws IOException {
        String addProductName = addProductNameTextField.getText();
        String addProductInv = addProductInvTextField.getText();
        String addProductPrice = addProductPriceTextField.getText();
        String addProductMax = addProductMaxTextField.getText();
        String addProductMin = addProductMinTextField.getText();

        try {
            exceptionMessage = Product.validateProduct(addProductName,
                    Integer.parseInt(addProductMin),
                    Integer.parseInt(addProductMax),
                    Integer.parseInt(addProductInv),
                    Double.parseDouble(addProductPrice),
                    currentParts, exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error Adding Product");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            } else {
                System.out.println("Product" + addProductName + " was saved");
                Product newProduct = new Product();
                newProduct.setProductID(productID);
                newProduct.setProductName(addProductName);
                newProduct.setProductInStock(Integer.parseInt(addProductInv));
                newProduct.setProductPrice(Double.parseDouble(addProductPrice));
                newProduct.setProductMin(Integer.parseInt(addProductMin));
                newProduct.setProductMax(Integer.parseInt(addProductMax));
                newProduct.setProductParts(currentParts);
                Inventory.addProduct(newProduct);

                Parent addProductSaveParent = FXMLLoader.load(getClass().
                        getResource("FJerezMainScreen.fxml"));
                Scene scene = new Scene(addProductSaveParent);
                Stage window = (Stage) ((Node) event.getSource()).
                        getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error Adding Product");
            alert.setContentText("Invalid Entry, Check All Required Fields");
            alert.showAndWait();
        }
    }

    //Constructor
    public void updatePartTableView() {
        addProductAddProductTableView.setItems(getPartInventory());
        //refresh or show changes?

    }

    public void updateAssociatedPartTableView() {
        associatedPartTableView.setItems(currentParts);
    }

    @FXML
    void AddProductCancelledAction(ActionEvent e) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Are you sure you want to cancel adding a product?");
        alert.setHeaderText("Confirm");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent addProductCancel = FXMLLoader.load(getClass().getResource("FJerezMainScreen.fxml"));
            Scene scene = new Scene(addProductCancel);
            Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            System.out.println("You Cancelled Adding a Product");
        }

    }

}
