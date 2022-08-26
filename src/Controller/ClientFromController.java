package Controller;

import com.vdurmont.emoji.EmojiParser;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import model.Client;

import java.io.*;
import java.net.Socket;

public class ClientFromController {
    public TextField txtmesageBox;
    public Client client;
    public VBox vbox_messages;
    public ScrollPane sp_main;
    public Button btnSendMessage;
    public Button btnFileChooser;
    public AnchorPane imojiContext;


    public void initialize(String text) {

        /*imojiContext.setVisible(false);
        imojiContext.setManaged(false);*/

        try {
            String userName = text;
            Socket socket = new Socket("localhost", 3000);
            client = new Client(socket, userName);
            System.out.println(socket);

            BufferedReader bufferReader = client.getBufferReader();
            BufferedWriter bufferedWriter = client.getBufferedWriter();

            /*to get messages from others*/
            new Thread(new Runnable() {
                @Override
                public void run() {

                    String messageFromGroupChat;

                    while (socket.isConnected()) {

                        try {

                            messageFromGroupChat = bufferReader.readLine();
                            System.out.println(messageFromGroupChat);

                            if (messageFromGroupChat.contains(".jpg")) {
                                viewImages(messageFromGroupChat, vbox_messages);
                            } else {
                                addLabel(messageFromGroupChat, vbox_messages);
                            }


                        } catch (IOException e) {
                            client.closeEveryThing(socket, bufferReader, bufferedWriter);
                            /*closeEveryThing(socket,bufferedReader,bufferedWriter);*/
                        }

                    }

                }
            }).start();

            /*client.listenForMessage();*/
            client.sendMessage("newly");

        } catch (IOException e) {

        }

        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp_main.setVvalue((Double) newValue);
            }
        });


        btnSendMessage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String messageToSend = txtmesageBox.getText();

                if (!messageToSend.isEmpty()) {
                    /*To be eddited */

                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_RIGHT);

                    hBox.setPadding(new Insets(5, 5, 5, 10));
                    Text text = new Text(messageToSend);
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.setStyle("-fx-color:rgb(239,242,255 );" + " -fx-background-color: rgb(15,125,242);" + " -fx-background-radius: 20px");

                    textFlow.setPadding(new Insets(5, 10, 5, 10));
                    text.setFill(Color.color(0.934, 0.945, 0.996));

                    hBox.getChildren().add(textFlow);
                    vbox_messages.getChildren().add(hBox);


                    client.sendMessage(messageToSend);
                    txtmesageBox.clear();

                }
            }
        });

        btnFileChooser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showOpenDialog(null);
                System.out.println(file);

                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_RIGHT);

                hBox.setPadding(new Insets(5, 5, 5, 10));

                ImageView imageView = new ImageView(file.toURI().toString());
                imageView.setFitHeight(150.0);
                imageView.setFitWidth(292.0);


                hBox.getChildren().add(imageView);
                vbox_messages.getChildren().add(hBox);

                client.sendMessage(String.valueOf(file.toURI()));

            }
        });

    }

    public static void viewImages(String messageFromServer, VBox vBox) {

        String[] splits = messageFromServer.split(":");
        String path = splits[2].trim();
        System.out.println(path);
        File file = new File(path);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        ImageView imageView = new ImageView(file.toURI().toString());
        imageView.setFitHeight(150.0);
        imageView.setFitWidth(292.0);

        hBox.getChildren().add(imageView);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }

    public static void addLabel(String messageFromServer, VBox vBox) {

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(messageFromServer);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle("-fx-background-color: rgb(233,233,233);" + "-fx-background-radius: 20px");

        textFlow.setPadding(new Insets(5, 10, 5, 10));

        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });

    }

    public void visibaleIImojiPanal(ActionEvent actionEvent) {
        imojiContext.setVisible(true);
        imojiContext.setManaged(true);

        System.out.println(EmojiParser.parseToUnicode("An :grinning:awesome :smiley:string &#128516;with a few :wink:emojis!"));
    }

    public void setCryImojeOnAction(MouseEvent mouseEvent) {
        String imojiCode = ":cry:";
        String currentText = txtmesageBox.getText();
        String editText = currentText + " " + EmojiParser.parseToUnicode(imojiCode);
        txtmesageBox.setText(editText);
    }

    public void setKissImojeOnAction(MouseEvent mouseEvent) {
        String imojiCode = ":kissing_heart:";
        String currentText = txtmesageBox.getText();
        String editText = currentText + " " + EmojiParser.parseToUnicode(imojiCode);
        txtmesageBox.setText(editText);
    }

    public void setLaughImojeOnAction(MouseEvent mouseEvent) {
        String imojiCode = ":smile:";
        String currentText = txtmesageBox.getText();
        String editText = currentText + " " + EmojiParser.parseToUnicode(imojiCode);
        txtmesageBox.setText(editText);
    }

    public void setHungryImojeOnAction(MouseEvent mouseEvent) {
        String imojiCode = ":yum:";
        String currentText = txtmesageBox.getText();
        String editText = currentText + " " + EmojiParser.parseToUnicode(imojiCode);
        txtmesageBox.setText(editText);
    }

    public void setAngrymojeOnAction(MouseEvent mouseEvent) {
        String imojiCode = ":angry:";
        String currentText = txtmesageBox.getText();
        String editText = currentText + " " + EmojiParser.parseToUnicode(imojiCode);
        txtmesageBox.setText(editText);
    }



    /*public void sendMessageOnAction(ActionEvent actionEvent) {
        client.sendMessage(txtmesageBox.getText());
        *//* client.listenForMessage();*//*
    }*/
}
