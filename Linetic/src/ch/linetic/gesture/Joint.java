package ch.linetic.gesture;

import processing.core.PVector;

/**
 * This class simply redefine what a point is
 * It is characterize by 3 coordinates : x, y and z
 * It contains some basic operation that are useful for the Analyzers
 * @author ketsio
 *
 */
public class Joint {

	public final float x;
	public final float y;
	public final float z;

	/**
	 * Create a Joint with the value zero for its 3 coordinates
	 */
	public Joint() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	/**
	 * Create a Joint given a PVector (from Processing)
	 * @param vector
	 */
	public Joint(PVector vector) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
	}

	/**
	 * Create a Joint given its 3 coordinates
	 * @param x
	 * @param y
	 * @param z
	 */
	public Joint(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Perform an addition of two Joints
	 * @param that
	 * @return a new Joint that is the addition of `this` and `that`
	 */
	public Joint add(Joint that) {
		return new Joint(
				this.x + that.x,
				this.y + that.y,
				this.z + that.z);
	}

	/**
	 * Perform an subtraction of two Joints
	 * @param that
	 * @return a new Joint that is the subtraction : `this` - `that`
	 */
	public Joint sub(Joint that) {
		return new Joint(
				this.x - that.x,
				this.y - that.y,
				this.z - that.z);
	}
	
	/**
	 * Calculate the distance between two Joints
	 * @param that
	 * @return the distance between `this` and `that`
	 */
	public float dist(Joint that) {
		float accumulator = 0;
		accumulator += (this.x - that.x) * (this.x - that.x);
		accumulator += (this.y - that.y) * (this.y - that.y);
		accumulator += (this.z - that.z) * (this.z - that.z);
		return (float) Math.sqrt(accumulator);
	}


	/**
	 * Calculate the difference of angle between two Joints
	 * @param that
	 * @return the angle between `this` and `that`
	 */
	public float angle(Joint that) {
		PVector v1 = new PVector(this.x,this.y,this.z);
		PVector v2 = new PVector(that.x,that.y,that.z);
		float mags = v1.mag() * v2.mag();
		if (mags == 0) {
			return 0;
		}
		
		float cosTheta = v1.dot(v2) / mags;
		cosTheta = cosTheta > 1.0 ? 1 : cosTheta;
		cosTheta = cosTheta < -1.0 ? -1 : cosTheta;
		
		return (float) (Math.acos(cosTheta) * 180.0 / Math.PI);
	}
	
	@Override
	public String toString() {
		return "Joint("+x+", "+y+", "+z+")";
	}

}
