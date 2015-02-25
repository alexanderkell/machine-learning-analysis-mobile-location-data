import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;


public class JSwingTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		JFrame frame = new JFrame();
		
		final JLabel jlabel1 = new JLabel();	// Label for "Playing at XX Speed"
		jlabel1.setText("Playing at X speed");
		
		final JLabel jlabel2 = new JLabel("label2");	// Label for showing start time
		
		final JLabel jlabel3 = new JLabel("label3");	// Label for showing current time
		
		final JLabel jlabel4 = new JLabel("label4");	// Label for showing current time

		final JProgressBar jpb = new JProgressBar();	//ProgressBar for showing the current time
		jpb.add(jlabel4);
		jpb.setStringPainted(true);
		jpb.setString("label5");
		
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		frame.add(panel, BorderLayout.SOUTH);
		
		
		JPanel subpanel = new JPanel();
		subpanel.setLayout(new BoxLayout(subpanel, BoxLayout.LINE_AXIS));
		
		panel.add(jlabel1);
		jlabel1.setAlignmentX(Container.LEFT_ALIGNMENT);
		subpanel.add(jlabel2);
		subpanel.add(jpb);
		subpanel.add(jlabel3);
		subpanel.setAlignmentX(Container.LEFT_ALIGNMENT);
		panel.add(subpanel);
		frame.pack();
		frame.setVisible(true);	
	}

}
