package gk0909c.sf.git.manage.sfdc;

import com.sforce.soap.apex.SoapConnection;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

/**
 * create sfdc connection.
 * @author satk0909
 *
 */
class SfdcConnector {
	private static SfdcConnector me;
	private MetadataConnection metaConn;
	private SoapConnection soapConn;
	
	private SfdcConnector(){}
	
	protected static SfdcConnector getConnection(SfdcInfo info) throws ConnectionException {
		if (me != null) {
			return me;
		}
		
		// login
		me = new SfdcConnector();
		ConnectorConfig partnerConfig = new ConnectorConfig();
		partnerConfig.setAuthEndpoint(info.getPartnerUri());
		partnerConfig.setServiceEndpoint(info.getPartnerUri());
		partnerConfig.setManualLogin(true);
		setProxy(partnerConfig);
		PartnerConnection partnerConn = new PartnerConnection(partnerConfig);
		LoginResult loginResult = partnerConn.login(info.getUser(),
														info.getPw() + info.getSecurityToken());
		
		// get Metadata connection
		ConnectorConfig metadataConfig = new ConnectorConfig();
		metadataConfig.setServiceEndpoint(loginResult.getMetadataServerUrl());
		metadataConfig.setSessionId(loginResult.getSessionId());
		setProxy(metadataConfig);
		me.metaConn = new MetadataConnection(metadataConfig);
		
		// get Soap Connection
		ConnectorConfig soapConfig = new ConnectorConfig();
		soapConfig.setServiceEndpoint(loginResult.getServerUrl().replace("Soap/u", "Soap/s"));
		soapConfig.setSessionId(loginResult.getSessionId());
		setProxy(soapConfig);
		me.soapConn = new SoapConnection(soapConfig);
		
		return me;
	}
	
	protected SoapConnection getSoapConnection() {
		return soapConn;
	}
	
	protected MetadataConnection getMetadataConnection() {
		return metaConn;
	}
	
	
	/**
	 * proxy setting
	 * @param config
	 */
	private static void setProxy(ConnectorConfig config) {
		if (System.getProperty("https.proxyHost") != null ) {
			config.setProxy(System.getProperty("https.proxyHost"),
							Integer.parseInt(System.getProperty("https.proxyPort")));
		}
		
		if (System.getProperty("https.proxyUser") != null ) {
			config.setProxyUsername(System.getProperty("https.proxyUser"));
		}
		
		if (System.getProperty("https.proxyPassword") != null ) {
			config.setProxyPassword(System.getProperty("https.proxyPassword"));
		}
	}
}
