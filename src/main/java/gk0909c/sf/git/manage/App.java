package gk0909c.sf.git.manage;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 * Hello world!
 *
 */
public class App {
	private static final String REPO_BASE = "C:/git-temp/";
    public static void main( String[] args ) throws IOException, InvalidRemoteException, TransportException, GitAPIException {
    	
    	FileRepositoryBuilder builder = new FileRepositoryBuilder();
    	Repository repository = builder.setGitDir(new File(REPO_BASE + "TestProject/" + Constants.DOT_GIT))
    										.readEnvironment().findGitDir().build();
    	
    	Git git = new Git(repository);
    	
    	/*String user = "satohk";
    	String password = "password";
    	CredentialsProvider credential = new UsernamePasswordCredentialsProvider(user, password);
    	
    	CloneCommand clone = Git.cloneRepository();
    	clone.setURI("http://172.16.120.152:10080/satohk/TestProject.git");
    	clone.setDirectory(new File(REPO_BASE + "TestProject"));
    	clone.setCredentialsProvider(credential);
    	Git git = clone.call();*/
    	
    	// チェックアウト
    	try{
    	git.checkout().setCreateBranch(true).setName("dev")
        .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.SET_UPSTREAM)
        .setStartPoint("origin/" + "dev").call();
    	} catch (RefAlreadyExistsException e) {
    		System.out.println(e.getMessage());
    		
    		git.checkout().setName("master").call();
            //git.pull().call();
    	}
    	
    	// ブランチ一覧
    	List<Ref> branchList = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
    	for (Ref ref : branchList) {
    		System.out.println(ref.getName());
    	}
    	
    }
}
