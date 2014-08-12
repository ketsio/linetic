class GUI {

  // Properties 
  boolean mirrored = false;
  boolean updateDisplay = true;
  int displayCost = 1;
  int padding = 15;

  // Pages
  boolean mainPage = true;

  // Move Grid
  int col;
  int row;

  // Fonts
  PFont fontA32;
  PFont fontA12;

  // Images
  PImage emptyImage;
  PImage[][] warnings;
  PImage shapeOfUser;  
  PImage logo;

  // Main page properties 
  // ...

  // Move pages properties 
  // ...

  public GUI() {
    this.setUp();
  }

  public void setUp() {
    // MOVE GRID ============================================
    col = 6;
    row = ceil(float(nbrOfMoves) / float(col));
    col = row*col - nbrOfMoves >= row ? ceil(float(nbrOfMoves) / float(row)) : col;
    println("col = " + col + ", row = " + row);

    // FONTS ================================================
    fontA32 = createFont("Arial", 32);
    fontA12 = createFont("Arial", 16);


    // IMAGES ===============================================
    loadImages();

    // APPLYING SETTINGS ====================================
    textFont(fontA32, 32);
    background(255);
    stroke(0, 0, 255);
    strokeWeight(3);
    smooth();

    size(1300, 650, JAVA2D);
    //println("w = " + context.depthWidth() + ", h = " + context.depthHeight());
    pg = createGraphics(context.depthWidth(), context.depthHeight());
    //pg = createGraphics(context.depthWidth(), context.depthHeight(), P2D);

    if (!useFullscreen) {
      // size(1070, 850, OPENGL); 
      // size(1070, 850);
    } else {
      // size(1280, 800, P2D);
      // fs = new FullScreen(this);
      // fs.enter();
    }

    // DISPLAY LOGO =========================================
    //    pushMatrix();
    //    rotate(-PI/2);
    //    image(logo, -780, 1180);
    //    popMatrix();
  }


  public void run() {
  }

  private void drawMainPage() {

    // update the cam
    context.update();

    // draw depthImageMap
    pg.beginDraw();
    pg.image(context.depthImage(), 0, 0);

    //ringbuffer[0].display();

    // process the skeleton if it's available
    foundSkeleton = false;
    person = 0;

    for (User u : users.values ())
    {
      if (context.isTrackingSkeleton(u.id) && person++ < nbrOfPerson)
      {
        evaluateSkeleton(u.id);
        foundSkeleton = true;
      }
    }

    pg.endDraw();

    //  counter2++;
    //  counter2 %= 25;
    //  if (counter2 == 0)
    //  {
    //    OscMessage myMessage = new OscMessage("/status");
    //    if (foundSkeleton) myMessage.add("tracking ...");
    //    if (!foundSkeleton) myMessage.add("looking for pose ...");
    //    oscP5.send(myMessage, myRemoteLocation);
    //  }


    if (!mirrored)
    {
      image(pg, padding, padding);
    } else
    {
      pushMatrix();
      translate(padding, padding);
      scale(-1.0, 1.0);
      image(pg, -pg.width, 0);
      popMatrix();
    }

    if (!foundSkeleton) 
    {
      stroke(0, 0, 0);
      fill(0, 0, 0);
      rect(context.depthWidth() + 2 * padding, padding, 200, context.depthHeight());

      stroke(0, 0, 255);
      fill(255, 0, 0);
      textFont(fontA32, 32);
      textAlign(CENTER);
      text("Please register user!", context.depthWidth() / 2, 40);
      image(shapeOfUser, 0, 0);
    } else if ((warning[0] >= 0) || (warning[1] >= 0))
    {
      if ((warning[0] >= 0) && (warning[1] < 0))
      {
        image(warnings[0][warning[0]], context.depthWidth()/2-100, context.depthHeight()/2 - 50);
      } else if ((warning[1] >= 0) && (warning[0] < 0))
      {
        image(warnings[1][warning[1]], context.depthWidth()/2-100, context.depthHeight()/2 - 50);
      } else
      {
        image(warnings[0][warning[0]], context.depthWidth()/2-100-75, context.depthHeight()/2 - 50);
        image(warnings[1][warning[1]], context.depthWidth()/2-100+75, context.depthHeight()/2 - 50);
      }
    }

    if (updateDisplay)
    {
      updateDisplay = false;

      PVector gridOrigin = new PVector(0, context.depthHeight() + padding);
      int gridWidth = width;
      int gridHeight = int(height - gridOrigin.y);

      textFont(fontA12, 16);
      textAlign(CENTER);
      rectMode(CORNER);
      fill(255);

      int cellWidth = (gridWidth - padding * (col + 1)) / col;
      int cellHeight = (gridHeight - padding * (row + 1)) / row;
      println("cellWidth = " + cellWidth + ", cellHeight = " + cellHeight);

      // Displaying the grid of moves (TODO : optimize offset calcul (more vars))
      int moveId;
      PVector cellOrigin = new PVector();
      for (int r = 0; r < row; r++)
        for (int c = 0; c < col; c++) {
          moveId = r * col + c;
          if (moveId < nbrOfMoves) {
            Move move = moves[moveId];

            cellOrigin.x = gridOrigin.x + padding + c * (cellWidth + padding);
            cellOrigin.y = gridOrigin.y + padding + r * (cellHeight + padding);
            if (move.empty) {
              image(emptyImage, cellOrigin.x, cellOrigin.y, cellWidth, cellHeight);
            }
            else if (move.image ==  null) {
              image(emptyImage, cellOrigin.x, cellOrigin.y, cellWidth, cellHeight); // TODO : create a 'noImage' png
            }
            else if (mirrored) {
              pushMatrix();
              scale(-1.0, 1.0);
              image(move.image, - (cellOrigin.x + cellWidth), cellOrigin.y, cellWidth, cellHeight);
              popMatrix();
            }
            else {
              image(move.image, cellOrigin.x, cellOrigin.y, cellWidth, cellHeight);
            }
            text(moveId, cellOrigin.x + 12, cellOrigin.y + 16);
          }
        }
    }

    // evaluate and draw DTW
    if (foundSkeleton)
    {

      noStroke();             
      fill(0, 0, 0);

      for (User user : users.values ()) {
        for (int i = 0; i < nbrOfMoves; i++)
        {
          Move move = moves[i];
          if (!move.empty)
          {
            user.rb.costLast[i] = user.rb.cost[i];
            user.rb.cost[i] = user.rb.pathcost(i);
            user.rb.cost[i] = (log(user.rb.cost[i]-1.0) - 5.5)/2.0;

            float matching = 10.0 * (user.rb.cost[i] - 0.25);

            fill(user.c);
            if ( user.rb.cost[i] <= 0.25 )
              fill(0, 255, 0);

            if (user.rb.cost[i] > 0.25 && user.rb.cost[i] < 0.35)
              fill(user.c, 200);

            // Positioning
            if (i < 5) rect(i * (context.depthWidth() + 400) / 5 + i*5, context.depthHeight() + 145 + 10*user.id, min(1.0, max(0.01, 1.0-user.rb.cost[i])) * ((context.depthWidth() + 400) / 5), 10);
            if (i >= 5) rect((i-5) * (context.depthWidth() + 400) / 5 + (i-5)*5, context.depthHeight() + 300 + 10*user.id, min(1.0, max(0.01, 1.0-user.rb.cost[i])) * ((context.depthWidth() + 400) / 5), 10);

            if (user.rb.cost[i] < 0.3 && user.rb.costLast[i] >= 0.3)
            {
              println("found gesture #" + i + " user : " + user.name);
              server.send(move, user);
            }
          }
        }
      }
    }
  }

  public void update() {
    updateDisplay = true;
  }

  public void switchDisplay() {
    mirrored = !mirrored;
    context.setMirror(mirrored);
    update();
  }

  public void editCost(int moveId) {
    displayCost = moveId;
    if (displayCost < 0) displayCost = 0;
    if (displayCost >= nbrOfMoves) displayCost = nbrOfMoves - 1;
  }

  public void prevCost() {
    editCost(--displayCost);
  }

  public void nextCost() {
    editCost(++displayCost);
  }



  /************************************
   **** PRIVATE AUXILIARY FONCTIONS ****
   ************************************/
  private void loadImages() {

    warnings = new PImage[2][8];
    warnings[0][0] = loadImage(dataPath("go_left_red.png"));    
    warnings[0][1] = loadImage(dataPath("go_lf_red.png"));
    warnings[0][2] = loadImage(dataPath("go_front_red.png"));
    warnings[0][3] = loadImage(dataPath("go_rf_red.png"));
    warnings[0][4] = loadImage(dataPath("go_right_red.png"));
    warnings[0][5] = loadImage(dataPath("go_rb_red.png"));
    warnings[0][6] = loadImage(dataPath("go_back_red.png"));
    warnings[0][7] = loadImage(dataPath("go_lb_red.png"));
    warnings[1][0] = loadImage(dataPath("go_left_blue.png"));    
    warnings[1][1] = loadImage(dataPath("go_lf_blue.png"));
    warnings[1][2] = loadImage(dataPath("go_front_blue.png"));
    warnings[1][3] = loadImage(dataPath("go_rf_blue.png"));
    warnings[1][4] = loadImage(dataPath("go_right_blue.png"));
    warnings[1][5] = loadImage(dataPath("go_rb_blue.png"));
    warnings[1][6] = loadImage(dataPath("go_back_blue.png"));
    warnings[1][7] = loadImage(dataPath("go_lb_blue.png"));

    shapeOfUser = loadImage(dataPath("shape.png"));  
    logo = loadImage(dataPath("kinetic_space.png"));
    emptyImage = loadImage(dataPath("empty.png"));

    for (int i = 0; i < nbrOfMoves; i++) {
      String str = Integer.toString(i);
      Move move = moves[i];

      File f = new File(dataPath("pose" + str + ".png"));
      if (!f.exists()) 
        move.empty = true;
      else
        move.image = loadImage(dataPath("pose" + str + ".png"));
    }
  }
}

