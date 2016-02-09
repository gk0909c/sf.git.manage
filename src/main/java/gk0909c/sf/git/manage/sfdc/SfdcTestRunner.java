package gk0909c.sf.git.manage.sfdc;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sforce.soap.apex.CodeCoverageResult;
import com.sforce.soap.apex.CodeCoverageWarning;
import com.sforce.soap.apex.RunTestFailure;
import com.sforce.soap.apex.RunTestsRequest;
import com.sforce.soap.apex.RunTestsResult;
import com.sforce.soap.apex.SoapConnection;
import com.sforce.ws.ConnectionException;

public class SfdcTestRunner {
	private SoapConnection soapConn;
	private Logger logger = LoggerFactory.getLogger(SfdcTestRunner.class);
	
	public SfdcTestRunner(SfdcInfo info) throws ConnectionException {
		SfdcConnector conn = SfdcConnector.getConnection(info);
		
		soapConn = conn.getSoapConnection();
	}
	
	public void runTest() throws ConnectionException {
		RunTestsRequest req = new RunTestsRequest();
		req.setAllTests(true);
	
		RunTestsResult runResult = soapConn.runTests(req);
		
		CodeCoverageResult[] coverages = runResult.getCodeCoverage();
		CodeCoverageWarning[] warnings = runResult.getCodeCoverageWarnings();
		RunTestFailure[] failures = runResult.getFailures();
	
		CovarageInfo coverageInfo = new CovarageInfo();
		final String coverageMessage = "\tClass: %s, step: %d, coverage: %.2f";
		
		logger.info("## Coverages ##");
		for (CodeCoverageResult result : coverages) {
			coverageInfo.putCovarage(result);
			
			logger.info(String.format(coverageMessage,
										result.getName(),
										result.getNumLocations(),
										coverageInfo.calc()));
		}
		
		logger.info("## Warnings ##");
		for (CodeCoverageWarning warning : warnings) {
			logger.warn("\t" + warning.getMessage());
		}

		logger.info("## Failures ##");
		for (RunTestFailure failure : failures) {
			logger.error("\t" + failure.getStackTrace());
			logger.error("\t\t" + failure.getMessage());
		}
		
		logger.info("## total coverage ##");
		logger.info(String.format("\t%.2f", coverageInfo.totalCalc()));
	}
	
	private class CovarageInfo {
		private BigDecimal totalStep;
		private BigDecimal totalNotCovered;
		private CodeCoverageResult coverage;
		
		private CovarageInfo() {
			totalStep = new BigDecimal(0);
			totalNotCovered = new BigDecimal(0);
		}
		
		// add total
		private void putCovarage(CodeCoverageResult coverage) {
			this.coverage = coverage;

			totalStep = totalStep.add(new BigDecimal(coverage.getNumLocations()));
			totalNotCovered = totalNotCovered.add(new BigDecimal(coverage.getNumLocationsNotCovered()));
		}
		
		// get temp covarage rate.
		private double calc() {
			BigDecimal step = new BigDecimal(coverage.getNumLocations());
			BigDecimal notCoverd = new BigDecimal(coverage.getNumLocationsNotCovered());

			return getCoveredRate(step, notCoverd);
		}
		
		// get total covarage rate.
		private double totalCalc() {
			return getCoveredRate(totalStep, totalNotCovered);
		}
		
		private double getCoveredRate(BigDecimal step, BigDecimal notCovered) {
			double notCoveredRate = notCovered.divide(step, 2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
			return 1 - notCoveredRate;
		}
	}
}
