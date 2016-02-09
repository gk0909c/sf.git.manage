package gk0909c.sf.git.manage.sfdc;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import com.sforce.soap.metadata.DeleteResult;

import gk0909c.sf.git.manage.zip.ZipCreater;
import gk0909c.sf.git.manage.zip.ZipInfo;


public class TestSfdcDeployer {
	private ZipInfo zipInfo;
	private SfdcInfo sfdcInfo;
	
	@Test
	public void testDeployMetadata01() throws Exception {
		setUp("TestSfdcDeployer/testDeployMetadata01", "TestSfdcDeployer/testDeployMetadata.yml");

		SfdcDeployer operator = new SfdcDeployer(sfdcInfo);
		operator.deployMetadata(zipInfo);
		
		DeleteResult[] result  = SfdcConnector.getConnection(sfdcInfo).getMetadataConnection()
				.deleteMetadata("CustomLabel", new String[]{"Gk_TestDeployLabel"});
		new File(zipInfo.getZipPath()).delete();
		
		assertTrue(result[0].getSuccess());
	}
	
	@Test
	public void testDeployMetadata02() throws Exception {
		setUp("TestSfdcDeployer/testDeployMetadata02", "TestSfdcDeployer/testDeployMetadata.yml");

		SfdcDeployer operator = new SfdcDeployer(sfdcInfo);
		try {
			operator.deployMetadata(zipInfo);
			fail();
		} catch (Exception e) {
		} finally {
			new File(zipInfo.getZipPath()).delete();
		}
	}
	
	private void setUp(String resourceBase, String ymlPath) throws Exception {
		String fileName = this.getClass().getClassLoader().getResource(resourceBase).getPath();
		String baseDir = new File(fileName).getAbsolutePath();
		
		zipInfo = new ZipInfo();
		zipInfo.setZipPath(baseDir + "/test.zip");
		zipInfo.setBaseDir(baseDir + "/src");
		ZipCreater zip = new ZipCreater();
		zip.createZip(zipInfo.getZipPath(), zipInfo.getBaseDir());
		
		Yaml yaml = new Yaml();
		sfdcInfo = yaml.loadAs(ClassLoader.getSystemResourceAsStream(ymlPath), SfdcInfo.class);
	}

}
