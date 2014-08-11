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
      add(pose.jointLeftShoulderRelative.x);
      add(pose.jointLeftShoulderRelative.y);
      add(pose.jointLeftShoulderRelative.z);

      add(pose.jointLeftElbowRelative.x);
      add(pose.jointLeftElbowRelative.y);
      add(pose.jointLeftElbowRelative.z);

      add(pose.jointLeftHandRelative.x);
      add(pose.jointLeftHandRelative.y);
      add(pose.jointLeftHandRelative.z);

      add(pose.jointRightShoulderRelative.x);
      add(pose.jointRightShoulderRelative.y);
      add(pose.jointRightShoulderRelative.z);

      add(pose.jointRightElbowRelative.x);
      add(pose.jointRightElbowRelative.y);
      add(pose.jointRightElbowRelative.z);

      add(pose.jointRightHandRelative.x);
      add(pose.jointRightHandRelative.y);
      add(pose.jointRightHandRelative.z);
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
      pose.jointLeftShoulderRelative.x = readFloat();
      pose.jointLeftShoulderRelative.y = readFloat();
      pose.jointLeftShoulderRelative.z = readFloat();

      pose.jointLeftElbowRelative.x = readFloat();
      pose.jointLeftElbowRelative.y = readFloat();
      pose.jointLeftElbowRelative.z = readFloat();

      pose.jointLeftHandRelative.x = readFloat();
      pose.jointLeftHandRelative.y = readFloat();
      pose.jointLeftHandRelative.z = readFloat();

      pose.jointRightShoulderRelative.x = readFloat();
      pose.jointRightShoulderRelative.y = readFloat();
      pose.jointRightShoulderRelative.z = readFloat();

      pose.jointRightElbowRelative.x = readFloat();
      pose.jointRightElbowRelative.y = readFloat();
      pose.jointRightElbowRelative.z = readFloat();

      pose.jointRightHandRelative.x = readFloat();
      pose.jointRightHandRelative.y = readFloat();
      pose.jointRightHandRelative.z = readFloat();

      if (NORMALIZE_SIZE) pose.normalizeSize();
      if (move.normRotation) pose = pose.normalizeRotation();
    }
  }
}

