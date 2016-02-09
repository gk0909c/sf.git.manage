package gk0909c.sf.git.manage.git;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.TransportCommand;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class GitOperator {
	private RepositoryInfo repInfo;
	
	public GitOperator(RepositoryInfo repInfo) {
		this.repInfo = repInfo;
	}
	
	/**
	 * clone git repository and get the refference object.
	 * if already clone, simply get it.
	 * @return git repository
	 * @throws InvalidRemoteException
	 * @throws TransportException
	 * @throws GitAPIException
	 * @throws IOException
	 */
	public Git cloneReporsitory() throws InvalidRemoteException, TransportException, GitAPIException, IOException {
		Git git = null;
		File localRepository = new File(repInfo.getLocalBase() + repInfo.getRepoName());
		
		if (localRepository.exists()) {
			FileRepositoryBuilder builder = new FileRepositoryBuilder();
	    	Repository repository = builder.setGitDir(new File(localRepository.getAbsolutePath() + "/" + Constants.DOT_GIT))
	    										.readEnvironment().findGitDir().build();
	    	
	    	git = new Git(repository);
		} else {
			CloneCommand clone = Git.cloneRepository();
			clone.setURI(repInfo.getUri());
			clone.setDirectory(localRepository);
			setCredentials(clone);
			
			git = clone.call();
		}
		
		return git;
	}
	
	/**
	 * pull target branch.
	 * @param git
	 * @throws RefAlreadyExistsException
	 * @throws RefNotFoundException
	 * @throws InvalidRefNameException
	 * @throws CheckoutConflictException
	 * @throws GitAPIException
	 */
	public void pullBranch(Git git) throws RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException {
		CheckoutCommand checkout = git.checkout();
		checkout.setCreateBranch(true);
		checkout.setName(repInfo.getBranchName());
		checkout.setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK);
		checkout.setStartPoint("origin" + "/" +  repInfo.getBranchName());
		
		try {
			checkout.call();
		} catch (RefAlreadyExistsException e) {
			git.checkout().setName(repInfo.getBranchName()).call();
		} finally {
			PullCommand pull = git.pull();
			setCredentials(pull);
			pull.call();
		}
	}
	
	/**
	 * set credentials
	 * @param command
	 */
	private void setCredentials(@SuppressWarnings("rawtypes") TransportCommand command) {
		if (repInfo.getUser() != null) {
			command.setCredentialsProvider(new UsernamePasswordCredentialsProvider(repInfo.getUser(), repInfo.getPw()));
		}
	}
}
