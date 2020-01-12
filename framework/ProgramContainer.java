package digits.framework;

import java.awt.Dimension;

import javax.swing.JPanel;

import digits.ProgramManager;

/**
 * Foundational runnable loop of the program
 * @author Robin Gather
 *
 */
@SuppressWarnings("serial")
public class ProgramContainer extends JPanel implements Runnable {

	public static final int WIDTH = 770;
	public static final int HEIGHT = 560;
	
	private ProgramManager program;
	private Renderer r;
	
	private Thread thread;
	private boolean running;
	private long tTime;
	private int fps_mod = 600;
	
	public ProgramContainer() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
	}
	
	public void addNotify() {
		super.addNotify();
		thread = new Thread(this);
		thread.start();
	}
	
	private void setFPS(int FPS){
		tTime = 1000 / FPS;
	}

	@Override
	public void run() {

		if(running) 
			return;

		running = true;
		
		r = new Renderer();
		program = new ProgramManager();
		program.init(this);
		
		long sTime;
		long pTime;
		long delay;
		
		while(running){
			sTime = System.nanoTime();
			
			setFPS(fps_mod);
			program.update(this);
			
			if(program.isExit())
				running = false;
			
			r.clear();
			program.render(r);
			r.display(this);
			
			pTime = System.nanoTime() - sTime;
			delay = tTime - pTime / 1000000;
			if(delay > 0){
				try{
					Thread.sleep(delay);
				}
				catch(Exception u){
					u.printStackTrace();
				}
			}
		}

		System.exit(0);
		
	}

}
