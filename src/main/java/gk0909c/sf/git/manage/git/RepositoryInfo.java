package gk0909c.sf.git.manage.git;

import lombok.Data;

@Data
public
class RepositoryInfo {
	/** Remote Repository User */
	private String user;
	/** Remote Repository Password */
	private String pw;
	/** Remote Repository URI */
	private String uri;
	/** Forlder Name of Local Repository */
	private String repoName;
	/** Local Repository Location (if not empty, it's need to end by "/") */
	private String localBase;
	/** target branch */
	private String branchName;
}
