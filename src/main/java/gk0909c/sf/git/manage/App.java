package gk0909c.sf.git.manage;

import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import gk0909c.sf.git.manage.git.GitOperator;
import gk0909c.sf.git.manage.git.RepositoryInfo;
import gk0909c.sf.git.manage.sfdc.SfdcInfo;
import gk0909c.sf.git.manage.sfdc.SfdcTestRunner;
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
    	Logger logger = LoggerFactory.getLogger(App.class);
    	logger.info("START");
    	
    	// 設定情報取得
    	Yaml yaml = new Yaml();
		@SuppressWarnings("unchecked")
		Map<String, Object> settingMap = yaml.loadAs(ClassLoader.getSystemResourceAsStream("setting.yml"), Map.class);

    	// 確認
		if (!confirmContinue(settingMap)) {
    		return;
    	}
    	
		// メタデータ取得
    	logger.info("Get repository resource.");
		RepositoryInfo repositoryInfo = (RepositoryInfo)settingMap.get("repository");
		GitOperator gitOperator = new GitOperator(repositoryInfo);
		Git git = gitOperator.cloneReporsitory();
		
		// 最新化
		gitOperator.pullBranch(git);
		
		// Zip作成
    	logger.info("create upload zipfile.");
		ZipInfo zipInfo = (ZipInfo)settingMap.get("zip");
		ZipCreater helper = new ZipCreater();
		helper.createZip(zipInfo.getZipPath(), zipInfo.getBaseDir());
		
		// Deploy
    	logger.info("deploy to Salesforce.");
		SfdcInfo sfdcInfo = (SfdcInfo)settingMap.get("sfdc");
		SfdcDeployer operator = new SfdcDeployer(sfdcInfo);
		operator.deployMetadata(zipInfo);
		
		// Run Test
    	logger.info("run tests.");
		SfdcTestRunner runner = new SfdcTestRunner(sfdcInfo);
		runner.runTest();
		
		logger.info("END");
    }
    
    private static boolean confirmContinue(Map<String, Object> settingMap) {
    	RepositoryInfo repositoryInfo = (RepositoryInfo)settingMap.get("repository");
		SfdcInfo sfdcInfo = (SfdcInfo)settingMap.get("sfdc");
    	
    	System.out.println("## Repository ##");
    	System.out.println("\tRepository: " + repositoryInfo.getUri());
    	System.out.println("\tBranch: " + repositoryInfo.getBranchName());
    	System.out.println("## SFDC ##");
    	System.out.println("\tuser: " + sfdcInfo.getUser());
    	System.out.println("\turl: " + sfdcInfo.getPartnerUri());
    	System.out.print("Continue?(y, n) > ");
    	
    	java.util.Scanner sc = new java.util.Scanner(System.in);
    	String input = sc.nextLine();
    	sc.close();
    	
    	if ("y".equals(input)) {
    		return true;
    	} else {
    		return false;
    	}
    }
}
