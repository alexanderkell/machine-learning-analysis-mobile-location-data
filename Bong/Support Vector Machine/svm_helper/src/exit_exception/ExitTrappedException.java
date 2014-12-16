package exit_exception;

import java.security.Permission;

public class ExitTrappedException extends SecurityException {

	  /** Prevents Java virtual machine from exiting when
	   *  System.exit(integer) being called
	   *  See http://stackoverflow.com/questions/5549720/how-to-prevent-calls-to-system-exit-from-terminating-the-jvm
	   */
	public static final long serialVersionUID = 1L;

	// Error message
	private String error_msg;
	private static SecurityManager current_security_manager = null;
	public ExitTrappedException(){
		this(null);
	}
	public ExitTrappedException(String error_msg){
		this.error_msg = error_msg;
	}
	public String toString(){
		return error_msg;
	}

	public static void forbidSystemExitCall(){
		current_security_manager = System.getSecurityManager();
		final SecurityManager securityManager = new SecurityManager() {
			@Override
			public void checkPermission( Permission permission ) {
		        if( "exitVM".equals( permission.getName() ) ) {
		          throw new ExitTrappedException() ;
		        }
		      }
			@Override
			public void checkExit(int status) {
				throw new ExitTrappedException("System.exit("+status+")is being called");
			}
		} ;
		
		System.setSecurityManager( securityManager ) ;
	}

    public static void enableSystemExitCall() {
	    System.setSecurityManager(current_security_manager) ;
	}
	  
}
