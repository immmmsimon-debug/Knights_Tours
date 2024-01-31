package com.example.knights_tours;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class HelloApplication extends Application {

    private AnimationTimer animationTimer;
    public static int numRows =8;
    public static int numCols = 8;
    private int board[][] = new int[numRows][numCols];
    private Stack<Location> stack = new Stack<>();
    private ArrayList<ArrayList<Location>> exhausted = new ArrayList<ArrayList<Location>>(64);
    private Location currentLoc;
    private boolean isRunning = false;
    private boolean step;
    private Location startingLoc;
    private int counter = 0;




    @Override
    public void start(Stage stage) throws IOException {
        HelloController hc = new HelloController(this);
        Scene rootScene = new Scene(hc.getPane(), 1024,768);
        stage.setTitle("Knight's Tour");
        stage.setScene(rootScene);

        initExhausted();
        //loop the program
        animationTimer = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long currentNanoTime) {
                if(isRunning || step){
                    if(currentNanoTime - lastUpdate >= 52){
                        //run the tour
                        currentLoc= getNextMove(getPossibleMoves(currentLoc));
                        System.out.println("---"+stack+"---");

                        //last line
                        lastUpdate=  currentNanoTime;

                        if(step){
                            step = false;
                        }
                    }
                }
                hc.draw();
            }
        };
        animationTimer.start();

        stage.show();
    }
    public Location getCurrentLoc(){
        return currentLoc;
    }
    public void setStartLoc(Location loc){
        currentLoc = loc;
        startingLoc = loc;
        board[currentLoc.getRow()][currentLoc.getCol()] = 1;
    }
    public void setRunning(boolean val){
        isRunning = val;
    }
    public void setStep(boolean val){
        step = val;
    }
    private void initExhausted() {
        for(int i = 0; i < numCols*numRows; i++){
            exhausted.add(new ArrayList<Location>());
        }
    }

    public int[][] getBoard() {
        return board;
    }


    // clear the exhausted or tried location for a give Location loc
    public void clearExhausted(Location loc){
        exhausted.get(convertLocToIndex(loc)).clear();
    }
    //add to the source Location, we have tried the dest location
    public void addToExhausted(Location  source, Location dest){
        exhausted.get(convertLocToIndex(source)).add(dest);
    }
    //Given an Arraylist of possible, valid moves, choose one
    public Location getNextMove(ArrayList<Location> locs) {
        int min = 10;
        if(counter==numRows*numCols-1){
            board[currentLoc.getRow()][currentLoc.getCol()] = counter;
            return null;
        }

        if (locs.size() == 0) {
            board[currentLoc.getRow()][currentLoc.getCol()]=0;
            clearExhausted(currentLoc);
            stack.remove(stack.size()-1);
            counter--;
            return stack.get(stack.size()-1);

        } else {
            Location next = locs.get(0);
            for (int i = 0; i < locs.size(); i++) {
                Location temp = locs.get(i);
                getPossibleMoves(temp).size();
                if (getPossibleMoves(temp).size() < min) {
                    min = getPossibleMoves(temp).size();
                    next = locs.get(i);
                }
            }


            addToExhausted(currentLoc, next);
            counter++;
            board[currentLoc.getRow()][currentLoc.getCol()] = counter;
            stack.add(next);
            return next;
        }


    }



    //return true if the destination is in the source exhausted list false otherwise
    public boolean inExhausted(Location source, Location dest){

        for(int i=0;i<exhausted.get(convertLocToIndex(source)).size();i++){
            if(exhausted.get(convertLocToIndex(source)).get(i).equals(dest)){
                return true;
            }
        }

        return false;
    }

    //return all the possible move from the Location loc. The moves must be valid, non-visted, and not in exhausted list
    public ArrayList<Location> getPossibleMoves(Location loc){
        ArrayList<Location> moves = new ArrayList<>();
        moves.add(new Location(loc.getRow()-2,loc.getCol()-1));
        moves.add(new Location(loc.getRow()-2,loc.getCol()+1));
        moves.add(new Location(loc.getRow()-1,loc.getCol()-2));
        moves.add(new Location(loc.getRow()-1,loc.getCol()+2));


        moves.add(new Location(loc.getRow()+1,loc.getCol()-2));
        moves.add(new Location(loc.getRow()+1,loc.getCol()+2));
        moves.add(new Location(loc.getRow()+2,loc.getCol()-1));
        moves.add(new Location(loc.getRow()+2,loc.getCol()+1));

        for(int i=0;i< moves.size();i++){
            if(isValid(moves.get(i))) {
                if (inExhausted(loc, moves.get(i)) || board[moves.get(i).getRow()][moves.get(i).getCol()] >0) {
                    moves.remove(i);
                    i--;

                }
            }else{
                moves.remove(i);
                i--;

            }
        }
        return  moves;
    }

    public boolean isValid(Location loc){
        return loc.getRow() >= 0 && loc.getRow() < board.length && loc.getCol() >= 0 && loc.getCol() < board[(loc.getRow())].length;
    }
    //revert a location to an index in the exhausted list
    public int convertLocToIndex(Location loc){
        return (loc.getRow()*numCols) + loc.getCol();
    }
    public static void main(String[] args) {
        launch();
    }
}