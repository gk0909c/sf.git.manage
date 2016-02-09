package gk0909c.sf.git.manage;

import org.eclipse.jgit.api.Git;
import org.yaml.snakeyaml.Yaml;

import gk0909c.sf.git.manage.git.GitOperator;
import gk0909c.sf.git.manage.git.RepositoryInfo;
import gk0909c.sf.git.manage.zip.ZipCreater;

/**
 * Get SfdcMetadata From git, and deploy metadata and run test.
 * @author satk0909
 *
 */
public class App {
    public static void main( String[] args ) throws Exception {
    	// リポジトリ情報の取得
    	Yaml yaml = new Yaml();
		String gitYaml = "reporsitoryInfo.yml";
		RepositoryInfo repositoryInfo = yaml.loadAs(ClassLoader.getSystemResourceAsStream(gitYaml), RepositoryInfo.class);
		
		// リポジトリ取得
		GitOperator gitOperator = new GitOperator(repositoryInfo);
		Git git = gitOperator.cloneReporsitory();
		
		// 最新化
		gitOperator.pullBranch(git);
		
		// Zip作成
		ZipCreater helper = new ZipCreater();
		String zipFile = repositoryInfo.getLocalBase() + "metadata.zip";
		String baseDir = repositoryInfo.getLocalBase() + repositoryInfo.getRepoName() + "/src";
		helper.createZip(zipFile, baseDir);
    }
}
