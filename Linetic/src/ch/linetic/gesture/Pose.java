package ch.linetic.gesture;

import processing.core.PApplet;
import processing.core.PVector;

public class Pose {

	public PVector jointLeftShoulderRelative = new PVector();
	public PVector jointLeftElbowRelative = new PVector();
	public PVector jointLeftHandRelative = new PVector();

	public PVector jointRightShoulderRelative = new PVector();
	public PVector jointRightElbowRelative = new PVector();
	public PVector jointRightHandRelative = new PVector();

	public void copyFrom(Pose that) {
		jointLeftShoulderRelative = that.jointLeftShoulderRelative.get();
		jointLeftElbowRelative = that.jointLeftElbowRelative.get();
		jointLeftHandRelative = that.jointLeftHandRelative.get();

		jointRightShoulderRelative = that.jointRightShoulderRelative.get();
		jointRightElbowRelative = that.jointRightElbowRelative.get();
		jointRightHandRelative = that.jointRightHandRelative.get();
	}

	public Pose normalizeRotation() {
		Pose poseNormalized = new Pose();

		PVector leftToRight = new PVector();
		leftToRight.x = jointRightShoulderRelative.x - jointLeftShoulderRelative.x;
		leftToRight.y = jointRightShoulderRelative.z - jointLeftShoulderRelative.z;

		leftToRight.normalize();
		PVector facingV = new PVector(1, 0);

		float fradians = PVector.angleBetween(leftToRight, facingV);
		float angle = PApplet.degrees(fradians);

		if (leftToRight.y < 0) {
			angle = -angle;
			fradians = -fradians;
		}

		float fcos = PApplet.cos(-fradians);
		float fsin = PApplet.sin(-fradians);

		poseNormalized.jointLeftShoulderRelative.x = fcos * jointLeftShoulderRelative.x - fsin * jointLeftShoulderRelative.z;
		poseNormalized.jointLeftShoulderRelative.z = fsin * jointLeftShoulderRelative.x + fcos * jointLeftShoulderRelative.z;

		poseNormalized.jointLeftElbowRelative.x = fcos * jointLeftElbowRelative.x - fsin * jointLeftElbowRelative.z;
		poseNormalized.jointLeftElbowRelative.z = fsin * jointLeftElbowRelative.x + fcos * jointLeftElbowRelative.z;

		poseNormalized.jointLeftHandRelative.x = fcos * jointLeftHandRelative.x - fsin * jointLeftHandRelative.z;
		poseNormalized.jointLeftHandRelative.z = fsin * jointLeftHandRelative.x + fcos * jointLeftHandRelative.z;

		poseNormalized.jointRightShoulderRelative.x = fcos * jointRightShoulderRelative.x - fsin * jointRightShoulderRelative.z;
		poseNormalized.jointRightShoulderRelative.z = fsin * jointRightShoulderRelative.x + fcos * jointRightShoulderRelative.z;

		poseNormalized.jointRightElbowRelative.x = fcos * jointRightElbowRelative.x - fsin * jointRightElbowRelative.z;
		poseNormalized.jointRightElbowRelative.z = fsin * jointRightElbowRelative.x + fcos * jointRightElbowRelative.z;

		poseNormalized.jointRightHandRelative.x = fcos * jointRightHandRelative.x - fsin * jointRightHandRelative.z;
		poseNormalized.jointRightHandRelative.z = fsin * jointRightHandRelative.x + fcos * jointRightHandRelative.z;

		return poseNormalized;
	}
	
	public void normalizeSize() {
		// define the size of the normalized person
		float scaleFactor = 1;
		float normalShoulderWidth = 370 * scaleFactor;
		float normalLeftUpperArmLength = 320 * scaleFactor;
		float normalRightUpperArmLength = 320 * scaleFactor;
		float normalLeftLowerArmLength = 300 * scaleFactor;
		float normalRightLowerArmLength = 300 * scaleFactor;

		// normalize shoulder width
		PVector shoulderVector = PVector.sub(jointLeftShoulderRelative, jointRightShoulderRelative);
		float shoulderNormalizationFactor = normalShoulderWidth / shoulderVector.mag();

		jointLeftShoulderRelative.mult(shoulderNormalizationFactor);
		jointLeftElbowRelative.mult(shoulderNormalizationFactor);
		jointLeftHandRelative.mult(shoulderNormalizationFactor);
		jointRightShoulderRelative.mult(shoulderNormalizationFactor);
		jointRightElbowRelative.mult(shoulderNormalizationFactor);
		jointRightHandRelative.mult(shoulderNormalizationFactor);

		// normalize upper arms length
		PVector leftUpperArmVector = PVector.sub(jointLeftElbowRelative, jointLeftShoulderRelative);
		PVector rightUpperArmVector = PVector.sub(jointRightElbowRelative, jointRightShoulderRelative);

		float leftUpperArmNormalizationFactor = normalLeftUpperArmLength / leftUpperArmVector.mag();
		float rightUpperArmNormalizationFactor = normalRightUpperArmLength / rightUpperArmVector.mag();

		PVector oldLeftElbow = jointLeftElbowRelative.get();
		PVector oldRightElbow = jointRightElbowRelative.get();

		jointLeftElbowRelative.mult(leftUpperArmNormalizationFactor);
		jointRightElbowRelative.mult(rightUpperArmNormalizationFactor);

		PVector leftHandMoveVector = jointLeftElbowRelative.get();
		leftHandMoveVector.sub(oldLeftElbow);
		PVector rightHandMoveVector = jointRightElbowRelative.get();
		rightHandMoveVector.sub(oldRightElbow);

		jointLeftHandRelative.add(leftHandMoveVector);
		jointRightHandRelative.add(rightHandMoveVector);

		// normalize lower arms length
		PVector leftLowerArmVector = PVector.sub(jointLeftElbowRelative, jointLeftHandRelative);
		PVector rightLowerArmVector = PVector.sub(jointRightElbowRelative, jointRightHandRelative);

		float leftLowerArmNormalizationFactor = normalLeftLowerArmLength / leftLowerArmVector.mag();
		float rightLowerArmNormalizationFactor = normalRightLowerArmLength / rightLowerArmVector.mag();

		leftLowerArmVector.mult(leftLowerArmNormalizationFactor);
		rightLowerArmVector.mult(rightLowerArmNormalizationFactor);

		PVector newLeftHandPosition = jointLeftElbowRelative.get();
		PVector newRightHandPosition = jointRightElbowRelative.get();

		newLeftHandPosition.sub(leftLowerArmVector);
		newRightHandPosition.sub(rightLowerArmVector);

		jointLeftHandRelative = newLeftHandPosition;
		jointRightHandRelative = newRightHandPosition;
	}

}
