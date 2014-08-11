class Pose
{
  public PVector jointLeftShoulderRelative = new PVector();
  public PVector jointLeftElbowRelative = new PVector();
  public PVector jointLeftHandRelative = new PVector();

  public PVector jointRightShoulderRelative = new PVector();
  public PVector jointRightElbowRelative = new PVector();
  public PVector jointRightHandRelative = new PVector();

  public Pose() {
  }

  public void copyFrom(Pose that) {
    jointLeftShoulderRelative.x = that.jointLeftShoulderRelative.x;
    jointLeftShoulderRelative.y = that.jointLeftShoulderRelative.y;
    jointLeftShoulderRelative.z = that.jointLeftShoulderRelative.z;

    jointLeftElbowRelative.x = that.jointLeftElbowRelative.x;
    jointLeftElbowRelative.y = that.jointLeftElbowRelative.y;
    jointLeftElbowRelative.z = that.jointLeftElbowRelative.z;

    jointLeftHandRelative.x = that.jointLeftHandRelative.x;
    jointLeftHandRelative.y = that.jointLeftHandRelative.y;
    jointLeftHandRelative.z = that.jointLeftHandRelative.z;

    jointRightShoulderRelative.x = that.jointRightShoulderRelative.x;
    jointRightShoulderRelative.y = that.jointRightShoulderRelative.y;
    jointRightShoulderRelative.z = that.jointRightShoulderRelative.z;

    jointRightElbowRelative.x = that.jointRightElbowRelative.x;
    jointRightElbowRelative.y = that.jointRightElbowRelative.y;
    jointRightElbowRelative.z = that.jointRightElbowRelative.z;        

    jointRightHandRelative.x = that.jointRightHandRelative.x;
    jointRightHandRelative.y = that.jointRightHandRelative.y;
    jointRightHandRelative.z = that.jointRightHandRelative.z;
  }

  public Pose capture(SimpleOpenNI kinect, int userId) {

    Pose pose = new Pose();

    PVector jointNeck3D = new PVector();
    PVector jointLeftShoulder3D = new PVector();
    PVector jointLeftElbow3D = new PVector();
    PVector jointLeftHand3D = new PVector();
    PVector jointRightShoulder3D = new PVector();
    PVector jointRightElbow3D = new PVector();
    PVector jointRightHand3D = new PVector();

    PVector jointNeck2D = new PVector();  
    PVector jointLeftShoulder2D = new PVector();
    PVector jointLeftElbow2D = new PVector();
    PVector jointLeftHand2D = new PVector();
    PVector jointRightShoulder2D = new PVector();
    PVector jointRightElbow2D = new PVector();
    PVector jointRightHand2D = new PVector();

    // get the joint positions
    kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_NECK, jointNeck3D);  
    kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, jointLeftShoulder3D);
    kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_LEFT_ELBOW, jointLeftElbow3D);
    kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_LEFT_HAND, jointLeftHand3D);
    kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, jointRightShoulder3D);
    kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_RIGHT_ELBOW, jointRightElbow3D);
    kinect.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_RIGHT_HAND, jointRightHand3D);

    kinect.convertRealWorldToProjective(jointNeck3D, jointNeck2D);
    kinect.convertRealWorldToProjective(jointLeftShoulder3D, jointLeftShoulder2D);
    kinect.convertRealWorldToProjective(jointLeftElbow3D, jointLeftElbow2D);
    kinect.convertRealWorldToProjective(jointLeftHand3D, jointLeftHand2D);
    kinect.convertRealWorldToProjective(jointRightShoulder3D, jointRightShoulder2D);
    kinect.convertRealWorldToProjective(jointRightElbow3D, jointRightElbow2D);
    kinect.convertRealWorldToProjective(jointRightHand3D, jointRightHand2D);

    // calculate relative position  
    pose.jointLeftShoulderRelative.x = jointLeftShoulder3D.x - jointNeck3D.x;
    pose.jointLeftShoulderRelative.y = jointLeftShoulder3D.y - jointNeck3D.y;
    pose.jointLeftShoulderRelative.z = jointLeftShoulder3D.z - jointNeck3D.z;

    pose.jointLeftElbowRelative.x = jointLeftElbow3D.x - jointNeck3D.x;
    pose.jointLeftElbowRelative.y = jointLeftElbow3D.y - jointNeck3D.y;
    pose.jointLeftElbowRelative.z = jointLeftElbow3D.z - jointNeck3D.z;

    pose.jointLeftHandRelative.x = jointLeftHand3D.x - jointNeck3D.x;
    pose.jointLeftHandRelative.y = jointLeftHand3D.y - jointNeck3D.y;
    pose.jointLeftHandRelative.z = jointLeftHand3D.z - jointNeck3D.z;

    pose.jointRightShoulderRelative.x = jointRightShoulder3D.x - jointNeck3D.x;
    pose.jointRightShoulderRelative.y = jointRightShoulder3D.y - jointNeck3D.y;
    pose.jointRightShoulderRelative.z = jointRightShoulder3D.z - jointNeck3D.z;

    pose.jointRightElbowRelative.x = jointRightElbow3D.x - jointNeck3D.x;
    pose.jointRightElbowRelative.y = jointRightElbow3D.y - jointNeck3D.y;
    pose.jointRightElbowRelative.z = jointRightElbow3D.z - jointNeck3D.z;

    pose.jointRightHandRelative.x = jointRightHand3D.x - jointNeck3D.x;
    pose.jointRightHandRelative.y = jointRightHand3D.y - jointNeck3D.y;
    pose.jointRightHandRelative.z = jointRightHand3D.z - jointNeck3D.z;

    // Drawing
    if (userId == 0) pg.stroke(255, 0, 0);
    if (userId == 1) pg.stroke(0, 0, 255);
    pg.strokeWeight(5);
    pg.line(jointNeck2D.x, jointNeck2D.y, jointLeftShoulder2D.x, jointLeftShoulder2D.y);
    pg.line(jointLeftShoulder2D.x, jointLeftShoulder2D.y, jointLeftElbow2D.x, jointLeftElbow2D.y);
    pg.line(jointLeftElbow2D.x, jointLeftElbow2D.y, jointLeftHand2D.x, jointLeftHand2D.y);  
    pg.line(jointNeck2D.x, jointNeck2D.y, jointRightShoulder2D.x, jointRightShoulder2D.y);
    pg.line(jointRightShoulder2D.x, jointRightShoulder2D.y, jointRightElbow2D.x, jointRightElbow2D.y);
    pg.line(jointRightElbow2D.x, jointRightElbow2D.y, jointRightHand2D.x, jointRightHand2D.y);

    // Warnings
    warning[userId] = -1; 
    textAlign(CENTER);
    textFont(gui.fontA32, 32);
    fill(255, 0, 0);

    if (jointNeck2D.x < 100) 
      warning[userId] = 0;

    if (jointNeck2D.x > 540) 
      warning[userId] = 4;

    if (jointNeck3D.z > 4000) {
      warning[userId] = 2;
      if (jointNeck2D.x < 100)
        warning[userId] = 1;
      if (jointNeck2D.x > 540)
        warning[userId] = 3;
    }

    if (jointNeck2D.z < 1500) {
      warning[userId] = 6;
      if (jointNeck2D.x < 100) 
        warning[userId] = 7;
      if (jointNeck2D.x > 540)
        warning[userId] = 5;
    }

    return pose;
  }

  public void normalizeSize() {
    // define the size of the normalized person
    float scaleFactor = 1.0;
    float normalShoulderWidth = 370*scaleFactor;
    float normalLeftUpperArmLength = 320*scaleFactor;
    float normalRightUpperArmLength = 320*scaleFactor;
    float normalLeftLowerArmLength = 300*scaleFactor;
    float normalRightLowerArmLength = 300*scaleFactor;

    // normalize shoulder width
    PVector shoulderVector = PVector.sub(jointLeftShoulderRelative, jointRightShoulderRelative);
    float shoulderNormalizationFactor = normalShoulderWidth/shoulderVector.mag();

    jointLeftShoulderRelative.mult(shoulderNormalizationFactor);
    jointLeftElbowRelative.mult(shoulderNormalizationFactor);
    jointLeftHandRelative.mult(shoulderNormalizationFactor);
    jointRightShoulderRelative.mult(shoulderNormalizationFactor);
    jointRightElbowRelative.mult(shoulderNormalizationFactor);
    jointRightHandRelative.mult(shoulderNormalizationFactor);

    // normalize upper arms length
    PVector leftUpperArmVector = PVector.sub(jointLeftElbowRelative, jointLeftShoulderRelative);
    PVector rightUpperArmVector = PVector.sub(jointRightElbowRelative, jointRightShoulderRelative);

    float leftUpperArmNormalizationFactor = normalLeftUpperArmLength/leftUpperArmVector.mag();
    float rightUpperArmNormalizationFactor = normalRightUpperArmLength/rightUpperArmVector.mag();

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

    float leftLowerArmNormalizationFactor = normalLeftLowerArmLength/leftLowerArmVector.mag();
    float rightLowerArmNormalizationFactor = normalRightLowerArmLength/rightLowerArmVector.mag();

    leftLowerArmVector.mult(leftLowerArmNormalizationFactor);
    rightLowerArmVector.mult(rightLowerArmNormalizationFactor);

    PVector newLeftHandPosition = jointLeftElbowRelative.get();  
    PVector newRightHandPosition = jointRightElbowRelative.get();   

    newLeftHandPosition.sub(leftLowerArmVector);
    newRightHandPosition.sub(rightLowerArmVector);

    jointLeftHandRelative = newLeftHandPosition;
    jointRightHandRelative = newRightHandPosition;
  }

  // all the poses will be rotatet. need neck-relative position (as origin)
  public Pose normalizeRotation() {
    Pose poseNormalized = new Pose();

    // get vector between shoulders and computer the normal in the middle of the way
    // only 2d vector, as angle between only computes one angle (x,y component)
    PVector leftToRight = new PVector();
    leftToRight.x = jointRightShoulderRelative.x - jointLeftShoulderRelative.x;
    leftToRight.y = jointRightShoulderRelative.z - jointLeftShoulderRelative.z;

    // normalize
    leftToRight.normalize();

    // the orientation in the view from the kinect sensor
    PVector facingV = new PVector(1, 0); //use the normal to the z-direction (facing of the k.sensor) //0,1);

    // 0 -> front face to sensor face
    // 90 -> turned front to right
    // -90 -> turned front to left
    float fradians = PVector.angleBetween(leftToRight, facingV);
    float angle = degrees( fradians );

    if (leftToRight.y < 0)
    {
      angle = -angle;
      fradians = -fradians;
    } 

    // TODO compute back-facing vector (test sign )
    // negative x is with face to the kinect device,   

    // rotate all bones by this angle, so that the recognisable   
    float fcos = cos(-fradians);
    float fsin = sin(-fradians);

    poseNormalized.jointLeftShoulderRelative.x = fcos *   jointLeftShoulderRelative.x  - fsin *   jointLeftShoulderRelative.z;
    poseNormalized.jointLeftShoulderRelative.z = fsin *   jointLeftShoulderRelative.x  + fcos *   jointLeftShoulderRelative.z;

    PVector leftER = new PVector();
    poseNormalized.jointLeftElbowRelative.x = fcos * jointLeftElbowRelative.x  - fsin * jointLeftElbowRelative.z;
    poseNormalized.jointLeftElbowRelative.z = fsin * jointLeftElbowRelative.x  + fcos * jointLeftElbowRelative.z;

    PVector leftHR = new PVector();
    poseNormalized.jointLeftHandRelative.x = fcos * jointLeftHandRelative.x  - fsin * jointLeftHandRelative.z;
    poseNormalized.jointLeftHandRelative.z = fsin * jointLeftHandRelative.x  + fcos * jointLeftHandRelative.z;

    PVector rightSR = new PVector();
    poseNormalized.jointRightShoulderRelative.x = fcos * jointRightShoulderRelative.x  - fsin * jointRightShoulderRelative.z;
    poseNormalized.jointRightShoulderRelative.z = fsin * jointRightShoulderRelative.x  + fcos * jointRightShoulderRelative.z;

    PVector rightER = new PVector();
    poseNormalized.jointRightElbowRelative.x = fcos * jointRightElbowRelative.x  - fsin * jointRightElbowRelative.z;
    poseNormalized.jointRightElbowRelative.z = fsin * jointRightElbowRelative.x  + fcos * jointRightElbowRelative.z;

    PVector rightHR = new PVector();
    poseNormalized.jointRightHandRelative.x = fcos * jointRightHandRelative.x  - fsin * jointRightHandRelative.z;
    poseNormalized.jointRightHandRelative.z = fsin * jointRightHandRelative.x  + fcos * jointRightHandRelative.z;

    return poseNormalized;
  }
}

