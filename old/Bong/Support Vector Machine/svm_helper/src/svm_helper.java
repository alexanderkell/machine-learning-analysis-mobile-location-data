public class svm_helper {

	/**SVM_helper main method
	 * Launch svm_train_helper window
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					new svm_train_helper("SVM Helper");
			    }
			});
	    }

}
