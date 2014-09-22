/* --------------------------------------------------------------------------
 * Linetic Sender - gesture recognition engine 
 * --------------------------------------------------------------------------
 * prog:  Loris Leiva
 * date:  10/08/2014 (m/d/y)
 * ver:   1.0
 * ----------------------------------------------------------------------------
 */

// === setup ==================================================

boolean useFullscreen = false;
int nbrOfMoves = 11; // TODO : back to 10
int nbrOfPerson = 2;
float maxCost = 0;
float minCost = 10000;

// === variables defined by XML ===============================

// gener
boolean autoPoseDetection = false;
boolean useMultiThreading = true;

// for all gestures
boolean NORMALIZE_SIZE = true;
int framesGestureMax = 25;

// default gesture settings
float defaultWeightX = 1.0;
float defaultWeightY = 1.0;
float defaultWeightZ = 1.0;
float defaultWeightLeftOrRight = 0.0;
boolean defaultNormRotation = true;
int defaultFramesGesture = framesGestureMax;


// === libraries ==============================================

// import fullscreen.*; 
// FullScreen fs; 

import SimpleOpenNI.*;
import oscP5.*;
import netP5.*;
import java.util.Map;
import java.util.List;
import java.text.Normalizer;
import java.util.Locale;
// import processing.opengl.*;


// === globals variables ======================================

SimpleOpenNI context;
int framesInputMax = 2*framesGestureMax;

int counterEvent = 0;

// Relative Array of objects
Move[] moves;
Map<Integer, User> users;
User highlightedUser = null;

int steps[];
float speed[];
PGraphics pg;

Data data;
Server server;
GUI gui;



/* =====================================================================================
 setup
 ===================================================================================== */
void setup()
{
  // Instanciations
  users = new HashMap<Integer, User>();
  data = new Data();
  server = new Server();

  // Arrays instanciations
  steps = new int[nbrOfMoves];
  speed = new float[nbrOfMoves];
  moves = new Move[nbrOfMoves];
  for (int i = 0; i < nbrOfMoves; i++)
    moves[i] = new Move(i);

  // Parse XML setup file
  parseXML();

  // Load Kinect
  if (useMultiThreading)
    context = new SimpleOpenNI(this, SimpleOpenNI.RUN_MODE_MULTI_THREADED); 
  else
    context = new SimpleOpenNI(this);
  if (context.isInit() == false) {
    println("Can't init SimpleOpenNI, maybe the camera is not connected!"); 
    exit();
    return;
  }

  // Load existing moves
  for (int i = 0; i < nbrOfMoves; i++)
    moves[i].load();

  // Enable depthMap generation & skeleton for particular joints
  context.enableDepth();
  context.enableUser();

  // Setup the GUI
  gui = new GUI();
}


void draw()
{
  gui.drawMainPage();
}


// -----------------------------------------------------------------
// SimpleOpenNI events

void onNewUser(SimpleOpenNI kinect, int userId)
{
  if (users.size() > nbrOfPerson) {
    println(">> Error : Maximum number of person reached : " + nbrOfPerson);
    return;
  }
    
  User user = new User(userId);
  if (users.isEmpty())
    highlightedUser = user;
    
  users.put(userId, user);
  kinect.startTrackingSkeleton(userId);
  
  println("onNewUser - userId: " + userId);
  user.hello();
}

void onLostUser(SimpleOpenNI kinect, int userId)
{
  User user = users.get(userId);
  users.remove(userId);
  if (users.isEmpty())
    highlightedUser = null;
  else if (highlightedUser.id == userId)
    highlightedUser = getRandomUser();
    
  context.stopTrackingSkeleton(userId);
  user.bye();
}

void onCompletedGesture(SimpleOpenNI context, int gestureType, PVector pos)
{
  println("onCompletedGesture - gestureType: " + gestureType + ", pos: " + pos);

  int handId = context.startTrackingHand(pos);
  println("hand stracked: " + handId);
}

void onNewHand(SimpleOpenNI curContext, int handId, PVector pos) {
}
void onTrackedHand(SimpleOpenNI curContext, int handId, PVector pos) {
}
void onLostHand(SimpleOpenNI curContext, int handId) {
}

// -----------------------------------------------------------------
// Keyboard events

void keyPressed()
{  
  if ( (key >= '0') && (key <= '9') && (highlightedUser != null) )
  {
    int keyIndex = key-'0';

    println("POSE " + keyIndex + " SAVED");
    moves[keyIndex].capture();
    gui.update();
  }

  switch(key)
  {
  case 'c': 
    pg.save ("capture.png");
    break; 

  case '+':
    gui.nextMove();
    break;

  case '-':
    gui.prevMove();
    break;

  case 'd':
    gui.switchDisplay();
    break;
  }
}

