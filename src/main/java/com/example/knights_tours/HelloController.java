package com.example.knights_tours;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class HelloController {

    private HelloApplication app;
    private Button startButton;
    private Button stepButton;
    private Label rowLabel;
    private Label colLabel;
    private TextField rowTextField;
    private TextField colTextField;
    private AnchorPane anchorPane;
    private Canvas canvas;
    private GraphicsContext gc;
    private int size = 50;

    public HelloController(HelloApplication app) {
        this.app = app;
        anchorPane = new AnchorPane();

        createGui();
        attachListener();


    }

    private void attachListener() {
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                handleButtonClick(actionEvent);
            }
        });
        stepButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                handleButtonClick(actionEvent);
            }
        });
    }

    private void handleButtonClick(ActionEvent actionEvent) {
        if (actionEvent.getSource() == startButton) {
            String buttonText = startButton.getText();
            if (buttonText.equals("Start")) {
                Location currentLoc = app.getCurrentLoc();
                if (currentLoc == null) {
                    int row = Integer.parseInt(rowTextField.getText());
                    int col = Integer.parseInt(colTextField.getText());
                    app.setStartLoc(new Location(row, col));
                }
                startButton.setText("Stop");
                app.setRunning(true);
            } else {
                startButton.setText("Start");
                app.setRunning(false);
            }
        }
        else if (actionEvent.getSource() == stepButton) {
            Location currentLoc = app.getCurrentLoc();
                if (currentLoc == null) {
                    int row = Integer.parseInt(rowTextField.getText());
                    int col = Integer.parseInt(colTextField.getText());
                    app.setStartLoc(new Location(row, col));
                    app.setStep(true);
                } else {
                    app.setStep(true);
                }

            }
        }



    private void createGui() {
        canvas = new Canvas(1024,780);
        gc = canvas.getGraphicsContext2D();
        //gc.setFill(Color.RED);
        //gc.fillRect(0,0,600,500);
        AnchorPane.setLeftAnchor(canvas, 100.0);
        AnchorPane.setTopAnchor(canvas, 100.0);
        anchorPane.getChildren().add(canvas);

        rowLabel = new Label("row");
        AnchorPane.setTopAnchor(rowLabel,100.0);
        AnchorPane.setRightAnchor(rowLabel, 200.0);
        anchorPane.getChildren().add(rowLabel);

        colLabel = new Label("col");
        AnchorPane.setTopAnchor(colLabel,130.0);
        AnchorPane.setRightAnchor(colLabel, 200.0);
        anchorPane.getChildren().add(colLabel);

        rowTextField = new TextField();
        rowTextField.setPrefWidth(50);
        AnchorPane.setTopAnchor(rowTextField,97.0);
        AnchorPane.setRightAnchor(rowTextField,135.0);
        anchorPane.getChildren().add(rowTextField);

        colTextField = new TextField();
        colTextField.setPrefWidth(50);
        AnchorPane.setTopAnchor(colTextField,127.0);
        AnchorPane.setRightAnchor(colTextField,135.0);
        anchorPane.getChildren().add(colTextField);

        startButton = new Button("Start");
        startButton.setPrefWidth(100);
        AnchorPane.setTopAnchor(startButton,170.0);
        AnchorPane.setRightAnchor(startButton,140.0);
        anchorPane.getChildren().add(startButton);

        stepButton = new Button("Step");
        stepButton.setPrefWidth(100);
        AnchorPane.setTopAnchor(stepButton,210.0);
        AnchorPane.setRightAnchor(stepButton,140.0);
        anchorPane.getChildren().add(stepButton);



    }

    public AnchorPane getPane(){
        return anchorPane;
    }
    public void draw() {
        Location currentLoc = app.getCurrentLoc();
        for (int row = 0; row < HelloApplication.numRows; row++) {
            for (int col = 0; col < HelloApplication.numCols; col++) {
                if (currentLoc != null && row == currentLoc.getRow() && col == currentLoc.getCol()) {
                    drawSingleBox(col * size, row * size, size, 2, Color.RED);
                    drawPossibleMoves();
                } else {
                    drawSingleBox(col * size, row * size, size, 2, Color.BURLYWOOD);
                }
                gc.strokeText(""+app.getBoard()[row][col], col*size+22, row*size+28);
            }
        }
    }


    public void drawPossibleMoves(){
        ArrayList<Location> locations = app.getPossibleMoves(app.getCurrentLoc());
        for(int i=0; i< locations.size();i++){
            drawSingleBox(locations.get(i).getCol() * size, locations.get(i).getRow() * size, size, 2, Color.BLUE);
        }

    }

    public void drawExaustedList(){

    }

    public void drawSingleBox(int x, int y, int size, int stroke, Paint color){
        gc.setFill(Color.BLACK);
        gc.fillRect(x,y,size,size);
        gc.setFill(color);
        gc.fillRect(x+stroke,y+stroke, size-(stroke*2), size-(stroke*2));

    }
}