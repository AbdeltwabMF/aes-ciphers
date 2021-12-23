package com.example.aesmodes;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class UI extends Application {
    private Stage stage;
    private Scene homeScene;
    private File selectedFile;
    private double width, height;
    private TextField keyTextField;
    private Alert errorAlert;

    @Override
    public void start(Stage stage) throws IOException {
        errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(null);

        Font.loadFont(getClass().getResourceAsStream("Ubuntu-Regular.ttf"), 16);
        width = 500;
        height = 400;
        initScreen();
        this.stage = stage;
        stage.setTitle("AES Encrypt-Decrypt");
        stage.setScene(homeScene);
        stage.setHeight(436);
        stage.setWidth(588);
        stage.show();

        stage.setOnCloseRequest(e ->{
            System.out.println(stage.getWidth() + " " + stage.getHeight());
        });
    }

    public void initScreen() {
        FileChooser fileChooser = new FileChooser();
        Button browseButton = new Button("Browse");
        TextField filePathTextField = new TextField();
        filePathTextField.setEditable(false);

        browseButton.setOnAction(e -> {
            selectedFile = fileChooser.showOpenDialog(stage);
            filePathTextField.setText(selectedFile.getPath());
        });

        HBox browseAndPathHBox = makeHBox();
        //browseAndPathHBox.setAlignment(Pos.TOP_LEFT);
        browseAndPathHBox.getChildren().addAll(browseButton, filePathTextField);

        TitledBorder chooseFileTitledBorder = new TitledBorder("Choose file", browseAndPathHBox);

        browseButton.setMaxWidth(100);
        browseButton.setMinWidth(100);

        filePathTextField.setMaxWidth(width - 120);
        filePathTextField.setMinWidth(width - 120);

        keyTextField = new TextField();
        keyTextField.setMaxWidth(width - 20);
        keyTextField.setMinWidth(width - 20);

        Button generateKey = new Button("Generate");
        generateKey.setMinWidth(100);
        generateKey.setMaxWidth(100);
        generateKey.setOnAction(e -> {

        });

        Button loadKey = new Button("Load");
        loadKey.setMinWidth(100);
        loadKey.setMaxWidth(100);
        loadKey.setOnAction(e -> {
            FileChooser keyFileChooser = new FileChooser();
            keyFileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );
            File keyFile = keyFileChooser.showOpenDialog(stage);
            try{
                Scanner sc = new Scanner(keyFile);
                keyTextField.setText(sc.next());
            }catch (Exception ex){
                errorAlert.setContentText("Choose a valid txt file!");
                errorAlert.showAndWait();
            }
        });

        Button saveKey = new Button("Save");
        saveKey.setMinWidth(100);
        saveKey.setMaxWidth(100);
        saveKey.setOnAction(e -> {

        });

        HBox keyHBox = makeHBox();
        keyHBox.getChildren().addAll(generateKey, loadKey, saveKey);

        VBox keyInputAndOptionsVBox = makeVBox();
        keyInputAndOptionsVBox.getChildren().addAll(keyTextField, keyHBox);

        TitledBorder keyTitledBorder = new TitledBorder("Key", keyInputAndOptionsVBox);


        ComboBox<String> modes = new ComboBox<>();
        modes.setId("modes");
        modes.setMinWidth(410);
        modes.setMaxWidth(410);
        modes.getItems().addAll("ECB");
        modes.getItems().addAll("CBC");
        modes.getItems().addAll("CTR");
        modes.setPromptText("Select Block Cipher Mode");


        Button encryptButton = new Button("Encrypt");
        encryptButton.setMinWidth(200);
        encryptButton.setMaxWidth(200);
        Button decryptButton = new Button("Decrypt");
        decryptButton.setMinWidth(200);
        decryptButton.setMaxWidth(200);

        encryptButton.setOnAction(e -> {
            int selected_index = modes.getSelectionModel().getSelectedIndex();
            switch (selected_index) {
                case 0:
                    //ECB
                    break;
                case 1:
                    //CBC
                    break;
                case 2:
                    //CTR
                    break;
                default:
                    errorAlert.setContentText("Select encryption mode!");
                    errorAlert.showAndWait();
                    break;
            }
        });

        decryptButton.setOnAction(e -> {
            int selected_index = modes.getSelectionModel().getSelectedIndex();
            switch (selected_index) {
                case 0:
                    //ECB
                    break;
                case 1:
                    //CBC
                    break;
                case 2:
                    //CTR
                    break;
                default:
                    errorAlert.setContentText("Select decryption mode!");
                    errorAlert.showAndWait();
                    break;
            }
        });


        HBox encryptDecryptHBox = makeHBox();
        encryptDecryptHBox.getChildren().addAll(encryptButton, decryptButton);

        VBox modesAndED = makeVBox();
        modesAndED.getChildren().addAll(modes, encryptDecryptHBox);

        TitledBorder modesAndOperationsBorder = new TitledBorder("Encrypt - Decrypt", modesAndED);


        VBox vBox = makeVBox();
        vBox.setPadding(new Insets(0, 5, 0, 5));
        vBox.getChildren().addAll(chooseFileTitledBorder, keyTitledBorder, modesAndOperationsBorder);

        homeScene = new Scene(vBox, width, height);
//        homeScene.getStylesheets().add(Objects.requireNonNull(UI.class.getResource("Light.css")).toExternalForm());
    }

    private GridPane makeGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.BASELINE_CENTER);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        return gridPane;
    }

    private HBox makeHBox() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BASELINE_CENTER);
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setSpacing(10);
        return hBox;
    }

    private VBox makeVBox() {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        return vBox;
    }

    public static void main(String[] args) {
        launch();
    }
}
