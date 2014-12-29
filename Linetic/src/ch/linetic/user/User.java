package ch.linetic.user;

import ch.linetic.gesture.MovementInterface;
import ch.linetic.gesture.RingBuffer;
import ch.linetic.gui.Color;

public class User implements UserInterface {
	
	private final MovementInterface movement;
	private final int id;
	private final int color;
	
	/**
	 * Create a user given an identifier
	 * @param id the identifier
	 */
	public User(int id) {
		this.movement = new RingBuffer();
		this.id = id;
		this.color = Color.SHADE_A;
	}

	@Override
	public MovementInterface getMovement() {
		return movement;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
	public String getName() {
		return "user #" + id;
	}

	@Override
	public void hello() {
		System.out.println(">> Hello " + getName());
		
	}

	@Override
	public void bye() {
		System.out.println(">> Bye " + getName());
	}

}
