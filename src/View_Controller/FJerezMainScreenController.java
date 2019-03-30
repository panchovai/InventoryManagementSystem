/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Inventory;
import static Model.Inventory.deletePart;
import static Model.Inventory.getPartInventory;
import static Model.Inventory.getProductInventory;
import static Model.Inventory.removeProduct;
import static Model.Inventory.validateProductDelete;
import Model.Part;
import Model.Product;
import franciscojerezc482.FranciscoJerezC482;
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
public class FJerezMainScreenController implements Initializable {

    private static Part partToModify; //create a static part in the main class
    private static int partToModifyIndex;
    private static Product productToModify; //create a static part in the main class
    String partSearchText;
    String productSearchText;
    private static int modifyProductIndex;

    public static int partToModifyIndex() {

        return partToModifyIndex;
    }

    // Part Modeling 
    @FXML

    private TextField mainScreenPartsSearchTextField;

    @FXML
    Button mainScreenPartsSearchButton;

    @FXML
    Button mainScreenPartClearButton;

    @FXML
    Button mainScreenPartAddButton;

    @FXML
    Button mainScreenPartModifyButton;

    @FXML
    Button mainScreenPartDeleteButton;

    @FXML
    private TableView<Part> mainScreenPartTableView;

    @FXML
    private TableColumn<Part, Integer> mainPartIDColumn;

    @FXML

    private TableColumn<Part, String> mainPartNameColumn;

    @FXML

    private TableColumn<Part, Integer> mainPartInventoryLevel;

    @FXML

    private TableColumn<Part, Double> mainPartPriceCostPerUnit;

    //Product Modeling
    @FXML

    private TextField mainScreenProductSearchTextField;

    @FXML
    Button mainScreenProductSearchButton;

    @FXML
    Button mainScreenProductClearButton;

    @FXML
    Button mainScreenProductAddButton;

    @FXML
    Button mainScreenProductModifyButton;

    @FXML
    Button mainScreenProductDeleteButton;

    @FXML

    private TableView<Product> mainScreenProductTableView;

    @FXML

    private TableColumn<Product, Integer> mainProductProductID;

    @FXML

    private TableColumn<Product, String> mainScreenProductName;

    @FXML

    private TableColumn<Product, Integer> mainScreenPoductInventoryLevel;

    @FXML

    private TableColumn<Product, Double> mainScreenProductPriceCostPerUnit;

    @FXML
    Button mainScreenExitButton;

    @Override //populate tableview Initialize
    public void initialize(URL url, ResourceBundle rb) {

        mainPartIDColumn.setCellValueFactory(cellData
                -> cellData.getValue().partIDProperty().asObject());

        mainPartNameColumn.setCellValueFactory(cellData
                -> cellData.getValue().partNameProperty());

        mainPartInventoryLevel.setCellValueFactory(cellData
                -> cellData.getValue().partInvProperty().asObject());

        mainPartPriceCostPerUnit.setCellValueFactory(cellData
                -> cellData.getValue().partPriceProperty().asObject());

        //Populate Product Tableview
        mainProductProductID.setCellValueFactory(cellData
                -> cellData.getValue().productIDProperty().asObject());

        mainScreenProductName.setCellValueFactory(cellData
                -> cellData.getValue().productNameProperty());

        mainScreenPoductInventoryLevel.setCellValueFactory(cellData
                -> cellData.getValue().productInvProperty().asObject());

        mainScreenProductPriceCostPerUnit.setCellValueFactory(cellData
                -> cellData.getValue().productPriceProperty().asObject());

        updatePartTableView();
        updateProductTableView();

    }

    //Update tableview content from Part add/modify screen
    public void updatePartTableView() {
        mainScreenPartTableView.setItems(getPartInventory());

    }

    public void updateProductTableView() {
        mainScreenProductTableView.setItems(getProductInventory());

    }

