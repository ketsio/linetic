// TOOLS

void parseXML()
{

  File f = new File(dataPath("setup.xml"));
  if (!f.exists()) 
  {  
    println("File " + dataPath("setup.xml") + " does not exist");
    return;
  } 
  println("File " + dataPath("setup.xml") + " does exist");
  XML root = loadXML(dataPath("setup.xml"));

  // AUTODETECTION
  XML autodetection = root.getChild("autodetection");
  if (autodetection != null)
    autoPoseDetection = parseYN(autodetection.getContent());

  // MULTITHREADING
  XML usemultithreading = root.getChild("usemultithreading");
  if (usemultithreading != null)
    useMultiThreading = parseYN(usemultithreading.getContent());

  // NORMALIZE
  XML normalize = root.getChild("normalize");
  if (normalize != null) {

    // size
    XML normalize_size = normalize.getChild("size");
    if (normalize_size != null)
      NORMALIZE_SIZE = parseYN(normalize_size.getContent());

    // rotation
    XML normalize_rotation = normalize.getChild("rotation");
    if (normalize_rotation != null)
      defaultNormRotation = parseYN(normalize_rotation.getContent());
  }

  // WEIGHT
  XML weight = root.getChild("weight");
  if (weight != null) {

    // x
    XML weight_x = weight.getChild("x");
    if (weight_x != null)
      defaultWeightX = weight_x.getFloatContent();

    // y
    XML weight_y = weight.getChild("y");
    if (weight_y != null)
      defaultWeightY = weight_y.getFloatContent();

    // z
    XML weight_z = weight.getChild("z");
    if (weight_z != null)
      defaultWeightZ = weight_z.getFloatContent();
  }

  // BODYPART
  XML bodypart = root.getChild("bodypart");
  if (bodypart != null)
    defaultWeightLeftOrRight = parseBodypart(bodypart.getContent());

  // FRAMES
  XML globalFrames = root.getChild("frames");
  if (globalFrames != null)
    defaultFramesGesture = globalFrames.getIntContent() >= framesGestureMax ? framesGestureMax : globalFrames.getIntContent();

  // GESTURES
  XML[] gestures = root.getChildren("gesture");
  for (int i = 0; i < gestures.length; i++) {

    // default values
    int id = -1;
    int frames = -1;
    int nr = -1; 
    float lr = sqrt(-1);
    float x = -1.0;        
    float y = -1.0;        
    float z = -1.0;

    // id
    if (gestures[i].getChild("id") != null)
      id = gestures[i].getChild("id").getIntContent();

    // frames
    if (gestures[i].getChild("frames") != null)
      frames = gestures[i].getChild("frames").getIntContent();

    // normalize_rotation
    if (gestures[i].getChild("normalize_rotation") != null)
      nr = parseYN(gestures[i].getChild("normalize_rotation").getContent()) ? 1 : 0;

    // bodypart
    if (gestures[i].getChild("bodypart") != null)
      lr = parseBodypart(gestures[i].getChild("bodypart").getContent());

    // weight_x
    if (gestures[i].getChild("weight/x") != null)
      x = gestures[i].getChild("weight/x").getFloatContent();

    // weight_y
    if (gestures[i].getChild("weight/y") != null)
      y = gestures[i].getChild("weight/y").getFloatContent();

    // weight_z
    if (gestures[i].getChild("weight/z") != null)
      z = gestures[i].getChild("weight/z").getFloatContent();

    // affectations
    if ((id >= 0) && (id < nbrOfMoves))
    {
      if (frames>=0) moves[id].framesGesture = frames;
      if (x>=0) moves[id].weightX = x;
      if (y>=0) moves[id].weightY = y;
      if (z>=0) moves[id].weightZ = z;
      if (nr==0) moves[id].normRotation = false;
      if (nr==1) moves[id].normRotation = true;
      if ((lr>=1) && (lr<=1)) moves[id].weightLeftOrRight = lr;
    }
  }
}

boolean foundString(String str1, String str2)
{
  boolean found = false;

  if ((str1 == null) || (str2 == null)) return false;

  String[] m1 = match(str1, str2);
  if (m1 != null) {
    found = true;
  }  

  m1 = match(str2, str1);
  if (m1 != null) {
    found = true;
  }

  return found;
}

boolean parseYN(String str) {
  return str.equals("yes") || str.equals("true") || str.equals("on");
}

Float parseBodypart(String str) {
  return str.equals("leftarm") ? -1.0 : str.equals("rightarm") ? 1.0 : 0.0;
}

User getRandomUser() {
  if (users.isEmpty())
    return null;
  List<User> currentUsers = new ArrayList<User>(users.values());
  return currentUsers.get(int(random(currentUsers.size())));
}

