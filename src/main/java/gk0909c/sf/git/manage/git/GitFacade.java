package gk0909c.sf.git.manage.git;

import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.yaml.snakeyaml.Yaml;

public class GitFacade {
	public void getResource() throws InvalidRemoteException, TransportException, GitAPIException, IOException {
		// リポジトリ情報の取得
    	Yaml yaml = new Yaml();
		String yamlFile = "reporsitoryInfo.yml";
		RepositoryInfo info = yaml.loadAs(ClassLoader.getSystemResourceAsStream(yamlFile), RepositoryInfo.class);
		
		// リポジトリ取得
		GitOperator operator = new GitOperator(info);
		Git git = operator.cloneReporsitory();
		
		// 最新化
		operator.pullBranch(git);
	}
}
