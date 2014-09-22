package ch.linetic.gesture;

import ch.linetic.kinect.Kinect;
import ch.linetic.settings.Data;
import ch.linetic.settings.Default;
import ch.linetic.user.User;
import processing.core.*;

public class Move {

	private Pose[] poses = new Pose[Default.MOVE_FRAMES_MAX];
	private Data data;
	
	private int id;
	public boolean empty = true;
	public PImage image = null;
	public String name = null;
	public String slug = null;

	// settings
	public float weightX = Default.WEIGHT_X;
	public float weightY = Default.WEIGHT_Y;
	public float weightZ = Default.WEIGHT_Z;
	public float weightLR = Default.WEIGHT_LR;
	public boolean normRotation = Default.NORM_ROTATION;
	public float triggerAt = Default.TRIGGER_AT;
	public int frames = Default.MOVE_FRAMES;

	public Move(int moveId, Data data) {
	    this.id = moveId;
	    this.data = data;
	    this.slug = slug();
	    for (int i = 0; i < Default.MOVE_FRAMES_MAX; i++) poses[i] = new Pose();
	}

	private String slug() {
	    return "move" + id;
	}

	public Pose get(int id) {
	    if (id < 0 || id >= frames)
	        return null;
	      return poses[id];
	}
	
	public void set(int id, Pose p) {
	    if (id < 0 || id >= frames)
	        return;
	    poses[id] = p;
	}


	public void beginRecord(User user, Kinect kinect) {
	    image = kinect.screenshot(slug + ".png"); 
	    user.record(this);
	}
	
	public void endRecord() {
	    data.save(this);
	    empty = false;
	}
	
	public float[] costs(Pose pose) {
		float[] costs = new float[frames];
		
		if (normRotation)
			pose = pose.normalizeRotation();
		for (int i = 0; i < frames; i++)
			costs[i] = cost(pose, poses[frames - i]);
		
		return costs;
	}

	public float cost(Pose a, Pose b) {
		if (weightLR > 1.0)
			weightLR = (float) 1.0;
		if (weightLR < -1.0)
			weightLR = (float) -1.0;

		float weight_left = (float) (1.0 + weightLR);
		float weight_right = (float) (1.0 - weightLR);

		float mse = 0;

		mse += weight_left * dist(a.jointLeftShoulderRelative, b.jointLeftShoulderRelative);
		mse += weight_left * dist(a.jointLeftElbowRelative, b.jointLeftElbowRelative);
		mse += weight_left * dist(a.jointLeftHandRelative, b.jointLeftHandRelative);

		mse += weight_right * dist(a.jointRightShoulderRelative, b.jointRightShoulderRelative);
		mse += weight_right * dist(a.jointRightElbowRelative, b.jointRightElbowRelative);
		mse += weight_right * dist(a.jointRightHandRelative, b.jointRightHandRelative);

		mse /= (3 * weight_left + 3 * weight_right);

		return mse;
	}

	private float dist(PVector a, PVector b) {
		float result = PApplet.sq(a.x - b.x) * weightX;
		result += PApplet.sq(a.y - b.y) * weightY;
		result += PApplet.sq(a.z - b.z) * weightZ;
		result /= (weightX + weightY + weightZ);
		return PApplet.sqrt(result);
	}
}
