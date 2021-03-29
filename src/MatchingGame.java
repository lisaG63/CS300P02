//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:           P02 Matching Game
// Files:           MatchingGame.java
// Course:          CS300, fall, 2019
//
// Author:          Weihang Guo
// percentage:           wguo63@wisc.edu
// Lecturer's Name: Mouna Kacem
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// Partner Name:    Jiaqi Zhang
// Partner percentage:   jzhang2247@wisc.edu
// Partner Lecturer's Name: Mouna Kacem
//
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
// _X__ Write-up states that pair programming is allowed for this assignment.
// _X__ We have both read and understand the course Pair Programming Policy.
// _X__ We have registered our team prior to the team registration deadline.
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully 
// acknowledge and credit those sources of help here.  Instructors and TAs do 
// not need to be credited here, but tutors, friends, relatives, room mates, 
// strangers, and others do.  If you received no outside help from either type
//  of source, then please explicitly indicate NONE.
//
// Milks: None
// Online Sources: None
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////

import java.io.File;
import java.util.Random;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * This class contains the main functions of the matching game.
 * 
 * @author Weihang Guo, Jiaqi Zhang
 * @version 1.0
 */
public class MatchingGame {
  
//Congratulations message
private final static String CONGRA_MSG = "CONGRATULATIONS! YOU WON!";
//Cards not matched message
private final static String NOT_MATCHED = "CARDS NOT MATCHED. Try again!";
//Cards matched message
private final static String MATCHED = "CARDS MATCHED! Good Job!";
//2D-array which stores cards coordinates on the window display
private final static float[][] CARDS_COORDINATES =
new float[][] {{170, 170}, {324, 170}, {478, 170}, {632, 170},
{170, 324}, {324, 324}, {478, 324}, {632, 324},
{170, 478}, {324, 478}, {478, 478}, {632, 478}};
//Array that stores the card images filenames
private final static String[] CARD_IMAGES_NAMES = new String[] {"apple.png",
"ball.png", "peach.png", "redFlower.png", "shark.png", "yellowFlower.png"};

  /**
  * Defines the initial environment properties of this game as the program starts
  */
  public static void setup(PApplet processing) {
    //Set the processing class variable to the one passed as input parameter
    MatchingGame.processing = processing;
    // Set the color used for the background of the Processing window
    processing.background(245, 255, 250); // Mint cream color
    //Set the images array to have the same length as CARD IMAGES NAMES array.
    images = new PImage[CARD_IMAGES_NAMES.length];
    //Initiate the content of images array.
    for (int i = 0; i < images.length; i ++) {
      images[i] = processing.loadImage("images" + File.separator + CARD_IMAGES_NAMES[i]);
    }
    //Call the initGame() method so that it what be called every time the program runs. 
    initGame();
    }
    
  /**
   * Initializes the Game
   */
  public static void initGame() {
    //Initialize the static fields to the default values.
    randGen = new Random(Utility.getSeed());
    selectedCard1 = null;
    selectedCard2 = null;
    matchedCardsCount = 0;
    winner = false;
    message = "";
    cards = new Card[CARD_IMAGES_NAMES.length*2];//Set the cards array to have 12 elements.
    int random = 0; //Store the number that would be used as a random index.
    
    /* Create an array which stores boolean variables that 
     * check whether one coordinate is already assigned.
     */
    boolean isAssigned[] = new boolean[CARDS_COORDINATES.length]; 
    //Mix up the cards and draw the cards.
    for (int i = 0; i < cards.length; i ++) {
      // Only store the number in random when the number hasn't been assigned.
      do {
        random = randGen.nextInt(12);
        } while (isAssigned[random]);
      isAssigned[random] = true;
      cards[i] = new Card(images[i/2], CARDS_COORDINATES[random][0], 
        CARDS_COORDINATES[random][1]);
    }
  }
  
  /**
   * Callback method called each time the user presses a key
   */
  public static void keyPressed() {
    if(processing.key == 'N' || processing.key == 'n') {
      initGame();
    }
  }
  
  /**
   * Displays a given message to the display window
   * @param message to be displayed to the display window
   */
  public static void displayMessage(String message) {
  processing.fill(0);
  processing.textSize(20);
  processing.text(message, processing.width / 2, 50);
  processing.textSize(12);
  }
  
