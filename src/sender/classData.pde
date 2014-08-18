class Data {
  ArrayList datalist;
  String filename, data[];
  int datalineId;


    ////////////
   // SAVING //
  ////////////

  void beginSave() {
    datalist=new ArrayList();
  }

  // Add
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
    datalist.add(val.x);
    datalist.add(val.y);
    datalist.add(val.z);
  }

  void add(Pose pose) {
    add(pose.jointLeftShoulderRelative);
    add(pose.jointLeftElbowRelative);
    add(pose.jointLeftHandRelative);

    add(pose.jointRightShoulderRelative);
    add(pose.jointRightElbowRelative);
    add(pose.jointRightHandRelative);
  }

  void endSave(String _filename) {
    filename = _filename;

    data = new String[datalist.size()];
    data = (String[]) datalist.toArray(data);

    saveStrings(filename, data);
    println("Saved data to '"+filename+"', "+data.length+" lines.");
  }


    /////////////
   // LOADING //
  /////////////

  void load(String _filename) {
    filename=_filename;

    datalineId=0;
    data=loadStrings(filename);
    println("Loaded data from '"+filename+"', "+data.length+" lines.");
  }

  Pose readPose() {
    Pose pose = new Pose();
    pose.jointLeftShoulderRelative = readVector();
    pose.jointLeftElbowRelative = readVector();
    pose.jointLeftHandRelative = readVector();

    pose.jointRightShoulderRelative = readVector();
    pose.jointRightElbowRelative = readVector();
    pose.jointRightHandRelative = readVector();
    return pose;
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
}

