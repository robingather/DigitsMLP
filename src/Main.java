package digits;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;

import digits.framework.ProgramContainer;

public class Main {
	
	public static void main(String[] args) throws IOException {

		JFrame window = new JFrame("Digit Recognizer");
		
		window.setContentPane(new ProgramContainer());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setPreferredSize(new Dimension(ProgramContainer.WIDTH, ProgramContainer.HEIGHT));
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
	}
	
}
