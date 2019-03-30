/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.InHousePart;
import Model.Inventory;
import static Model.Inventory.getPartInventory;
import static View_Controller.FJerezMainScreenController.partToModifyIndex;
import Model.OutsourcedPart;
import Model.Part;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author francisco
 */
public class ModifyPartsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private boolean isPartOutsourced;
    // private static int partIndex;
    int partID;
    int partIndex = partToModifyIndex();
    @FXML
    private TextField modifyPartIDTextField;

    @FXML
    private TextField modifyPartNameTextField;

    @FXML
    private TextField modifyPartInvTextField;

    @FXML
    private TextField modifyPartPriceTextField;

    @FXML
    private TextField modifyPartMaxTextField;

    @FXML
    private TextField modifyPartMinTextField;

    @FXML
    private TextField modifyPartCompanyNameOrMachineIDTextField;

    @FXML
    private Label modifyPartCompanyNameOrMachineID;

    @FXML
    Button modifyPartSaveButton;

    @FXML
    Button modifyPartCancelButton;

    @FXML
    RadioButton modifyPartsInHouseRadioButton;

    @FXML
    RadioButton modifyPartsOutsourcedRadioButton;

    private String exceptionMessage = new String();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Part part = getPartInventory().get(partIndex);
        partID = getPartInventory().get(partIndex).getPartID();

        modifyPartIDTextField.setText("Part ID autoset to: " + partID);

        modifyPartNameTextField.setText(part.getPartName());
        System.out.println("Part to populate is part name: " + part.getPartName());

        modifyPartInvTextField.setText(Integer.toString(part.getPartInStock()));

        modifyPartPriceTextField.setText(Double.toString(part.getPartPrice()));

        modifyPartMinTextField.setText(Integer.toString(part.getPartMin()));

        modifyPartMaxTextField.setText(Integer.toString(part.getPartMax()));

        if (part instanceof InHousePart) { //If instanceOfInHouse then add MachineID value and label
            modifyPartCompanyNameOrMachineIDTextField.setText(Integer
                    .toString(((InHousePart) getPartInventory()
                            .get(partIndex)).getPartMachineID()));
            modifyPartCompanyNameOrMachineID.setText("Machine ID");
            modifyPartsInHouseRadioButton.setSelected(true);

        } else {
            modifyPartCompanyNameOrMachineIDTextField.setText(((OutsourcedPart) getPartInventory()
                    .get(partIndex)).getPartCompanyName());
            modifyPartCompanyNameOrMachineID.setText("Company Name");
            modifyPartsOutsourcedRadioButton.setSelected(true);
        }
    }

    //Add control dialog to all cancel buttons
    @FXML
    void modifyPartsCancelledAction(ActionEvent e) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Are you sure you want to cancel?");
        alert.setHeaderText("Confirm");
        alert.setContentText("Are you sure you want to cancel modifying a Part?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent addProductCancel = FXMLLoader.load(getClass()
                    .getResource("FJerezMainScreen.fxml"));
            Scene scene = new Scene(addProductCancel);
            Stage window = (Stage) ((Node) e.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } else {
            System.out.println("You Cancelled Modifying a Part");
        }

    }

    @FXML
    void modifyPartsOutsourcedRadioButton(ActionEvent event) {
//        isPartOutsourced= true;
//        modifyPartCompanyNameOrMachineID.setText("Company Name");
    }

    @FXML
    void modifyPartsInHouseRadioButton(ActionEvent event) {
//        isPartOutsourced= false;
//        modifyPartCompanyNameOrMachineID.setText("Machine ID");
    }

    @FXML
    void saveModifiedPartAction(ActionEvent event) throws IOException {

        //Capture user input addPartsPartIDTextField
        String partNameString = modifyPartNameTextField.getText();
        String partInvString = modifyPartInvTextField.getText();
        String partPriceString = modifyPartPriceTextField.getText();
        String partMinString = modifyPartMinTextField.getText();
        String partMaxString = modifyPartMaxTextField.getText();
        String partMachineIDStringOrCompanyName = modifyPartCompanyNameOrMachineIDTextField.getText();

        String partIDString = modifyPartIDTextField.getText();// Autogenerated, not captured from user

        try {
            exceptionMessage = Part.isPartValid(partNameString,
                    Integer.parseInt(partMinString), Integer.parseInt(partMaxString),
                    Integer.parseInt(partInvString), Double.parseDouble(partPriceString),
                    exceptionMessage);

            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Adding Part");
                alert.setHeaderText("Error");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            } else {

                //  Set captured values to TableView
                if (isPartOutsourced == false) //Maybe check isInstanceOutsourcedPart instead
                {
                    OutsourcedPart addedOutsourcedPart = new OutsourcedPart();
                    addedOutsourcedPart.setPartCompanyName(partMachineIDStringOrCompanyName);
                    addedOutsourcedPart.setPartID(partID);//Not captured
                    addedOutsourcedPart.setPartName(partNameString);
                    addedOutsourcedPart.setPartInStock(Integer.parseInt(partInvString));
                    addedOutsourcedPart.setPartMax(Integer.parseInt(partMaxString));
                    addedOutsourcedPart.setPartMin(Integer.parseInt(partMinString));
                    addedOutsourcedPart.setPartPrice(Double.parseDouble(partPriceString));
                    Inventory.updatePart(partIndex, addedOutsourcedPart);

                } else //Add InHousePart
                {
                    InHousePart addedInHousePart = new InHousePart(); //Add inHousePart
                    addedInHousePart.setPartID(partID);//no String to capture it keep as Int generated by system
                    addedInHousePart.setPartName(partNameString);
                    addedInHousePart.setPartPrice(Double.parseDouble(partPriceString));
                    addedInHousePart.setPartInStock(Integer.parseInt(partInvString));
                    addedInHousePart.setPartMin(Integer.parseInt(partMinString));
                    addedInHousePart.setPartMax(Integer.parseInt(partMaxString));
                    addedInHousePart.setPartMachineID(Integer.parseInt(partMachineIDStringOrCompanyName));
                    Inventory.updatePart(partIndex, addedInHousePart);

                }

                Parent savingNewPart = FXMLLoader.load(getClass().getResource("FJerezMainScreen.fxml"));
                Scene scene = new Scene(savingNewPart);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();

            }
        } catch (Exception e) {  //this code broke addpart
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Adding Part");
            alert.setHeaderText("Error");
            alert.setContentText("Invalid Entry, Check All Required Fields");
            alert.showAndWait();
        }
    }

}
