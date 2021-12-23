package com.example.aesmodes;


import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.util.Objects;

public class TitledBorder extends StackPane {
    private Label titleLabel = new Label();
    private StackPane contentPane = new StackPane();
    private Node content;


    public void setContent(Node content) {
        content.getStyleClass().add("bordered-titled-content");
        contentPane.getChildren().add(content);
    }


    public Node getContent() {
        return content;
    }


    public void setTitle(String title) {
        titleLabel.setText(" " + title + " ");
    }


    public String getTitle() {
        return titleLabel.getText();
    }

    public TitledBorder() {
        this.getStylesheets().add(Objects.requireNonNull(UI.class.getResource("Light.css")).toExternalForm());
        titleLabel.setText("default title");
        titleLabel.getStyleClass().add("bordered-titled-title");
        StackPane.setAlignment(titleLabel, Pos.TOP_LEFT);

        getStyleClass().add("bordered-titled-border");
        getChildren().addAll(titleLabel, contentPane);
    }

    public TitledBorder(String title, Node node) {
        this.getStylesheets().add(Objects.requireNonNull(UI.class.getResource("Light.css")).toExternalForm());
        titleLabel.setText(title);
        titleLabel.getStyleClass().add("bordered-titled-title");
        StackPane.setAlignment(titleLabel, Pos.TOP_LEFT);

        this.setContent(node);
        getStyleClass().add("bordered-titled-border");
        getChildren().addAll(titleLabel, contentPane);
    }

}
