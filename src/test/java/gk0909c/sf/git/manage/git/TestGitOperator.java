package gk0909c.sf.git.manage.git;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.yaml.snakeyaml.Yaml;

import static org.junit.Assert.*;

public class TestGitOperator {
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	
	@Test
	public void testGetReporsitory01() throws Exception {
		Yaml yaml = new Yaml();
		String yamlFile = "TestGitOperator/testGetReporsitory01.yml";
		RepositoryInfo info = yaml.loadAs(ClassLoader.getSystemResourceAsStream(yamlFile), RepositoryInfo.class);
		info.setLocalBase(tempFolder.getRoot() + "/");
		
		GitOperator operator = new GitOperator(info);
		Git git = operator.cloneReporsitory();
		
		try {
			assertTrue(new File(info.getLocalBase() + info.getRepoName() + "/" + Constants.DOT_GIT).exists());
			
			git = operator.cloneReporsitory();
			operator.pullBranch(git);
			operator.pullBranch(git);
		} finally {
			git.getRepository().close();
			git.close();
		}
		
	}
	
	@Test
	public void testGetReporsitory02() throws Exception {
		Yaml yaml = new Yaml();
		String yamlFile = "TestGitOperator/testGetReporsitory02.yml";
		RepositoryInfo info = yaml.loadAs(ClassLoader.getSystemResourceAsStream(yamlFile), RepositoryInfo.class);
		info.setLocalBase(tempFolder.getRoot() + "/");
		
		GitOperator operator = new GitOperator(info);
		Git git = operator.cloneReporsitory();
		
		try {
			assertTrue(new File(info.getLocalBase() + info.getRepoName() + "/" + Constants.DOT_GIT).exists());
		} finally {
			git.getRepository().close();
			git.close();
		}
	}
}
