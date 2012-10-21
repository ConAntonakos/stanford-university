/*
 * File: Breakout.java
 * -------------------
 * Name: Constantine Antonakos
 * Section Leader:
 *
 * This file will eventually implement the game of Breakout.
 */
 
import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {
     
    /** Width and height of application window in pixels */
    public static final int APPLICATION_WIDTH = 400;
    public static final int APPLICATION_HEIGHT = 600;
 
    /** Dimensions of game board (usually the same) */
    private static final int WIDTH = APPLICATION_WIDTH;
    private static final int HEIGHT = APPLICATION_HEIGHT;
 
    /** Dimensions of the paddle */
    private static final int PADDLE_WIDTH = 60;
    private static final int PADDLE_HEIGHT = 10;
 
    /** Offset of the paddle up from the bottom */
    private static final int PADDLE_Y_OFFSET = 30;
 
    /** Number of bricks per row */
    private static final int NBRICKS_PER_ROW = 10;
 
    /** Number of rows of bricks */
    private static final int NBRICK_ROWS = 10;
 
    /** Separation between bricks */
    private static final int BRICK_SEP = 4;
 
    /** Width of a brick */
    private static final int BRICK_WIDTH =
      (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;
 
    /** Height of a brick */
    private static final int BRICK_HEIGHT = 8;
 
    /** Radius of the ball in pixels */
    private static final int BALL_RADIUS = 10;
 
    /** Offset of the top brick row from the top */
    private static final int BRICK_Y_OFFSET = 70;
 
    /** Number of turns */
    private static final int NTURNS = 3;
    private int lives = NTURNS;
     
    /** Pause time of ball animation */
    private static final int PAUSE_TIME = 5;
     
    /** Point value of a brick */
    private static final int BRICK_VALUE = 10;
         
    /** Initializes score tracker */
    private int scoreTracker = 0;
    private GLabel scoreLabel = new GLabel("Score: " + scoreTracker, 0, 20);
     
    private GLabel livesLabel = new GLabel("Lives: " + lives, 325, 20);
     
    /** Standard fonts */
    private static final int MESSAGE_FONT = 30;
    private static final int SCORE_FONT = 20;
     
     
     
    /* Define your instance variables here */
    private int bricksLeft;
    private GObject collidedObject;
    private GRect paddle;
    private GOval ball;
    private int collisionWithPaddle = 0;
    private double vx, vy; /** Ball velocity */
    private RandomGenerator rgen = RandomGenerator.getInstance();
    private GLabel serveMessage; /** "Click to Serve" Message */
    private GLabel winner; /** Prints the "Winner" Message */
    private GLabel loser; /** Prints the "Loser" Message */
    AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
 
     
    /** Runs the Breakout program. */
    public void run() {
        setupGame();
        addMouseListeners();
        displayServeMessage();
        while (!gameOver() || bricksLeft != 0){
        	playGame();
            if (gameOver()){
            	removeAll();
                displayLoser(); 
            }
            if (bricksLeft == 0){
            	removeAll();
                displayWinner();
            }
        }
    }
 
 
    //The main setup function for the game (colored block of bricks and paddle)
    private void setupGame() {
        setupBlockOfBricks();
        createPaddle();
        add(scoreLabel);
        scoreLabel.setFont(new Font("Comic Sans Serif", Font.BOLD, SCORE_FONT));
        add(livesLabel);
        livesLabel.setFont(new Font("Comic Sans Serif", Font.BOLD, SCORE_FONT));
    }
 
     
    //Creates the solid black paddle used to bounce the ball and play (and subsequently, win) the game
    private void createPaddle() {
        int startX = getWidth()/2;
        int startY = HEIGHT - PADDLE_Y_OFFSET;
        paddle = new GRect(startX, startY, PADDLE_WIDTH, PADDLE_HEIGHT);
        paddle.setFillColor(Color.BLACK);
        paddle.setFilled(true);
        add(paddle);
    }
     
    //Allows for manipulation of mouse movements
    public void mouseMoved(MouseEvent e){
        int mouseX = e.getX();
        paddle.setBounds(mouseX - PADDLE_WIDTH/2, HEIGHT - PADDLE_Y_OFFSET, PADDLE_WIDTH, PADDLE_HEIGHT);
         
        //Constrain paddle movement within the boundaries of application window
        if (paddle.getX() < 0){
            paddle.setLocation(0, HEIGHT - PADDLE_Y_OFFSET);
        } else if (paddle.getX() + PADDLE_WIDTH > WIDTH ){
            paddle.setLocation(WIDTH - PADDLE_WIDTH, HEIGHT - PADDLE_Y_OFFSET);
        }
    }
 
 
     
    //Draws the block of bricks to setup the start of the game
    private void setupBlockOfBricks() {
        int startX = 0;
        int startY = BRICK_Y_OFFSET;
        int numBrickRows = NBRICK_ROWS;
        int numBricksPerRow = NBRICKS_PER_ROW;
        for (int i = 0; i < numBrickRows; i++){
            drawSingleBrickRow(startX, startY, numBricksPerRow, i);
            startY += BRICK_HEIGHT + BRICK_SEP;
        }
    }
 
    //Draws a single row of bricks
    private void drawSingleBrickRow(int startX, int startY, int numBricksPerRow, int rowNum) {
        for (int j = 0; j < numBricksPerRow; j++){
            drawBrick(startX, startY, rowNum);
            bricksLeft++;
            startX += BRICK_WIDTH + BRICK_SEP;
        }
         
    }
 
 
    //Draws a single brick; color depends on row number(rowNum)
    private void drawBrick(int x, int y, int rowNum) {
        GRect coloredBrick = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
        add(coloredBrick);
        coloredBrick.setFilled(true);
        //This list of "if"s provides the color scheme of the bricks from top to bottom
        if (rowNum > 9){
        	rowNum = rowNum % 10;
        }
        
        if (rowNum < 2){
            coloredBrick.setColor(Color.RED);
        }
        else if (rowNum == 2 || rowNum == 3){
            coloredBrick.setColor(Color.ORANGE);
        }
        else if (rowNum == 4 || rowNum == 5){
            coloredBrick.setColor(Color.YELLOW);
        }
        else if (rowNum == 6 || rowNum == 7){
            coloredBrick.setColor(Color.GREEN);
        }
        else if (rowNum == 8 || rowNum == 9){
            coloredBrick.setColor(Color.CYAN);
        }
         
    }
 
 
    //Initiates the objects and physics to begin playing
    private void playGame() {
        waitForClick();
        createBall();
        setBall();
        remove(serveMessage);
        while (true){
            moveBall();
            checkForCollisions();
            if (gameOver() || bricksLeft == 0)break;
        }
    }
 
 
 
 
    //Function that creates the ball and makes it bounce around the application window
    private void createBall() {
        int startX = getWidth()/2;
        int startY = getHeight()/2;
        int diameter = BALL_RADIUS*2;
        ball = new GOval(startX - BALL_RADIUS, startY - BALL_RADIUS, diameter, diameter);
        ball.setFillColor(Color.BLACK);
        ball.setFilled(true);
        add(ball);
         
    }
     
    //Set the ball up for motion
    private void setBall() {
        vx = rgen.nextDouble(1.0, 3.0);
        if (rgen.nextBoolean(0.5)) vx = -vx;
        vy = rgen.nextDouble(1.5, 3.0);
        if (rgen.nextBoolean(0.5)) vy = -vy;
         
    }
     
    //Moves the ball and pauses so the movement can be registered by the player */
    private void moveBall(){
        ball.move(vx, vy);
        ball.pause(PAUSE_TIME);
 
    }
     
    private void checkForCollisions(){    
        //If the ball reaches the left and right of the application window
        if (ball.getX() + 2 * BALL_RADIUS >= WIDTH || ball.getX() <= 0) {
            vx = -vx;
        }
        //If ball reaches the top of the application window
        if (ball.getY() <= 0) {
            vy = -vy;
        }
        //If ball reaches height of application (below paddle)
        if (ball.getY() - 2* BALL_RADIUS >= HEIGHT) {
            lives--;
            livesLabel.setLabel("Lives: " + lives);
            remove(ball);
             
            if (!gameOver()){
                add(serveMessage);
                waitForClick();
                remove(serveMessage);
                createBall();
            } else {
                return;
            }
        }
        collidedObject = collisionDetection();
         
        //Detect collision with paddle
        if (collidedObject == paddle){
            collisionWithPaddle++;
            vy = -Math.abs(vy);
            bounceClip.play();
            if (collisionWithPaddle == 10){
                vx = vx*1.5;
            }
             
        }
         
         
        //Detect collision with anything else (bricks) and act accordingly
        else if ((collidedObject != null) && (collidedObject != scoreLabel) &&(collidedObject != livesLabel)){ //|| collidedObject != scoreLabel || collidedObject != livesLabel){
            remove(collidedObject);
            bricksLeft--;
            bounceClip.play();
            scoreTracker += BRICK_VALUE;
            scoreLabel.setLabel("Score: " + scoreTracker);
            vy = -vy;          
        }
    }
     
    //Checks for collision with the ball
    private GObject collisionDetection() {
 
        if((getElementAt(ball.getX(), ball.getY())) != null) {
            return getElementAt(ball.getX(), ball.getY());
        } else if (getElementAt( (ball.getX() + BALL_RADIUS*2), ball.getY()) != null ){
            return getElementAt(ball.getX() + BALL_RADIUS*2, ball.getY());
        } else if(getElementAt(ball.getX(), (ball.getY() + BALL_RADIUS*2)) != null ){
            return getElementAt(ball.getX(), ball.getY() + BALL_RADIUS*2);
        } else if(getElementAt((ball.getX() + BALL_RADIUS*2), (ball.getY() + BALL_RADIUS*2)) != null ){
            return getElementAt(ball.getX() + BALL_RADIUS*2, ball.getY() + BALL_RADIUS*2);
        }
        //If there are no objects present
        else {
            return null;
        }
 
    }
     
    //Game over once all lives(turns) are lost
    private boolean gameOver(){
        return lives == 0;
    }
     
     
    //Displays the "Winner" message
    public void displayWinner(){
        winner = new GLabel("Congratulations! You won!");
        winner.setColor(Color.BLUE);
        winner.setFont(new Font("Comic Sans Serif", Font.BOLD, MESSAGE_FONT));
        winner.setLocation(WIDTH / 2 - winner.getWidth()/2, HEIGHT/2 - winner.getHeight());
        remove(ball);
        add(winner);
    }
 
    //Displays the "Loser" message
    public void displayLoser(){
        loser = new GLabel("GAME OVER :(");
        loser.setColor(Color.RED);
        loser.setFont(new Font("Comic Sans Serif", Font.BOLD, MESSAGE_FONT));
        loser.setLocation(WIDTH/2 - loser.getWidth()/2, HEIGHT/2 - loser.getHeight());
        remove(ball);
        add(loser);
    }
    
    public void displayServeMessage(){
        serveMessage = new GLabel("Click to Serve!");
        serveMessage.setFont(new Font("Comic Sans Serif", Font.BOLD, MESSAGE_FONT));
        serveMessage.setLocation(WIDTH/2 - serveMessage.getWidth()/2, HEIGHT/2 - serveMessage.getHeight());
        add(serveMessage);    	
    }
    
//    public static void music(){
//    	AudioPlayer MGP = AudioPlayer.player;
//    	AudioStream BGM;
//    	AudioData MD;
//    ContinuousAudioDataStream loop = null;
//    try{
//    BGM = new AudioStream(new FileInputStream("avicii.mp3"));
//    MD = BGM.getData();
//    loop = new ContinuousAudioDataStream(MD);
//    } catch(IOException error){}
//    
//    MGP.start(loop);
//    }
//    
    
         
}