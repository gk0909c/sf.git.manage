package gk0909c.sf.git.manage.git;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
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
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

class GitOperator {
	private RepositoryInfo repInfo;
	
	GitOperator(RepositoryInfo repInfo) {
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
	protected Git cloneReporsitory() throws InvalidRemoteException, TransportException, GitAPIException, IOException {
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

			if (repInfo.getUser() != null) {
				CredentialsProvider credential = new UsernamePasswordCredentialsProvider(repInfo.getUser(), repInfo.getPw());
				clone.setCredentialsProvider(credential);
			}
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
	protected void pullBranch(Git git) throws RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException {
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
			git.pull().call();
		}
	}
}
