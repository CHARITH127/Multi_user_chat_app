package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {
    public AnchorPane loginContext;
    public Label loginHeading;
    public TextField txtCustomerLogin;

    public void loginToChatOnAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../views/ChatingRoom.fxml"));
        Parent parent=loader.load();
        ClientFromController client = loader.getController();
        client.initialize(txtCustomerLogin.getText());
        Stage window = (Stage) loginContext.getScene().getWindow();
        window.setScene(new Scene(parent));
        txtCustomerLogin.clear();
    }
}
