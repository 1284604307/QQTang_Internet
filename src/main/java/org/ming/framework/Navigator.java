package org.ming.framework;

import io.vertx.core.json.JsonObject;
import javafx.animation.FadeTransition;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.ming.Main;

import java.util.HashMap;

public class Navigator extends HashMap<String, Constructor> {
    private String initialRoute = "/";

    public Navigator() {
    }

    public Navigator(String initialRoute) {
        this.initialRoute = initialRoute;
    }

    public Navigator route(String key, Constructor value) {
        this.put(key, value);
        return this;
    }

    public String getInitialRoute() {
        return this.initialRoute;
    }

    public PushAndPopPane initialRoute() {
        return ((Constructor)this.get(this.initialRoute)).build();
    }

    public void setInitialRoute(String initialRoute) {
        this.initialRoute = initialRoute;
    }

    public static void pushNamed(String paneName) {
        pushNamed(paneName, new JsonObject());
    }

    public static void pushNamed(String paneName, JsonObject parameters) {
        push(((Constructor) Main.getRoutes().get(paneName)).build(), parameters);
    }

    public static void push(PushAndPopPane node, JsonObject parameters) {
        StackPane root = Main.getRoot();
        PushAndPopPane currentNode = (PushAndPopPane)root.getChildren().get(root.getChildren().size() - 1);
        currentNode.disable();
        root.getChildren().add(node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000.0D), node);
        ft.setFromValue(0.0D);
        ft.setToValue(1.0D);
        ft.play();
        ft.setOnFinished((e) -> {
            node.enable();
            node.setParameters(parameters);
        });
    }

    public static void replaceNamed(String paneName) {
        replaceNamed(paneName, new JsonObject());
    }

    public static void replaceNamed(String paneName, JsonObject parameters) {
        replace(((Constructor)Main.getRoutes().get(paneName)).build(), parameters);
    }

    public static void replace(PushAndPopPane node, JsonObject parameters) {
        StackPane root = Main.getRoot();
        PushAndPopPane currentNode = (PushAndPopPane)root.getChildren().get(root.getChildren().size() - 1);
        currentNode.disable();
        node.disable();
        root.getChildren().add(root.getChildren().size() - 1, node);
        FadeTransition ft = new FadeTransition(Duration.millis(1000.0D), currentNode);
        ft.setFromValue(1.0D);
        ft.setToValue(0.0D);
        ft.play();
        ft.setOnFinished((e) -> {
            node.enable();
            node.setParameters(parameters);
            root.getChildren().remove(currentNode);
            currentNode.dispose();
        });
    }

    public static void pop() {
        pop(new JsonObject());
    }

    public static void pop(JsonObject parameters) {
        StackPane root = Main.getRoot();
        if (root.getChildren().size() > 1) {
            PushAndPopPane node = (PushAndPopPane)root.getChildren().get(root.getChildren().size() - 1);
            node.disable();
            PushAndPopPane nextNode = (PushAndPopPane)root.getChildren().get(root.getChildren().size() - 2);
            nextNode.disable();
            FadeTransition ft = new FadeTransition(Duration.millis(1000.0D), node);
            ft.setFromValue(1.0D);
            ft.setToValue(0.0D);
            ft.play();
            ft.setOnFinished((e) -> {
                nextNode.setParameters(parameters);
                nextNode.enable();
                root.getChildren().remove(node);
                node.dispose();
            });
        }

    }
}