  /**
   * Callback method draws continuously this application window display
   */
  public static void draw() {
    processing.background(245, 255, 250);//Set the color of the background.
    //Draw the different cards.
    for (int i = 0; i < cards.length; i ++) {
      cards[i].draw();
    }
    displayMessage(message);// draw the class variable message to the application display window.
  }
  
  /**
   * Checks whether the mouse is over a given Card
   * @return true if the mouse is over the storage list, false otherwise
   */
  public static boolean isMouseOver(Card card) {
    //Get the coordinates of boundaries of an image.
    int minX = (int)(card.getX() - card.getWidth()/2);
    int maxX = minX + card.getWidth();
    int minY = (int)(card.getY() - card.getHeight()/2);
    int maxY = minY + card.getHeight();
    
    //Check whether the mouse is in the given boundary.
    if (processing.mouseX > minX && processing.mouseX < maxX && processing.mouseY > minY
        && processing.mouseY < maxY) {
      return true;
    }
    return false;
  }

  
  /**
  * Callback method called each time the user presses the mouse
  */
  public static void mousePressed() {
    if(!winner) { // Only when the user has not won the game, the lines below will be operated.
      for (int i = 0; i < cards.length; i ++) {
        /* When a new invisible card is clicked, set it to be visible and selected, 
         * and assign it to either selectedCard1 or selectedCard2.
         */
        if(!cards[i].isVisible() && isMouseOver(cards[i])) {
          cards[i].setVisible(true);
          cards[i].select();
          reset();
          if(selectedCard1 == null) {
            selectedCard1 = cards[i];
          }
          else if(selectedCard2 == null) {
            selectedCard2 = cards[i];
          }
          chooseMessage();
        }
      }
    }
    
    //When all images are matched, display the congratulation message.
    if (winner) {
      message = CONGRA_MSG;
    }
    
  }
  
  /**
   * Choose the right message to display according to the matching result.
   */
  private static void chooseMessage() {
    if(selectedCard2 != null) {
      if(matchingCards(selectedCard1, selectedCard2)) {
        message = MATCHED;
        //If two cards match, choose the message that encourages the user.
        matchedCardsCount += 2;
        //Two cards has matched, so 2 should be added to the matchedCardsCount.
      } else {
        message = NOT_MATCHED;
        //If two cards doesn't match, choose the message that lets the user try again.
      }
      
      if(matchedCardsCount == 12) {
        winner = true;
        //When all 12 cards match, the user wins the game, and the boolean winner is set to true.
      }
    }
  }
  
  /**
   * Reset selectedCard1, selectedCard2 and the previous two cards.
   */
  private static void reset() {
    if(selectedCard2 != null ) {
      selectedCard1.deselect();
      selectedCard2.deselect();  
      //When clicking on a new image, the previous two images should be deselected.
      if (!matchingCards(selectedCard1, selectedCard2)) {
        selectedCard1.setVisible(false);
        selectedCard2.setVisible(false);
        //Turn the previous two unmatched cards back over when a new image is clicked.
      }
      selectedCard1 = null;
      selectedCard2 = null;
      //Set these two variables to null so new references can be assigned.
    }  
  }
  
  /**
  * Checks whether two cards match or not
  * @param card1 reference to the first card
  * @param card2 reference to the second card
  * @return true if card1 and card2 image references are the same, false otherwise
  */
  public static boolean matchingCards(Card card1, Card card2) {
    //Check whether the two cards refer to the same reference.
    if(card1.getImage() == card2.getImage()) {
        return true;
        }
    return false;
  }

  
  public static void main(String[] args) {
    Utility.runApplication(); // starts the application
  }

  private static PApplet processing; // PApplet object that represents
//the graphic display window
private static Card[] cards; // one dimensional array of cards
private static PImage[] images; // array of images of the different cards
private static Random randGen; // generator of random numbers
private static Card selectedCard1; // First selected card
private static Card selectedCard2; // Second selected card
private static boolean winner; // boolean evaluated true if the game is won,
//and false otherwise
private static int matchedCardsCount; // number of cards matched so far
//in one session of the game
private static String message; // Displayed message to the display window

}
