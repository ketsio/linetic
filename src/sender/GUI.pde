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
  PVector gridOrigin;
  int gridWidth;
  int gridHeight;
  int cellWidth;
  int cellHeight;

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
    
    // MAIN SETTINGS ========================================
    size(1200, 600, JAVA2D);

    // MOVE GRID ============================================
    col = 6;
    row = ceil(float(nbrOfMoves) / float(col));
    col = row*col - nbrOfMoves >= row ? ceil(float(nbrOfMoves) / float(row)) : col;
    println("col = " + col + ", row = " + row);

    gridOrigin = new PVector(0, context.depthHeight() + padding);
    gridWidth = width;
    gridHeight = int(height - gridOrigin.y);

    cellWidth = (gridWidth - padding * (col + 1)) / col;
    cellHeight = (gridHeight - padding * (row + 1)) / row;

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

    // process the skeleton if it's available
    for (User u : users.values ())
      if (context.isTrackingSkeleton(u.id))
        evaluateSkeleton(u.id);

    pg.endDraw();


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

    if (users.isEmpty()) 
    {
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

    // Grid of Moves
    displayGrid();
    
    // Grid Buffer of the highlighted user
    if (highlightedUser != null)
      highlightedUser.rb.display();
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

  private void displayGrid() {

    if (updateDisplay) {
      updateDisplay = false;

      textFont(fontA12, 16);
      textAlign(CENTER);
      rectMode(CORNER);
      fill(255);

      println("cellWidth = " + cellWidth + ", cellHeight = " + cellHeight);

      // Displaying the grid of moves (TODO : optimize offset calcul (more vars))
      int moveId;
      PVector cellOrigin = new PVector();
      for (int r = 0; r < row; r++)
        for (int c = 0; c < col; c++) {
          moveId = r * col + c;
          if (moveId < nbrOfMoves) {
            cellOrigin.x = gridOrigin.x + padding + c * (cellWidth + padding);
            cellOrigin.y = gridOrigin.y + padding + r * (cellHeight + padding);
            drawCell(cellOrigin, moveId);
          }
        }
    }

    // evaluate and draw DTW
    if (highlightedUser != null) {

      noStroke();             
      fill(0, 0, 0);

      for (int i = 0; i < nbrOfMoves; i++)
        drawMatchingBar(highlightedUser, i);
    }
    //    if (!users.isEmpty())
    //    {
    //
    //      noStroke();             
    //      fill(0, 0, 0);
    //
    //      for (User user : users.values ()) 
    //        for (int i = 0; i < nbrOfMoves; i++)
    //          drawMatchingBar(user, i);
    //    }
  }

  private void drawCell(PVector cellOrigin, int moveId) {
    Move move = moves[moveId];

    if (move.empty) {
      image(emptyImage, cellOrigin.x, cellOrigin.y, cellWidth, cellHeight);
    } else if (move.image ==  null) {
      image(emptyImage, cellOrigin.x, cellOrigin.y, cellWidth, cellHeight); // TODO : create a 'noImage' png
    } else if (mirrored) {
      pushMatrix();
      scale(-1.0, 1.0);
      image(move.image, - (cellOrigin.x + cellWidth), cellOrigin.y, cellWidth, cellHeight);
      popMatrix();
    } else {
      image(move.image, cellOrigin.x, cellOrigin.y, cellWidth, cellHeight);
    }
    text(moveId, cellOrigin.x + 12, cellOrigin.y + 16);
  }

  private void drawMatchingBar(User user, int moveId) {
    Move move = moves[moveId];
    if (!move.empty)
    {
      user.rb.costLast[moveId] = user.rb.cost[moveId];
      user.rb.cost[moveId] = user.rb.pathcost(moveId);
      user.rb.cost[moveId] = (log(user.rb.cost[moveId]-1.0) - 5.5)/2.0;

      float matching = 1.0 - user.rb.cost[moveId];

      fill(user.c);
      if (matching >= 0.75 )
        fill(0, 255, 0);

      if (matching < 0.75 && matching > 0.65)
        fill(user.c, 200);

      // Positioning
      int r = moveId / col;
      int c = moveId % col;
      PVector cellOrigin = new PVector();
      cellOrigin.x = gridOrigin.x + padding + c * (cellWidth + padding);
      cellOrigin.y = gridOrigin.y + padding + r * (cellHeight + padding);

      rect(cellOrigin.x, cellOrigin.y + cellHeight + 2, min(1.0, max(0.01, matching)) * float(cellWidth), 8);

      if ( matching >= 0.7) // TODO : move.triggerAt
      {
        println("found gesture #" + moveId + " user : " + user.name);
        server.send(move, user);
      }
    }
  }

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

