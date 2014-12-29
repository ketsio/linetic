package ch.linetic.user;

import ch.linetic.gesture.MovementInterface;

public interface UserInterface {

	/**
	 * @see ch.linetic.gesture.MovementInterface
	 * @return the movement of the user
	 */
	public MovementInterface getMovement();

	/**
	 * @return the identifier of the user
	 */
	public int getId();
	
	/**
	 * @return the color of the user, used for displaying the skeleton for example
	 */
	public int getColor();
	
	/**
	 * @return the name of the user
	 */
	public String getName();
	
	/**
	 * Acknowledge the recognition of the user
	 */
	public void hello();

	/**
	 * Acknowledge the loss of the user
	 */
	public void bye();
}