    @FXML
    void mainScreenDeletePart(ActionEvent event) throws IOException {

        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Are you sure you want to delete this Part?");
            alert.setHeaderText("Confirm");
            alert.setContentText("Are you sure you want delete this Part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                Part part = mainScreenPartTableView.getSelectionModel().getSelectedItem();
                deletePart(part);
                updatePartTableView();
                System.out.println("Part " + part.getPartName() + " was removed.");
            } else {
                System.out.println("You Cancelled Modifying a Part");
            }

        }

    }

    @FXML //Enable the user to Exit the Main Screen
    void exitMainScreen(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Are you sure you want to exit the system?");
        alert.setHeaderText("Confirm");
        alert.setContentText("Are you sure you want to Exit Inventory Management System?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.exit(0);
        } else {

            System.out.println("You have exited the system");

        }

    }

    @FXML
    void clearProductSearchTextField(ActionEvent e) throws IOException {

        mainScreenProductSearchTextField.setText("");
        updateProductTableView();

    }

    @FXML
    void clearPartSearchTextField(ActionEvent e) throws IOException {

        mainScreenPartsSearchTextField.setText("");
        updatePartTableView();

    }

    @FXML //Search PartTableview
    void mainScreenPartsSearch(ActionEvent event) throws IOException {

        String searchPart = mainScreenPartsSearchTextField.getText();
        //System.out.println(searchPart);
        int partInt = 0;
        partInt = Inventory.lookupPart(searchPart);
        Part searchThisPart = Inventory.getPartInventory().get(partInt);
        ObservableList<Part> filteredPart = FXCollections.observableArrayList();

        filteredPart.add(searchThisPart);
        mainScreenPartTableView.setItems(filteredPart);
        System.out.println("Part was found. Part Name is:  "
                + searchThisPart.getPartName() + "  ,Part ID is: "
                + searchThisPart.getPartID());
    }

    //Enable User to Modify a Part
    @FXML
    void modifyAPartFromMainScreen(ActionEvent event) throws IOException {

        partToModify = mainScreenPartTableView.getSelectionModel().getSelectedItem();
        partToModifyIndex = getPartInventory().indexOf(partToModify);

        Parent modifyParts = FXMLLoader.load(getClass().getResource("ModifyParts.fxml"));

        Scene scene = new Scene(modifyParts);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }

    //Action Event to switch to AddPart Window
    @FXML
    void changeSceneUsingAddButton(ActionEvent event) throws IOException {

        Parent addParts = FXMLLoader.load(getClass().
                getResource("/View_Controller/AddPart.fxml"));

        Scene scene = new Scene(addParts);
        Stage window = (Stage) ((Node) event.getSource()).
                getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    //Product 
    //Load AddProductScreen
    @FXML
    void changeSceneUsingAddProductButton(ActionEvent event) throws IOException {

        Parent addParts = FXMLLoader.load(getClass().
                getResource("/View_Controller/AddProduct.fxml"));

        Scene scene = new Scene(addParts);
        Stage window = (Stage) ((Node) event.getSource()).
                getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    //Search for Product
    @FXML
    void mainScreenProductSearch(ActionEvent event) throws IOException {

        String searchProduct = mainScreenProductSearchTextField.getText();
        int productInt = 0;
        productInt = Inventory.lookupProduct(searchProduct);
        Product searchThisProduct = Inventory.getProductInventory().get(productInt);
        ObservableList<Product> filteredProduct = FXCollections.observableArrayList();

        filteredProduct.add(searchThisProduct);
        mainScreenProductTableView.setItems(filteredProduct);
        System.out.println("Product was found. Product Name is:  "
                + searchThisProduct.getProductName() + "  ,Product ID is: "
                + searchThisProduct.getProductID());
    }

    // Delete Product 
    @FXML
    void mainScreenDeleteProduct(ActionEvent event) throws IOException {

        Product product = mainScreenProductTableView.getSelectionModel().getSelectedItem();
        if (validateProductDelete(product)) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete Error");
            alert.setHeaderText("Product cannot be deleted!");
            alert.setContentText("Product contains parts");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Are you sure you want to delete this Product?");
            alert.setHeaderText("Confirm");
            alert.setContentText("Are you sure you want delete this Product?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                removeProduct(product);
                updatePartTableView();
                System.out.println("Product " + product.getProductName() + " was deleted.");

            } else {
                System.out.println("Product was not removed");

            }
        }
    }

    // Allow user to Modify a Product
    public static int productToModifyIndex() {
        return modifyProductIndex;
    }

    @FXML
    void modifyAProductFromMainScreen(ActionEvent event) throws IOException {

        productToModify = mainScreenProductTableView.getSelectionModel().getSelectedItem();
        partToModifyIndex = getProductInventory().indexOf(productToModify);

        Parent modifyProduct = FXMLLoader.load(getClass().getResource("ModifyProduct.fxml"));

        Scene scene = new Scene(modifyProduct);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }

    //Reload Data Main App Data
    public void reloadDataMainScreen(FranciscoJerezC482 mainApp) {
        updatePartTableView();
        updateProductTableView();
    }

}
