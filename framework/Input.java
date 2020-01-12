package digits.framework;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Basic user input handler class
 * @author Robin Gather
 *
 */
public class Input implements KeyListener, MouseListener {

	private final int NUM_KEYS = 256;
	private boolean[] keys = new boolean[NUM_KEYS];
	private final int NUM_BUTTONS = 5;
	private boolean[] buttons = new boolean[NUM_BUTTONS];
	
	private int mouseX, mouseY;
	
	public Input(ProgramContainer pc) {
		mouseX = 0;
		mouseY = 0;
		pc.addKeyListener(this);
		pc.addMouseListener(this);
	}
	
	public void update(ProgramContainer pc) {

		if(pc.getMousePosition()==null)
			return;
		
		try {
			mouseX = pc.getMousePosition().x;
			mouseY = pc.getMousePosition().y;
		} catch(NullPointerException e) {
			
		}
		
	}
	
	public boolean isKey(int keyCode) {
		
		return keys[keyCode];
		
	}
	public boolean isKeyUp(int keyCode) {
		
		return !keys[keyCode];
		
	}
	public boolean isKeyDown(int keyCode) {
		
		return keys[keyCode];
		
	}
	public boolean isButton(int button) {

		return buttons[button];
		
	}
	public boolean isButtonUp(int button) {

		return !buttons[button];
		
	}
	public boolean isButtonDown(int button) {

		return buttons[button];
		
	}

	
	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}
	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	
	@Override
	public void mousePressed(MouseEvent e) {
		buttons[e.getButton()] = true;
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		buttons[e.getButton()] = false;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public double getMouseX() {
		return mouseX;
	}
	public double getMouseY() {
		return mouseY;
	}

}
