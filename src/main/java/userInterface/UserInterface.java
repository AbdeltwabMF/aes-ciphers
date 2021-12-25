package userInterface;

import ciphers.*;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import java.math.BigInteger;

import java.nio.file.Files;

import java.security.NoSuchAlgorithmException;

import java.util.Base64;
import java.util.Scanner;

public class UserInterface extends Application {
  private Stage stage;
  private Scene homeScene;

  private File fileToBeProcessed;
  private File decryptedFile, encryptedFile;
  private FileChooser decryptedFileChooser, encryptedFileChooser;

  private double width, height;
  private TextField keyTextField;
  private Alert errorAlert, infoAlert;
  private byte[] decryptedFileBytes, encryptedFileBytes;

  @Override
  public void start(Stage stage) throws IOException {
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
        new FileChooser.ExtensionFilter("Text Files", "*.txt")
      );
      File keyFile = keyFileChooser.showOpenDialog(stage);
      if (keyFile != null) {
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
          errorAlert.setContentText("Error on Saving file!");
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
    modes.setPromptText("Select a Block Cipher Mode");

    Button encryptButton = new Button("Encrypt");
    encryptButton.setMinWidth(width / 2 - 20);
    encryptButton.setMaxWidth(width / 2 - 20);
    Button decryptButton = new Button("Decrypt");
    decryptButton.setMinWidth(width / 2 - 20);
    decryptButton.setMaxWidth(width / 2 - 20);

    encryptButton.setOnAction(e -> {
      int selected_index = modes.getSelectionModel().getSelectedIndex();
      if (fileToBeProcessed == null) {
        errorAlert.setContentText("Choose a file!");
        errorAlert.showAndWait();
        return;
      }
      switch (selected_index) {
        case 0:
          //ECB
          ECB ecb = null;
          try {
            ecb = new ECB(Hex.toHex(keyTextField.getText()).getBytes());
            encryptedFileBytes = ecb.encrypt(Files.readAllBytes(fileToBeProcessed.toPath()));
            showSuccessAndSaveEncryptedFile();
          } catch (Exception ex) {
            errorAlert.setContentText(ex.getLocalizedMessage());
            errorAlert.showAndWait();
          }
          break;
        case 1:
          //CBC
          CBC cbc = null;
          try {
            cbc = new CBC(Hex.toHex(keyTextField.getText()).getBytes());
            encryptedFileBytes = cbc.encrypt(Files.readAllBytes(fileToBeProcessed.toPath()));
            showSuccessAndSaveEncryptedFile();
          } catch (Exception ex) {
            errorAlert.setContentText(ex.getLocalizedMessage());
            errorAlert.showAndWait();
          }
          break;
        case 2:
          //CTR
          CTR ctr = null;
          try {
            ctr = new CTR(Hex.toHex(keyTextField.getText()).getBytes());
            encryptedFileBytes = ctr.encrypt(Files.readAllBytes(fileToBeProcessed.toPath()));
            showSuccessAndSaveEncryptedFile();
          } catch (Exception ex) {
            errorAlert.setContentText(ex.getLocalizedMessage());
            errorAlert.showAndWait();
          }
          break;
        default:
          errorAlert.setContentText("Select an encryption mode!");
          errorAlert.showAndWait();
          return;
      }
    });

    decryptButton.setOnAction(e -> {
      int selected_index = modes.getSelectionModel().getSelectedIndex();
      if (fileToBeProcessed == null) {
        errorAlert.setContentText("Choose a file!");
        errorAlert.showAndWait();
        return;
      }
      switch (selected_index) {
        case 0:
          //ECB
          ECB ecb = null;
          try {
            ecb = new ECB(Hex.toHex(keyTextField.getText()).getBytes());
            decryptedFileBytes = ecb.decrypt(Files.readAllBytes(fileToBeProcessed.toPath()));
            showSuccessAndSaveDecryptedFile();
          } catch (Exception ex) {
            errorAlert.setContentText(ex.getLocalizedMessage());
            errorAlert.showAndWait();
          }
          break;
        case 1:
          //CBC
          CBC cbc = null;
          try {
            cbc = new CBC(Hex.toHex(keyTextField.getText()).getBytes());
            decryptedFileBytes = cbc.decrypt(Files.readAllBytes(fileToBeProcessed.toPath()));
            showSuccessAndSaveDecryptedFile();
          } catch (Exception ex) {
            errorAlert.setContentText(ex.getLocalizedMessage());
            errorAlert.showAndWait();
          }
          break;
        case 2:
          //CTR
          CTR ctr = null;
          try {
            ctr = new CTR(Hex.toHex(keyTextField.getText()).getBytes());
            decryptedFileBytes = ctr.decrypt(Files.readAllBytes(fileToBeProcessed.toPath()));
            showSuccessAndSaveDecryptedFile();
          } catch (Exception ex) {
            errorAlert.setContentText(ex.getLocalizedMessage());
            errorAlert.showAndWait();
          }
          break;
        default:
          errorAlert.setContentText("Select a decryption mode!");
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

  private void showSuccessAndSaveDecryptedFile() throws IOException {
    infoAlert.setHeaderText("File decrypted successfully");
    infoAlert.showAndWait();
    decryptedFile = decryptedFileChooser.showSaveDialog(stage);
    if (decryptedFile != null) {
      Files.write(decryptedFile.toPath(), decryptedFileBytes);
    }
  }

  private void showSuccessAndSaveEncryptedFile() throws IOException {
    infoAlert.setHeaderText("File encrypted successfully!");
    infoAlert.showAndWait();
    encryptedFile = encryptedFileChooser.showSaveDialog(stage);
    if (encryptedFile != null) {
      Files.write(encryptedFile.toPath(), encryptedFileBytes);
    }
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
