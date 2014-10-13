package ch.linetic.user;

import ch.linetic.gesture.MovementInterface;

public interface UserInterface {

	public MovementInterface getMovement();

	public int getId();
	public int getColor();
	public String getName();
	
	public void hello();
	public void bye();
}
