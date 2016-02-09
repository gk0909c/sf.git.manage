package gk0909c.sf.git.manage.sfdc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.metadata.AsyncResult;
import com.sforce.soap.metadata.CodeCoverageWarning;
import com.sforce.soap.metadata.DeployDetails;
import com.sforce.soap.metadata.DeployMessage;
import com.sforce.soap.metadata.DeployOptions;
import com.sforce.soap.metadata.DeployResult;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.metadata.RunTestFailure;
import com.sforce.soap.metadata.RunTestsResult;
import com.sforce.soap.partner.LoginResult;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

import gk0909c.sf.git.manage.zip.ZipCreater;

public class SfdcOperator {
	private MetadataConnection metaConn;
	
	public SfdcOperator(SfdcInfo info) throws ConnectionException {
		ConnectorConfig config = new ConnectorConfig();
		config.setAuthEndpoint(info.getPartnerUri());
		config.setServiceEndpoint(info.getPartnerUri());
		config.setManualLogin(true);
		PartnerConnection partnerConn = new PartnerConnection(config);
		LoginResult loginResult = partnerConn.login(info.getUser(), info.getPw() + info.getSecurityToken());
				
		config = partnerConn.getConfig();
		config.setServiceEndpoint(loginResult.getMetadataServerUrl());
		config.setSessionId(loginResult.getSessionId());
		metaConn = new MetadataConnection(config);
	}
	
	public void getMetadata() throws Exception {
		String ZIP_FILE = "C:/git-temp/tmp/test.zip";
		long ONE_SECOND = 1000;
		int MAX_NUM_POLL_REQUESTS = 50;
		
		FileInputStream fileInputStream = new FileInputStream(ZIP_FILE);
		byte zipBytes[] = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			int bytesRead = 0;
			while (-1 != (bytesRead = fileInputStream.read(buffer))) {
				bos.write(buffer, 0, bytesRead);
			}
			zipBytes = bos.toByteArray();
		} finally {
			fileInputStream.close();
		}
		
		DeployOptions deployOptions = new DeployOptions();
		deployOptions.setPerformRetrieve(false);
		deployOptions.setRollbackOnError(true);
		AsyncResult asyncResult = metaConn.deploy(zipBytes, deployOptions);

		// wait finish =================================================================
		int poll = 0;
		long waitTimeMilliSecs = ONE_SECOND;
		DeployResult deployResult;
		boolean fetchDetails;
		do {
			Thread.sleep(waitTimeMilliSecs);
			// double the wait time for the next iteration
			waitTimeMilliSecs *= 2;
			if (poll++ > MAX_NUM_POLL_REQUESTS) {
				throw new Exception(
						"Request timed out. If this is a large set of metadata components, "
								+
						"ensure that MAX_NUM_POLL_REQUESTS is sufficient.");
			}
			// Fetch in-progress details once for every 3 polls
			fetchDetails = (poll % 3 == 0);
			deployResult = metaConn.checkDeployStatus(asyncResult.getId(), fetchDetails);
			System.out.println("Status is: " + deployResult.getStatus());
			if (!deployResult.isDone() && fetchDetails) {
				printErrors(deployResult, "Failures for deployment in progress:\n");
			}
		}
		while (!deployResult.isDone());
		// wait finish =================================================================
		
		if (!deployResult.isSuccess() && deployResult.getErrorStatusCode() != null) {
			throw new Exception(deployResult.getErrorStatusCode() + " msg: " +
					deployResult.getErrorMessage());
		}
		
		if (!fetchDetails) {
			// Get the final result with details if we didn't do it in the last attempt.
			deployResult = metaConn.checkDeployStatus(asyncResult.getId(), true);
		}
		
		if (!deployResult.isSuccess()) {
			printErrors(deployResult, "Final list of failures:\n");
			throw new Exception("The files were not successfully deployed");
		}
	}
	
	private static void printErrors(DeployResult result, String messageHeader) {
		DeployDetails details = result.getDetails();
		StringBuilder stringBuilder = new StringBuilder();
		if (details != null) {
			DeployMessage[] componentFailures = details.getComponentFailures();
			for (DeployMessage failure : componentFailures) {
				String loc = "(" + failure.getLineNumber() + ", " +
						failure.getColumnNumber();
				if (loc.length() == 0 &&
						!failure.getFileName().equals(failure.getFullName()))
				{
					loc = "(" + failure.getFullName() + ")";
				}
				stringBuilder.append(failure.getFileName() + loc + ":"
						+ failure.getProblem()).append('\n');
			}
			RunTestsResult rtr = details.getRunTestResult();
			if (rtr.getFailures() != null) {
				for (RunTestFailure failure : rtr.getFailures()) {
					String n = (failure.getNamespace() == null ? "" :
						(failure.getNamespace() + ".")) + failure.getName();
					stringBuilder.append("Test failure, method: " + n + "." +
							failure.getMethodName() + " -- " + failure.getMessage() +
							" stack " + failure.getStackTrace() + "\n\n");
				}
			}
			if (rtr.getCodeCoverageWarnings() != null) {
				for (CodeCoverageWarning ccw : rtr.getCodeCoverageWarnings()) {
					stringBuilder.append("Code coverage issue");
					if (ccw.getName() != null) {
						String n = (ccw.getNamespace() == null ? "" :
							(ccw.getNamespace() + ".")) + ccw.getName();
						stringBuilder.append(", class: " + n);
					}
					stringBuilder.append(" -- " + ccw.getMessage() + "\n");
				}
			}
		}
		if (stringBuilder.length() > 0) {
			stringBuilder.insert(0, messageHeader);
			System.out.println(stringBuilder.toString());
		}
	}
}
