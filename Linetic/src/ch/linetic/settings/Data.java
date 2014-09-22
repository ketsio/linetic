package ch.linetic.settings;

import java.io.File;
import java.util.ArrayList;

import ch.linetic.gesture.Move;
import ch.linetic.gesture.Pose;
import processing.core.*;

public class Data {
	private ArrayList<String> datalist;
	private String filename, data[];
	private int datalineId;
	private PApplet parrent;
	
	public Data(PApplet papplet) {
		this.parrent = papplet;
	}

	public void load(Move m) {
		if (m.empty)
			return;
		
		load(m.slug);
		
		// settings
		m.frames = readInt();
		m.name = readString();
		m.slug = readString();
		m.triggerAt = readFloat();

		// weights
		m.weightX = readFloat();
		m.weightY = readFloat();
		m.weightZ = readFloat();
		m.weightLR = readFloat();
		m.normRotation = readBoolean();
		
		// poses
		for (int i = 0; i < m.frames; i++)
			m.set(i, readPose());

	    m.empty = false;
	    File f = new File(m.slug + ".png");
	    if (f.exists()) 
	      m.image = parrent.loadImage(m.slug + ".png");
	}
	
	public void save(Move m) {
		if (m.empty)
			return;

	    beginSave();

		// settings
		add(m.frames);
		add(m.name);
		add(m.slug);
		add(m.triggerAt);

		// weights
		add(m.weightX);
		add(m.weightY);
		add(m.weightZ);
		add(m.weightLR);
		add(m.normRotation);
		
		// poses
	    for (int i = 0; i < m.frames; i++)
	    	add(m.get(i));
	    
	    endSave(m.slug + ".data");
	}
	
	
	
	
	  ////////////
	 // SAVING //
	////////////

	private void beginSave() {
		datalist = new ArrayList<>();
	}

	// Add
	private void add(String s) {
		datalist.add(s);
	}

	private void add(float val) {
		datalist.add("" + val);
	}

	private void add(int val) {
		datalist.add("" + val);
	}

	private void add(boolean val) {
		datalist.add("" + val);
	}

	private void add(PVector val) {
		add(val.x);
		add(val.y);
		add(val.z);
	}

	private void add(Pose pose) {
		add(pose.jointLeftShoulderRelative);
		add(pose.jointLeftElbowRelative);
		add(pose.jointLeftHandRelative);

		add(pose.jointRightShoulderRelative);
		add(pose.jointRightElbowRelative);
		add(pose.jointRightHandRelative);
	}

	private void endSave(String _filename) {
		filename = _filename;

		data = new String[datalist.size()];
		data = (String[]) datalist.toArray(data);

		parrent.saveStrings(filename, data);
		System.out.println("Saved data to '" + filename + "', " + data.length + " lines.");
	}

	  /////////////
	 // LOADING //
	/////////////

	private void load(String _filename) {
		filename = _filename;

		datalineId = 0;
		data = parrent.loadStrings(filename);
		System.out.println("Loaded data from '" + filename + "', " + data.length
				+ " lines.");
	}

	private Pose readPose() {
		Pose pose = new Pose();
		pose.jointLeftShoulderRelative = readVector();
		pose.jointLeftElbowRelative = readVector();
		pose.jointLeftHandRelative = readVector();

		pose.jointRightShoulderRelative = readVector();
		pose.jointRightElbowRelative = readVector();
		pose.jointRightHandRelative = readVector();
		return pose;
	}

	private PVector readVector() {
		return new PVector(readFloat(), readFloat(), readFloat());
	}

	private float readFloat() {
	    return PApplet.parseFloat(data[datalineId++]);
	  }

	private int readInt() {
	    return PApplet.parseInt(data[datalineId++]);
	  }

	private boolean readBoolean() {
	    return PApplet.parseBoolean(data[datalineId++]);
	  }

	private String readString() {
		return data[datalineId++];
	}
}
