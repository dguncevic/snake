package com.snake;

import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * JavaFX App
 */
public class App extends Application {

    ArrayList<Rectangle> snake;
    double directionX = 0;
    double directionY = 0;
    Circle circle = new Circle(200, 200, 10);
    int counter;
    AnchorPane anchor;
    Label label = new Label("0");
    Timeline timeLine;
    Scene scene;
    boolean gameStarted = false;

    @Override
    public void start(Stage stage) {

        anchor = new AnchorPane();
        anchor.setBackground(Background.fill(Color.BLACK));

        snake = getNewSnake();

        anchor.getChildren().addAll(snake);

        circle.setFill(Color.RED);

        scene = new Scene(new StackPane(anchor), 700, 500);
        scene.setOnKeyPressed(this::sceneKeyPressed);

        label.setFont(new Font("Consolas", 32));
        label.setTextFill(Color.YELLOW);
        label.setLayoutX(scene.getWidth() / 2);
        label.setLayoutY(20);

        anchor.getChildren().addAll(circle, label);

        KeyFrame keyFrame = new KeyFrame(Duration.millis(100), this::move);
        timeLine = new Timeline(keyFrame);
        timeLine.setCycleCount(Animation.INDEFINITE);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void sceneKeyPressed(KeyEvent e) {

        if (e.getCode() == KeyCode.W || e.getCode() == KeyCode.UP) {
            directionY = -20;
            directionX = 0;
            gameStarted = true;
        } else if (e.getCode() == KeyCode.S || e.getCode() == KeyCode.DOWN) {
            directionY = 20;
            directionX = 0;
            gameStarted = true;
        } else if (e.getCode() == KeyCode.D || e.getCode() == KeyCode.RIGHT) {
            directionX = 20;
            directionY = 0;
            gameStarted = true;
        } else if (e.getCode() == KeyCode.A || e.getCode() == KeyCode.LEFT) {
            directionX = -20;
            directionY = 0;
            gameStarted = true;
        } else if (e.getCode() == KeyCode.SPACE) {
            gameStarted = false;
        } else if (e.getCode() == KeyCode.N) {
            resetGame();
        }

        if (gameStarted) {
            timeLine.play();
        } else {
            timeLine.pause();
        }

    }

    public void move(ActionEvent e) {

        for (int i = snake.size() - 1; i > 0; i--) {
            snake.get(i).setX(snake.get(i - 1).getX());
            snake.get(i).setY(snake.get(i - 1).getY());
        }
        snake.get(0).setX(snake.get(0).getX() + directionX);
        snake.get(0).setY(snake.get(0).getY() + directionY);

        // detecting colision to red dot
        Shape intersect = Shape.intersect(snake.get(0), circle);
        if (intersect.getBoundsInLocal().getWidth() != -1) {
            label.setText("" + ++counter);
            circle.setCenterX(Math.random() * 700);
            circle.setCenterY(Math.random() * 500);
            Rectangle temp = new Rectangle(snake.get(snake.size() - 1).getX(), snake.get(snake.size() - 1).getY(), 15, 15);
            snake.add(temp);
            temp.setFill(Color.GREENYELLOW);
            anchor.getChildren().add(temp);
        }
        // check if snake hit itself
        for (int i = 1; i < snake.size(); i++) {
            Shape temp = Shape.intersect(snake.get(0), snake.get(i));
            if (temp.getBoundsInLocal().getWidth() != -1) {
                gameOver();
            }
        }

        // check if snake is outside screen
        if (snake.get(0).getX() < 0 || snake.get(0).getX() > scene.getWidth() || snake.get(0).getY() < 0 || snake.get(0).getY() > scene.getHeight()) {
            gameOver();
        }
    }

    private ArrayList<Rectangle> getNewSnake() {
        ArrayList<Rectangle> temp = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            Rectangle r = new Rectangle(i * 20 + 200, 15, 15, 15);
            temp.add(r);
            r.setFill(Color.GREENYELLOW);
        }
        temp.get(0).setFill(Color.GREEN);
        return temp;
    }

    private void resetGame() {
        directionX = 0;
        directionY = 0;
        anchor.getChildren().removeAll(snake);
        snake = getNewSnake();
        anchor.getChildren().addAll(snake);
        label.setText("0");
        counter = 0;
        timeLine.play();
    }

    private void gameOver() {
        label.setText("Game over!" + "\nYour score: " + counter);
        setInCenter(label);
        label.setTextFill(Color.RED);
        timeLine.stop();
    }

    private void setInCenter(Label label) {
        label.setLayoutX(scene.getWidth() / 2 - label.getWidth() / 2);
    }
}
