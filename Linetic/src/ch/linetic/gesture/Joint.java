package ch.linetic.gesture;

import processing.core.PVector;

public class Joint {

	public final float x;
	public final float y;
	public final float z;

	public Joint() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public Joint(PVector vector) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
	}

	public Joint(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Joint add(Joint that) {
		return new Joint(
				this.x + that.x,
				this.y + that.y,
				this.z + that.z);
	}

	public Joint sub(Joint that) {
		return new Joint(
				this.x - that.x,
				this.y - that.y,
				this.z - that.z);
	}

	public float dist(Joint that) {
		float accumulator = 0;
		accumulator += (this.x - that.x) * (this.x - that.x);
		accumulator += (this.y - that.y) * (this.y - that.y);
		accumulator += (this.z - that.z) * (this.z - that.z);
		return (float) Math.sqrt(accumulator);
	}

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
