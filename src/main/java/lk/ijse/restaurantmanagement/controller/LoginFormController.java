package lk.ijse.restaurantmanagement.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.restaurantmanagement.db.DbConnection;
import lk.ijse.restaurantmanagement.util.Regex;
import lk.ijse.restaurantmanagement.util.TextField;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {


    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXTextField txtUserId;
    @FXML
    public AnchorPane rootNode;

    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {
        if (isValidate()) {
            String userId = txtUserId.getText();
            String password = txtPassword.getText();

            try {
                checkCredential(userId, password);
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "OOPS! something went wrong").show();
            }
        }
    }

    private void checkCredential(String userId, String password) throws SQLException, IOException {
        String sql = "SELECT userName, password FROM users WHERE userName = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setObject(1, userId);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            String dbPw = resultSet.getString(2);
            if(dbPw.equals(password)) {
                navigateToTheDashboard();
            } else {
                new Alert(Alert.AlertType.ERROR, "Password is incorrect!").show();
            }
        } else {
            new Alert(Alert.AlertType.INFORMATION, "userId not found!").show();
        }
    }

    private void navigateToTheDashboard() throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/main_form.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Dashboard Form");
    }

    public void linkRegistrationOnAction(ActionEvent actionEvent) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/registration_form.fxml"));

        Scene scene = new Scene(rootNode);
        Stage stage = new Stage();
        stage.setScene(scene);

        stage.setTitle("Registration Form");

        stage.show();
    }

    public void txtUsernameOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.NAME,txtUserId);
    }

    public void txtpwOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColor(TextField.PW,txtPassword);
    }
    public boolean isValidate(){
        if(!Regex.setTextColor(TextField.NAME,txtUserId))return false;
        if(!Regex.setTextColor(TextField.PW,txtPassword))return false;
        return true;
    }
}
