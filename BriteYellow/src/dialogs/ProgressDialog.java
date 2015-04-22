package dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ProgressDialog extends JFrame implements ActionListener{
	

	private JLabel progresslabel;
	private JTextArea logarea;
	private JPanel mainpanel;
	private JScrollPane scroll;
	private JLabel infolabel;
	private JLabel loglabel;
	private Timer timer;
	private JProgressBar progressbar;
	private JLabel infolabel2;
	
	
	public final static Font progressfontLarge = new Font("large", Font.TRUETYPE_FONT, 22);
	public final static Font progressfontNormal = new Font("normal", Font.TRUETYPE_FONT, 16);
	public final static Font progressfontSmall = new Font("small", Font.BOLD, 13);

	public final static int DEFAULT_WIDTH = 480;
	public final static int DEFAULT_HEIGHT = 320;
	private int width;
	private int height;
	
	public ProgressDialog(String title){
		this(title, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	public ProgressDialog(String title, int width, int height){
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle(title);
		
		this.width = width;
		this.height = height;
		
		mainpanel = new JPanel();
		mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.PAGE_AXIS));
		
		// Icon gif downloaded at: http://preloaders.net/en/circular/windows8-loader/
		ImageIcon loading = new ImageIcon("src\\dialogs\\loading.gif", "Loading");
		progresslabel = new JLabel("Initialising...",loading, JLabel.CENTER);
		infolabel = new JLabel(" ");
		infolabel2 = new JLabel(" ");
		
		progressbar = new JProgressBar();
		logarea = new JTextArea(" ");
		loglabel = new JLabel("<html>Progress logs</html>",JLabel.LEFT);
	    scroll = new JScrollPane (logarea);
	    

		// Setup swing components
	    progresslabel.setFont(progressfontLarge);
	    progresslabel.setAlignmentX(CENTER_ALIGNMENT);
	    infolabel.setFont(progressfontNormal);
	    infolabel.setAlignmentX(CENTER_ALIGNMENT);
	    infolabel2.setFont(progressfontSmall);
	    infolabel2.setAlignmentX(CENTER_ALIGNMENT);
	    progressbar.setAlignmentX(CENTER_ALIGNMENT);
	    loglabel.setAlignmentX(CENTER_ALIGNMENT);
	  	logarea.setLineWrap(true);
		logarea.setEditable(false);
		logarea.setAutoscrolls(true);
		logarea.setAlignmentX(CENTER_ALIGNMENT);
		
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    

        // Add components to the main panel
		mainpanel.add(Box.createVerticalStrut(40));
		mainpanel.add(progresslabel);
		mainpanel.add(Box.createVerticalStrut(10));
		mainpanel.add(infolabel);
		mainpanel.add(Box.createVerticalStrut(10));
		mainpanel.add(infolabel2);
		mainpanel.add(progressbar);
		mainpanel.add(Box.createVerticalStrut(20));
		mainpanel.add(loglabel);
		mainpanel.add(scroll);
		
		add(mainpanel);
		setPreferredSize(new Dimension(width,height));
		super.pack();
		super.setLocationRelativeTo(null);
		super.setVisible(true);
		progressbar.setVisible(false);
	}
	public void addComponent(Component component, int position){
		mainpanel.add(component,position);
	}
	public int getComponentCount(){
		return mainpanel.getComponentCount();
	}
	public void updateProgress(String msg){
		progresslabel.setText(msg);
		progressbar.setVisible(false);
		progressbar.setValue(0);
/*		if(timer != null && timer.isRunning())
			timer.stop();
		timer = new Timer(40, this);
		timer.setInitialDelay(10000);
		timer.start();
*/	}
	public void updateProgress(int percent){
		if(percent == -1)
			progressbar.setVisible(false);
		else{
			progressbar.setVisible(true);
			progressbar.setValue(percent);
		}
	}
	public void updateLog(String msg){
		logarea.append(msg);
		logarea.setCaretPosition(logarea.getDocument().getLength());
	}
	public void updateInfo(String msg){
//		infolabel.setFadedOut();
		infolabel.setText(msg);
//		infolabel.fadein();
	}
	public void updateInfo2(String msg){
//		infolabel.setFadedOut();
		infolabel2.setText(msg);
//		infolabel.fadein();
	}
	
	public void finish(){
//		timer.stop();
		super.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		updateInfo("This might take a while");
		timer.stop();
	}
	
	public static void main(String args[]){
		ProgressDialog s = new ProgressDialog("Test Frame");
		try{
			Thread.sleep(2000);
		} catch (Exception e){}
		s.updateInfo("This may take a while");
	}
	
}

class FadingLabel extends JLabel implements ActionListener{

    private Timer timer;

    public FadingLabel() {
    	super(" ");
    }

    public FadingLabel(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
        
    }

    public void fadein(){
    	
    	if(timer!=null && timer.isRunning())
    		timer.stop();
    	timer = new Timer(40, this);
    	timer.setDelay(100);
    	timer.start();
    }
    
    public void setFadedOut(){
    	Color c = getForeground();
		setForeground(new Color(c.getRed(),c.getGreen(),c.getBlue(),0));
    }

    @Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Color c = getForeground();
		int alpha = c.getAlpha();
		alpha+=17;
		setForeground(new Color(c.getRed(),c.getGreen(),c.getBlue(),alpha));
			
		if(alpha>=255){
			alpha = 255;
			setForeground(new Color(c.getRed(),c.getGreen(),c.getBlue(),255));
			timer.stop();
		}
	}
}

