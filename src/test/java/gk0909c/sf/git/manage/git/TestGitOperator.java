package gk0909c.sf.git.manage.git;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import gk0909c.sf.git.manage.test.helper.TestHelper;
import junit.framework.TestCase;

public class TestGitOperator extends TestCase {
	@Test
	public void testGetReporsitory01() throws Exception {
		Yaml yaml = new Yaml();
		String yamlFile = "TestGitOperator/testGetReporsitory01.yml";
		RepositoryInfo info = yaml.loadAs(ClassLoader.getSystemResourceAsStream(yamlFile), RepositoryInfo.class);
		
		GitOperator operator = new GitOperator(info);
		Git git = operator.cloneReporsitory();
		
		try {
			assertTrue(new File("C:/git-temp/sf.git.manage/.git").exists());
			
			git = operator.cloneReporsitory();
			operator.pullBranch(git);
			operator.pullBranch(git);
		} finally {
			git.close();
			TestHelper.delete("C:/git-temp/sf.git.manage");
		}
		
	}
	
	@Test
	public void testGetReporsitory02() throws Exception {
		Yaml yaml = new Yaml();
		String yamlFile = "TestGitOperator/testGetReporsitory02.yml";
		RepositoryInfo info = yaml.loadAs(ClassLoader.getSystemResourceAsStream(yamlFile), RepositoryInfo.class);
				
		GitOperator operator = new GitOperator(info);
		Git git = operator.cloneReporsitory();
		
		try {
			assertTrue(new File("C:/git-temp/sf.git.manage/.git").exists());
		} finally {
			git.close();
			TestHelper.delete("C:/git-temp/sf.git.manage");
		}
	}
}
