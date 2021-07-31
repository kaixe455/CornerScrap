package model.entity;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class SwingDemo extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JProgressBar progressBar;
	   int i = 0;
	   public SwingDemo() {
	      progressBar = new JProgressBar(0, 1000);
	      progressBar.setBounds(70, 50, 120, 30);
	      progressBar.setValue(0);
	      progressBar.setStringPainted(true);
	      add(progressBar);
	      setSize(300, 150);
	      setLayout(null);
	   }
	   public void inc() {
	      while (i <= 1000) {
	         progressBar.setValue(i);
	         i = i + 50;
	      }
	   }
	   public static void main(String[] args) {
	      SwingDemo s = new SwingDemo();
	      s.setVisible(true);
	      s.inc();
	   }

}
