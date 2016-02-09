package gk0909c.sf.git.manage;

import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.yaml.snakeyaml.Yaml;

import gk0909c.sf.git.manage.git.GitOperator;
import gk0909c.sf.git.manage.git.RepositoryInfo;
import gk0909c.sf.git.manage.sfdc.SfdcInfo;
import gk0909c.sf.git.manage.sfdc.SfdcDeployer;
import gk0909c.sf.git.manage.zip.ZipCreater;
import gk0909c.sf.git.manage.zip.ZipInfo;

/**
 * Get SfdcMetadata From git, and deploy metadata and run test.
 * @author satk0909
 *
 */
public class App {
	/**
	 * main method.
	 * @param args
	 * @throws Exception
	 */
    public static void main( String[] args ) throws Exception {
    	// 設定情報取得
    	Yaml yaml = new Yaml();
		@SuppressWarnings("unchecked")
		Map<String, Object> settingMap = yaml.loadAs(ClassLoader.getSystemResourceAsStream("setting.yml"), Map.class);
		
		// リポジトリ取得
		RepositoryInfo repositoryInfo = (RepositoryInfo)settingMap.get("repository");
		GitOperator gitOperator = new GitOperator(repositoryInfo);
		Git git = gitOperator.cloneReporsitory();
		
		// 最新化
		gitOperator.pullBranch(git);
		
		// Zip作成
		ZipInfo zipInfo = (ZipInfo)settingMap.get("zip");
		ZipCreater helper = new ZipCreater();
		helper.createZip(zipInfo.getZipPath(), zipInfo.getBaseDir());
		
		// Deploy
		SfdcInfo sfdcInfo = (SfdcInfo)settingMap.get("sfdc");
		SfdcDeployer operator = new SfdcDeployer(sfdcInfo);
		operator.deployMetadata(zipInfo);
		
		// Run Test
		
    }
}
