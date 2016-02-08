package gk0909c.sf.git.manage;

import gk0909c.sf.git.manage.git.GitFacade;

/**
 * Get SfdcMetadata From git, and deploy metadata and run test.
 * @author satk0909
 *
 */
public class App {
    public static void main( String[] args ) throws Exception {
    	// Gitからソース取得
    	GitFacade git = new GitFacade();
    	git.getResource();
    }
}
