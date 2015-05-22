package dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;

import javax.swing.Timer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class ProgressDialog extends JFrame implements ActionListener{
	public interface ProgressDialogListener{
		public abstract void onAbort();
	}

	private JLabel progresslabel;
	private JTextArea logarea;
	private JPanel mainpanel;
	private JScrollPane scroll;
	private JLabel infolabel;
//	private JLabel loglabel;
	private java.util.Timer util_timer;
	private javax.swing.Timer swing_timer;
	private JProgressBar progressbar;
	private JLabel infolabel2;
	private JButton cancelbutton;
	private ProgressDialogListener progress_dialog_listener;
	private TimerTask timertask;
	private long starttime;
	private JPanel infopanel2;
	private JLabel progresspic;
	private JPanel infopanel1;
	private JPanel infopanel3;
	
	
	public final static Font progressfontLarge = new Font("large", Font.TRUETYPE_FONT, 18);
	public final static Font progressfontNormal = new Font("normal", Font.TRUETYPE_FONT, 14);
	public final static Font progressfontSmall = new Font("small", Font.BOLD, 12);

	public final static int DEFAULT_WIDTH = 500;
	public final static int DEFAULT_HEIGHT = 400;
	
	public ProgressDialog(String title){
		this(title, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	public ProgressDialog(String title, int width, int height){
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle(title);
		
		mainpanel = new JPanel();
		mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.PAGE_AXIS));
		
		// Icon gif downloaded at: http://preloaders.net/generator.php?image=482&speed=10&fore_color=000000&back_color=FFFFFF&size=28x28&transparency=1&reverse=0&orig_colors=1&gray_transp=1&image_type=1&inverse=0&flip=0&frames_amount=12&word=130-144-11-32-115-140-54-36-36-36&download&uncacher=6.369215785525739
		ImageIcon loading = new ImageIcon("src\\dialogs\\loading.gif", "Loading");
		progresspic = new JLabel(null ,loading, JLabel.CENTER);
		progresslabel = new JLabel("Initialising...", JLabel.LEFT);
		infolabel = new JLabel("", JLabel.LEFT);
		infolabel2 = new JLabel("", JLabel.LEFT);
		
		progressbar = new JProgressBar();
		logarea = new JTextArea();
		infopanel1 = new JPanel();
		infopanel2 = new JPanel();
		infopanel3 = new JPanel();
		
//		loglabel = new JLabel("<html><i>Progress logs</i></html>",JLabel.LEFT);
	    scroll = new JScrollPane (logarea);
	    cancelbutton = new JButton("Cancel");

		// Setup swing components
	    infopanel1.setLayout(new BoxLayout(infopanel1, BoxLayout.LINE_AXIS));
	    infopanel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 20));
	    infopanel1.setAlignmentX(CENTER_ALIGNMENT);
//	    infopanel1.setBackground(new Color(140,183,189));
	    infopanel2.setLayout(new BoxLayout(infopanel2, BoxLayout.PAGE_AXIS));
	    infopanel2.setAlignmentX(LEFT_ALIGNMENT);
	    infopanel2.setAlignmentY(TOP_ALIGNMENT);
//	    infopanel3.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    infopanel3.setLayout(new BoxLayout(infopanel3, BoxLayout.PAGE_AXIS));
	    infopanel3.setAlignmentX(LEFT_ALIGNMENT);
	    progresspic.setAlignmentY(TOP_ALIGNMENT);
	    progresspic.setBorder(new EmptyBorder(0, 10, 10, 10) );
	    // Setup the labels
	    progresslabel.setFont(progressfontLarge);
	    progresslabel.setAlignmentX(LEFT_ALIGNMENT);
	    infolabel.setFont(progressfontSmall);
	    infolabel.setAlignmentX(LEFT_ALIGNMENT);
	    infolabel2.setFont(progressfontSmall);
	    infolabel2.setAlignmentX(LEFT_ALIGNMENT);
	    // Setup progress bar
	    progressbar.setAlignmentX(LEFT_ALIGNMENT);
	    progressbar.setBorderPainted(false);
	    progressbar.setBackground(Color.WHITE);
	    progressbar.setForeground(new Color(100,100,245));
	    progressbar.setPreferredSize(new Dimension(width, 4));
	    // Setup the "Progress log" label
//	    loglabel.setAlignmentX(CENTER_ALIGNMENT);
	    // Setup the text log area
	  	logarea.setLineWrap(true);
		logarea.setEditable(false);
		logarea.setAutoscrolls(true);
		logarea.setAlignmentX(CENTER_ALIGNMENT);
		
		cancelbutton.setFont(ProgressDialog.progressfontSmall);
		cancelbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
		cancelbutton.setAlignmentY(TOP_ALIGNMENT);
		cancelbutton.addActionListener(this);
		
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Add components to the main panel
//		mainpanel.add(Box.createVerticalStrut(10));
		infopanel2.add(progresslabel);
		infopanel2.add(Box.createVerticalStrut(10));
		
		infopanel3.add(infolabel);
		infopanel3.add(Box.createVerticalStrut(10));
		infopanel3.add(infolabel2);
		infopanel1.add(progresspic);
		infopanel1.add(infopanel2);
		infopanel1.add(cancelbutton);
		infopanel2.add(infopanel3);
		infopanel2.add(Box.createVerticalStrut(5));
		infopanel2.add(progressbar);
		mainpanel.add(infopanel1);
		mainpanel.add(Box.createVerticalStrut(10));
		mainpanel.add(scroll);
		
		
		add(mainpanel);
		setMinimumSize(new Dimension(width,height));
		super.pack();
		super.setLocationRelativeTo(null);
		super.setVisible(true);
		progressbar.setVisible(false);
	}
	public void addComponent(Component component, int position){
		mainpanel.add(component,position);
	}
	
	public void setCancelButtonEnabled(boolean enable){
		cancelbutton.setEnabled(enable);
		cancelbutton.setVisible(enable);
	}
	public void startTimer(){
		timertask = new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				long elapsed = (System.currentTimeMillis() - starttime)/1000;
				String elapsed_time_msg = "Elapsed Time: "+elapsed+" second";
				if(elapsed != 1){
					elapsed_time_msg += "s";
				}
				updateInfo2(elapsed_time_msg);
			}
			
		};
		if(util_timer != null){
			util_timer.cancel();
		}
		util_timer = new java.util.Timer();
		starttime = System.currentTimeMillis();
		util_timer.scheduleAtFixedRate(timertask, 0, 1000);
	}
	public long stopTimer(){
		util_timer.cancel();
		final long elapsed = (System.currentTimeMillis() - starttime)/1000;
		String timespentmsg = "Total Time spent: "+elapsed+" second(s)";
		updateInfo2(timespentmsg);
		updateLog(timespentmsg);
		return elapsed;
	}
	public boolean getCancelButtonEnabled(boolean enable){
		return cancelbutton.isEnabled();
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
		if(!msg.endsWith("\n"))
			msg = msg+"\n";
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
	
	public void setProgressDialogListener(ProgressDialogListener progress_dialog_listener){
		this.progress_dialog_listener = progress_dialog_listener;
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == cancelbutton){
			if(progress_dialog_listener!=null)
				progress_dialog_listener.onAbort();
		}else{
			updateInfo("This might take a while");
			swing_timer.stop();
		}
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