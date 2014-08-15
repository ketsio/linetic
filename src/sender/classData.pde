class Data {
  ArrayList datalist;
  String filename, data[];
  int datalineId;

  // begin data saving
  void beginSave() {
    datalist=new ArrayList();
  }

  void add(String s) {
    datalist.add(s);
  }

  void add(float val) {
    datalist.add(""+val);
  }

  void add(int val) {
    datalist.add(""+val);
  }

  void add(boolean val) {
    datalist.add(""+val);
  }
  
  void add(PVector val) {
    datalist.add(""+val.x);
    datalist.add(""+val.y);
    datalist.add(""+val.z);
  }

  void endSave(String _filename) {
    filename = _filename;

    data = new String[datalist.size()];
    data = (String[]) datalist.toArray(data);

    saveStrings(filename, data);
    println("Saved data to '"+filename+"', "+data.length+" lines.");
  }

  void load(String _filename) {
    filename=_filename;

    datalineId=0;
    data=loadStrings(filename);
    println("Loaded data from '"+filename+"', "+data.length+" lines.");
  }

  PVector readVector() {
    return new PVector(readFloat(), readFloat(), readFloat());
  }
  
  float readFloat() {
    return float(data[datalineId++]);
  }

  int readInt() {
    return int(data[datalineId++]);
  }

  boolean readBoolean() {
    return boolean(data[datalineId++]);
  }

  String readString() {
    return data[datalineId++];
  }
  
  void saveMove(int moveId) { 
    beginSave();
    Move move = moves[moveId];
    Pose pose;
    for (int i = 1; i < framesGestureMax; i++)
    {
      pose = move.get(i);
      add(pose.jointLeftShoulderRelative);
      add(pose.jointLeftElbowRelative);
      add(pose.jointLeftHandRelative);

      add(pose.jointRightShoulderRelative);
      add(pose.jointRightElbowRelative);
      add(pose.jointRightHandRelative);
    }

    // Saving
    String str = Integer.toString(moveId);    
    endSave(dataPath("pose" + str + ".data"));
  }

  void loadMove(int moveId) {
    // Loading
    String str = Integer.toString(moveId);
    load(dataPath("pose" + str + ".data"));

    Move move = moves[moveId];
    Pose pose;
    for (int i = 1; i < framesGestureMax; i++)
    {
      pose = move.get(i);
      pose.jointLeftShoulderRelative = readVector();
      pose.jointLeftElbowRelative = readVector();
      pose.jointLeftHandRelative = readVector();
      
      pose.jointRightShoulderRelative = readVector();
      pose.jointRightElbowRelative = readVector();
      pose.jointRightHandRelative = readVector();

      if (NORMALIZE_SIZE) pose.normalizeSize();
      if (move.normRotation) pose = pose.normalizeRotation();
    }
    move.empty = false;
  }
}

