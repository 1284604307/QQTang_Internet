package org.ming.framework;

import io.vertx.core.json.JsonObject;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PushAndPopPane extends Pane {
    private JsonObject parameters;

    public PushAndPopPane(Stage stage) {
        this.prefWidthProperty().bind(stage.getScene().widthProperty());
        this.prefHeightProperty().bind(stage.getScene().heightProperty());
    }

    public PushAndPopPane(Stage stage, JsonObject parameters) {
        this.parameters = parameters;
        this.setStyle("-fx-background-color: #ffffff");
    }

    public void disable() {
        this.setDisable(true);
    }

    public void enable() {
        this.setDisable(false);
    }

    public void dispose() {
        this.prefWidthProperty().unbind();
        this.prefHeightProperty().unbind();
    }

    public JsonObject getParameters() {
        return this.parameters;
    }

    public void setParameters(JsonObject parameters) {
        this.parameters = parameters;
    }
}
