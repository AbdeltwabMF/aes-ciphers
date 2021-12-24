module UserInterface {
  requires javafx.controls;
  requires javafx.fxml;

  requires org.kordamp.ikonli.javafx;
  requires org.kordamp.bootstrapfx.core;

  opens UserInterface to javafx.fxml;
  exports UserInterface;
}