package userInterface;

import ciphers.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class UserInterface extends Application {
    private Stage stage;
    private Scene homeScene;

    private File fileToBeProcessed;
    private FileChooser decryptedFileChooser, encryptedFileChooser;

    private double width, height;
    private TextField keyTextField;
    private Alert errorAlert, infoAlert;
    private byte[] decryptedFileBytes, encryptedFileBytes;
    private KeyManager keyManager;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(null);

        infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setHeaderText(null);

        decryptedFileChooser = new FileChooser();
        decryptedFileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        encryptedFileChooser = new FileChooser();
        encryptedFileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        width = 500;
        height = 400;
        initScreen();
        this.stage = stage;
        stage.setTitle("AES Encrypt-Decrypt");
        stage.setScene(homeScene);
        stage.setHeight(443);
        stage.setWidth(583);
        stage.setResizable(false);
        stage.show();
    }

    public void initScreen() {
        FileChooser fileChooser = new FileChooser();

        Button browseButton = new Button("Browse");
        TextField filePathTextField = new TextField();
        filePathTextField.setEditable(false);

        browseButton.setOnAction(e -> {
            fileToBeProcessed = fileChooser.showOpenDialog(stage);
            if (fileToBeProcessed != null) {
                filePathTextField.setText(fileToBeProcessed.getPath());
            }
        });

        HBox browseAndPathHBox = makeHBox();
        browseAndPathHBox.getChildren().addAll(browseButton, filePathTextField);

        TitledBorder chooseFileTitledBorder = new TitledBorder("Choose a file", browseAndPathHBox);

        browseButton.setMaxWidth(100);
        browseButton.setMinWidth(100);

        filePathTextField.setMaxWidth(width - 140);
        filePathTextField.setMinWidth(width - 140);

        keyTextField = new TextField();
        keyTextField.setMaxWidth(width - 190);
        keyTextField.setMinWidth(width - 190);

        ComboBox<String> keyBitSize = new ComboBox<>();

        keyBitSize.setMinWidth(150);
        keyBitSize.setMaxWidth(150);
        keyBitSize.setPromptText("Select a key size");
        keyBitSize.getItems().addAll("128-Bit", "192-Bit", "256-Bit");

        Button generateKey = new Button("Generate");
        generateKey.setMinWidth(150);
        generateKey.setMaxWidth(150);
        generateKey.setOnAction(e -> {
            try {
                /* Check if the key size is set */
                int keyBitSizeIndex = keyBitSize.getSelectionModel().getSelectedIndex();
                switch (keyBitSizeIndex) {
                    case 0:
                        keyManager = new KeyManager(128);
                        keyTextField.setText(Validate.byteToHexString(keyManager.generateKey()));
                        break;
                    case 1:
                        keyManager = new KeyManager(192);
                        keyTextField.setText(Validate.byteToHexString(keyManager.generateKey()));
                        break;
                    case 2:
                        keyManager = new KeyManager(256);
                        keyTextField.setText(Validate.byteToHexString(keyManager.generateKey()));
                        break;
                    default:
                        errorAlert.setContentText("Set a key size first!");
                        errorAlert.showAndWait();
                        break;
                }
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
                errorAlert.setContentText("Error on Generating key");
                errorAlert.showAndWait();
            }
        });

        Button loadKey = new Button("Load");
        loadKey.setMinWidth(150);
        loadKey.setMaxWidth(150);

        loadKey.setOnAction(e -> {
            FileChooser keyFileChooser = new FileChooser();
            keyFileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Files", "*.*")
            );

            File keyFile = keyFileChooser.showOpenDialog(stage);
            if (keyFile != null) {
                try {
                    Scanner sc = new Scanner(keyFile);
                    String loadedKey = sc.next();
                    keyTextField.setText(loadedKey);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    errorAlert.setContentText("Choose a valid txt file!");
                    errorAlert.showAndWait();
                }
            }
        });

        Button saveKey = new Button("Save");
        saveKey.setMinWidth(150);
        saveKey.setMaxWidth(150);
        saveKey.setOnAction(e -> {
            if (keyTextField.getText().trim().isEmpty()) {
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
            if (saveFile != null) {
                try {
                    PrintWriter printWriter = new PrintWriter(saveFile);
                    printWriter.println(keyTextField.getText().trim());
                    printWriter.close();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                    errorAlert.setContentText("Error on Saving file!");
                    errorAlert.showAndWait();
                }
            }
        });

        HBox keyHBox = makeHBox();
        keyHBox.getChildren().addAll(keyBitSize, keyTextField);

        HBox keyButtonsHBox = makeHBox();
        keyButtonsHBox.getChildren().addAll(generateKey, loadKey, saveKey);

        VBox keyInputAndOptionsVBox = makeVBox();
        keyInputAndOptionsVBox.getChildren().addAll(keyHBox, keyButtonsHBox);

        TitledBorder keyTitledBorder = new TitledBorder("Key", keyInputAndOptionsVBox);

        ComboBox<String> modes = new ComboBox<>();
        modes.setMinWidth(width - 30);
        modes.setMaxWidth(width - 30);
        modes.setPromptText("Select a Block Cipher Mode");
        modes.getItems().addAll("ECB Mode", "CBC Mode", "CTR Mode");

        Button encryptButton = new Button("Encrypt");
        encryptButton.setMinWidth(width / 2 - 20);
        encryptButton.setMaxWidth(width / 2 - 20);
        Button decryptButton = new Button("Decrypt");
        decryptButton.setMinWidth(width / 2 - 20);
        decryptButton.setMaxWidth(width / 2 - 20);

        encryptButton.setOnAction(e -> {
            /* Check if no file is selected throw an error */
            if (fileToBeProcessed == null) {
                showErrorAlertAndWait("Choose a file!");
                return;
            }

            if (keyBitSize.getSelectionModel().getSelectedIndex() == -1) {
                showErrorAlertAndWait("Set a key size first!");
                return;
            }

            String keyTextFieldString = keyTextField.getText().trim();
            byte[] keyTextFieldBytes = keyTextFieldString.getBytes();
            try {
                if ((keyTextFieldString.length() == 32
                        || keyTextFieldString.length() == 48
                        || keyTextFieldString.length() == 64)
                        && Validate.checkHex(keyTextFieldString)) {
                    keyTextFieldBytes = Validate.hexToByte(keyTextFieldString);
                    int keyBitSizeIndex = keyBitSize.getSelectionModel().getSelectedIndex();
                    switch (keyTextFieldString.length()) {
                        case 32:
                            if (keyBitSizeIndex != 0) {
                                showErrorAlertAndWait("key size and selected size are not equal!");
                                return;
                            }
                            keyBitSize.setPromptText("128-Bit");
                            if (keyManager == null) {
                                keyManager = new KeyManager(128);
                            } else {
                                keyManager.setKeySize(128);
                                keyManager.setSymmetricKey(keyTextFieldBytes);
                            }
                            break;
                        case 48:
                            if (keyBitSizeIndex != 1) {
                                showErrorAlertAndWait("key size and selected size are not equal!");
                                return;
                            }
                            keyBitSize.setPromptText("192-Bit");
                            if (keyManager == null) {
                                keyManager = new KeyManager(192);
                            } else {
                                keyManager.setKeySize(192);
                                keyManager.setSymmetricKey(keyTextFieldBytes);
                            }
                            break;
                        case 64:
                            if (keyBitSizeIndex != 2) {
                                showErrorAlertAndWait("key size and selected size are not equal!");
                                return;
                            }
                            keyBitSize.setPromptText("256-Bit");
                            if (keyManager == null) {
                                keyManager = new KeyManager(256);
                            } else {
                                keyManager.setKeySize(256);
                                keyManager.setSymmetricKey(keyTextFieldBytes);
                            }
                            break;
                        default:
                            showErrorAlertAndWait("Wrong key size.\nKey might be a hex value of length(32, 48, or 64)");
                            return;
                    }
                } else {
                    switch (keyTextFieldString.length()) {
                        case 8:
                            keyBitSize.setPromptText("128-Bit");
                            if (keyManager == null) {
                                keyManager = new KeyManager(128);
                            } else {
                                keyManager.setKeySize(128);
                                keyManager.setSymmetricKey(keyTextFieldBytes);
                            }
                            break;
                        case 12:
                            keyBitSize.setPromptText("192-Bit");
                            if (keyManager == null) {
                                keyManager = new KeyManager(192);
                            } else {
                                keyManager.setKeySize(192);
                                keyManager.setSymmetricKey(keyTextFieldBytes);
                            }
                            break;
                        case 16:
                            keyBitSize.setPromptText("256-Bit");
                            if (keyManager == null) {
                                keyManager = new KeyManager(256);
                            } else {
                                keyManager.setKeySize(256);
                                keyManager.setSymmetricKey(keyTextFieldBytes);
                            }
                            break;
                        default:
                            showErrorAlertAndWait("Wrong key size.\nKey might be a string of length(8, 12, or 16)");
                            return;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            int selectedIndex = modes.getSelectionModel().getSelectedIndex();
            switch (selectedIndex) {
                case 0:
                    ECB ecb;
                    try {
                        ecb = new ECB(keyTextFieldBytes);
                        encryptedFileBytes = ecb.encrypt(Files.readAllBytes(fileToBeProcessed.toPath()));
                        showSuccessAndSaveEncryptedFile();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        errorAlert.setContentText(ex.getLocalizedMessage());
                        errorAlert.showAndWait();
                    }
                    break;
                case 1:
                    CBC cbc;
                    try {
                        cbc = new CBC(keyTextFieldBytes);
                        encryptedFileBytes = cbc.encrypt(Files.readAllBytes(fileToBeProcessed.toPath()));
                        showSuccessAndSaveEncryptedFile();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        errorAlert.setContentText(ex.getLocalizedMessage());
                        errorAlert.showAndWait();
                    }
                    break;
                case 2:
                    CTR ctr;
                    try {
                        ctr = new CTR(keyTextFieldBytes);
                        encryptedFileBytes = ctr.encrypt(Files.readAllBytes(fileToBeProcessed.toPath()));
                        showSuccessAndSaveEncryptedFile();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        errorAlert.setContentText(ex.getLocalizedMessage());
                        errorAlert.showAndWait();
                    }
                    break;
                default:
                    /* case -1: */
                    errorAlert.setContentText("Select an encryption mode!");
                    errorAlert.showAndWait();
                    break;
            }
        });

        decryptButton.setOnAction(e -> {
            int selected_index = modes.getSelectionModel().getSelectedIndex();
            if (fileToBeProcessed == null) {
                errorAlert.setContentText("Choose a file!");
                errorAlert.showAndWait();
                return;
            }

            if (keyBitSize.getSelectionModel().getSelectedIndex() == -1) {
                errorAlert.setContentText("Set a key size first!");
                errorAlert.showAndWait();
                return;
            }

            String keyTextFieldString = keyTextField.getText().trim();
            byte[] keyTextFieldBytes = keyTextFieldString.getBytes();
            try {
                if ((keyTextFieldString.length() == 32
                        || keyTextFieldString.length() == 48
                        || keyTextFieldString.length() == 64)
                        && Validate.checkHex(keyTextFieldString)) {
                    keyTextFieldBytes = Validate.hexToByte(keyTextFieldString);
                    int keyBitSizeIndex = keyBitSize.getSelectionModel().getSelectedIndex();
                    switch (keyTextFieldString.length()) {
                        case 32:
                            keyBitSize.setPromptText("128-Bit");
                            if(keyBitSizeIndex != 0){
                                showErrorAlertAndWait("key size and selected size are not equal!");
                                return;
                            }
                            if (keyManager == null) {
                                keyManager = new KeyManager(128);
                            } else {
                                keyManager.setKeySize(128);
                                keyManager.setSymmetricKey(keyTextFieldBytes);
                            }
                            break;
                        case 48:
                            keyBitSize.setPromptText("192-Bit");
                            if(keyBitSizeIndex != 1){
                                showErrorAlertAndWait("key size and selected size are not equal!");
                                return;
                            }
                            if (keyManager == null) {
                                keyManager = new KeyManager(192);
                            } else {
                                keyManager.setKeySize(192);
                                keyManager.setSymmetricKey(keyTextFieldBytes);
                            }
                            break;
                        case 64:
                            keyBitSize.setPromptText("256-Bit");
                            if(keyBitSizeIndex != 2){
                                showErrorAlertAndWait("key size and selected size are not equal!");
                                return;
                            }
                            if (keyManager == null) {
                                keyManager = new KeyManager(256);
                            } else {
                                keyManager.setKeySize(256);
                                keyManager.setSymmetricKey(keyTextFieldBytes);
                            }
                            break;
                        default:
                            errorAlert.setContentText("Wrong key size.\nKey might be a hex value of length(32, 48, or 64)");
                            errorAlert.showAndWait();
                            return;
                    }
                } else {
                    switch (keyTextFieldString.length()) {
                        case 8:
                            keyBitSize.setPromptText("128-Bit");
                            if (keyManager == null) {
                                keyManager = new KeyManager(128);
                            } else {
                                keyManager.setKeySize(128);
                                keyManager.setSymmetricKey(keyTextFieldBytes);
                            }
                            break;
                        case 12:
                            keyBitSize.setPromptText("192-Bit");
                            if (keyManager == null) {
                                keyManager = new KeyManager(192);
                            } else {
                                keyManager.setKeySize(192);
                                keyManager.setSymmetricKey(keyTextFieldBytes);
                            }
                            break;
                        case 16:
                            keyBitSize.setPromptText("256-Bit");
                            if (keyManager == null) {
                                keyManager = new KeyManager(256);
                            } else {
                                keyManager.setKeySize(256);
                                keyManager.setSymmetricKey(keyTextFieldBytes);
                            }
                            break;
                        default:
                            showErrorAlertAndWait("Wrong key size.\nKey might be a string of length(8, 12, or 16)");
                            return;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            switch (selected_index) {
                case 0:
                    ECB ecb;
                    try {
                        ecb = new ECB(keyTextFieldBytes);
                        decryptedFileBytes = ecb.decrypt(Files.readAllBytes(fileToBeProcessed.toPath()));
                        showSuccessAndSaveDecryptedFile();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        showErrorAlertAndWait(ex.getLocalizedMessage());
                    }
                    break;
                case 1:
                    CBC cbc;
                    try {
                        cbc = new CBC(keyTextFieldBytes);
                        decryptedFileBytes = cbc.decrypt(Files.readAllBytes(fileToBeProcessed.toPath()));
                        showSuccessAndSaveDecryptedFile();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        showErrorAlertAndWait(ex.getLocalizedMessage());
                    }
                    break;
                case 2:
                    CTR ctr;
                    try {
                        ctr = new CTR(keyTextFieldBytes);
                        decryptedFileBytes = ctr.decrypt(Files.readAllBytes(fileToBeProcessed.toPath()));
                        showSuccessAndSaveDecryptedFile();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        showErrorAlertAndWait(ex.getLocalizedMessage());
                    }
                    break;
                default:
                    showErrorAlertAndWait("Select a decryption mode!");
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

    private void showErrorAlertAndWait(String message) {
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }

    private void showSuccessAndSaveDecryptedFile() throws IOException {
        infoAlert.setHeaderText("File decrypted successfully");
        infoAlert.showAndWait();
        File decryptedFile = decryptedFileChooser.showSaveDialog(stage);
        if (decryptedFile != null) {
            Files.write(decryptedFile.toPath(), decryptedFileBytes);
        }
    }

    private void showSuccessAndSaveEncryptedFile() throws IOException {
        infoAlert.setHeaderText("File encrypted successfully!");
        infoAlert.showAndWait();
        File encryptedFile = encryptedFileChooser.showSaveDialog(stage);
        if (encryptedFile != null) {
            Files.write(encryptedFile.toPath(), encryptedFileBytes);
        }
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
}
