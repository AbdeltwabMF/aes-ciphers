package userInterface;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utility.KeyManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

public class UserInterface extends Application {
    private Stage stage;
    private Scene homeScene;
    private File fileToBeProcessed;
    private double width, height;
    private TextField keyTextField;
    private Alert errorAlert;

    @Override
    public void start(Stage stage) throws IOException {
        errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(null);

        Font.loadFont(getClass().getResourceAsStream("/Fonts/Ubuntu-Regular.ttf"), 16);
        width = 500;
        height = 400;
        initScreen();
        this.stage = stage;
        stage.setTitle("AES Encrypt-Decrypt");
        stage.setScene(homeScene);
        stage.setHeight(442.3);
        stage.setWidth(582.4);
        stage.setResizable(false);
        stage.show();

        stage.setOnCloseRequest(e -> {
            System.out.println(stage.getWidth() + " " + stage.getHeight());
        });
    }

    public void initScreen() {
        FileChooser fileChooser = new FileChooser();
        Button browseButton = new Button("Browse");
        TextField filePathTextField = new TextField();
        filePathTextField.setEditable(false);

        browseButton.setOnAction(e -> {
            fileToBeProcessed = fileChooser.showOpenDialog(stage);
            filePathTextField.setText(fileToBeProcessed.getPath());
        });

        HBox browseAndPathHBox = makeHBox();
        browseAndPathHBox.getChildren().addAll(browseButton, filePathTextField);

        TitledBorder chooseFileTitledBorder = new TitledBorder("Choose file", browseAndPathHBox);

        browseButton.setMaxWidth(100);
        browseButton.setMinWidth(100);

        filePathTextField.setMaxWidth(width - 140);
        filePathTextField.setMinWidth(width - 140);

        keyTextField = new TextField();
        keyTextField.setMaxWidth(width - 30);
        keyTextField.setMinWidth(width - 30);

        Button generateKey = new Button("Generate");
        generateKey.setMinWidth(150);
        generateKey.setMaxWidth(150);
        generateKey.setOnAction(e -> {
            try {
                String base64 = Base64.getEncoder().encodeToString(new KeyManager().generateKey());
                keyTextField.setText(String.format("%x", new BigInteger(1, Base64.getDecoder().decode(base64))));
            } catch (NoSuchAlgorithmException ex) {
                errorAlert.setContentText("Error on generating key");
                errorAlert.showAndWait();
            }
        });

        Button loadKey = new Button("Load");
        loadKey.setMinWidth(150);
        loadKey.setMaxWidth(150);
        loadKey.setOnAction(e -> {
            FileChooser keyFileChooser = new FileChooser();
            keyFileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );
            File keyFile = keyFileChooser.showOpenDialog(stage);
            if(keyFile != null) {
                try {
                    Scanner sc = new Scanner(keyFile);
                    keyTextField.setText(sc.next());
                } catch (Exception ex) {
                    errorAlert.setContentText("Choose a valid txt file!");
                    errorAlert.showAndWait();
                }
            }
        });

        Button saveKey = new Button("Save");
        saveKey.setMinWidth(150);
        saveKey.setMaxWidth(150);
        saveKey.setOnAction(e -> {
            if(keyTextField.getText().trim().isEmpty()){
                errorAlert.setContentText("Key is empty!");
                errorAlert.showAndWait();
                return;
            }
            FileChooser saveFileChooser = new FileChooser();
            saveFileChooser.setTitle("Save");
            saveFileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );
            File saveFile = saveFileChooser.showSaveDialog(stage);
            if(saveFile != null) {
                try {
                    PrintWriter printWriter = new PrintWriter(saveFile);
                    printWriter.println(keyTextField.getText().trim());
                    printWriter.close();
                } catch (FileNotFoundException ex) {
                    errorAlert.setContentText("Error Saving file!");
                    errorAlert.showAndWait();
                }
            }
        });

        HBox keyHBox = makeHBox();
        keyHBox.getChildren().addAll(generateKey, loadKey, saveKey);

        VBox keyInputAndOptionsVBox = makeVBox();
        keyInputAndOptionsVBox.getChildren().addAll(keyTextField, keyHBox);

        TitledBorder keyTitledBorder = new TitledBorder("Key", keyInputAndOptionsVBox);

        ComboBox<String> modes = new ComboBox<>();
        modes.setId("modes");
        modes.setMinWidth(width - 30);
        modes.setMaxWidth(width - 30);
        modes.getItems().addAll("ECB");
        modes.getItems().addAll("CBC");
        modes.getItems().addAll("CTR");
        modes.setPromptText("Select Block Cipher Mode");

        Button encryptButton = new Button("Encrypt");
        encryptButton.setMinWidth(width / 2 - 20);
        encryptButton.setMaxWidth(width / 2 - 20);
        Button decryptButton = new Button("Decrypt");
        decryptButton.setMinWidth(width / 2 - 20);
        decryptButton.setMaxWidth(width / 2 - 20);

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
