package gk0909c.sf.git.manage.sfdc;

import static org.junit.Assert.*;


import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

public class TestSfdcTestRunner {
	@Test
	public void testDeployMetadata01() throws Exception {
		Yaml yaml = new Yaml();
		String ymlPath = "TestSfdcTestRunner/testRunTest.yml";
		SfdcInfo sfdcInfo = yaml.loadAs(ClassLoader.getSystemResourceAsStream(ymlPath), SfdcInfo.class);
		
		SfdcTestRunner runner = new SfdcTestRunner(sfdcInfo);
		try {
			runner.runTest();
		} catch (Exception e) {
			fail();
		}
	}
}
