package digits.framework;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Used to render shapes and text to the screen in a streamlined manner
 * @author Robin Gather
 *
 */
public class Renderer {

	private Graphics2D g2d;
	private BufferedImage image;
	
	public Renderer() {
		
		image = new BufferedImage(ProgramContainer.WIDTH, ProgramContainer.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		g2d = image.createGraphics();
		
	}
	
	public void display(ProgramContainer pc) {
		
		Graphics graphics = pc.getGraphics();
		graphics.drawImage(image, 0, 0, null);
		graphics.dispose();
		
	}
	
	public void clear() {
		
		g2d.clearRect(0, 0, ProgramContainer.WIDTH, ProgramContainer.HEIGHT);
		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, ProgramContainer.WIDTH, ProgramContainer.HEIGHT);
		
	}
	
	public void fillRect(int x, int y, int w, int h, Color c) {
		g2d.setColor(c);
		g2d.fillRect(x, y, w, h);
	}
	
	public void drawRect(int x, int y, int w, int h, Color c) {
		g2d.setColor(c);
		g2d.drawRect(x, y, w, h);
	}
	
	public void drawText(String string, int size, int x, int y, Color c, TextAlign mode) {
		
		g2d.setFont(new Font("roboto", Font.BOLD, size));
		
		if(mode == TextAlign.CENTER)
			x += (-g2d.getFontMetrics().stringWidth(string)) / 2;
		else if(mode == TextAlign.RIGHT)
			x += (-g2d.getFontMetrics().stringWidth(string));
		
		g2d.setColor(c);
		g2d.drawString(string, x, y);
	}
	
}
