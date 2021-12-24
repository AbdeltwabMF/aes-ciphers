module userInterface {
  requires javafx.controls;
  requires javafx.fxml;

  requires org.kordamp.ikonli.javafx;
  requires org.kordamp.bootstrapfx.core;

  opens userInterface to javafx.fxml;
  exports userInterface;
}