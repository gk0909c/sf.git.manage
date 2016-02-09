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
	private SoapConnection soapConn;
	private MetadataConnection metaConn;
	
	private SfdcConnector(){}
	
	protected static SfdcConnector getConnection(SfdcInfo info) throws ConnectionException {
		if (me != null) {
			return me;
		}
		
		// login
		me = new SfdcConnector();
		ConnectorConfig config = new ConnectorConfig();
		config.setAuthEndpoint(info.getPartnerUri());
		config.setServiceEndpoint(info.getPartnerUri());
		config.setManualLogin(true);
		setProxy(config);
		PartnerConnection partnerConn = new PartnerConnection(config);
		LoginResult loginResult = partnerConn.login(info.getUser(),
														info.getPw() + info.getSecurityToken());
		
		// get metadata connection
		config = partnerConn.getConfig();
		config.setServiceEndpoint(loginResult.getMetadataServerUrl());
		config.setSessionId(loginResult.getSessionId());
		me.metaConn = new MetadataConnection(config);
		
		// get SoapConnection
		config = partnerConn.getConfig();
		config.setServiceEndpoint(loginResult.getServerUrl().replace("Soap/u", "Soap/s"));
		config.setSessionId(loginResult.getSessionId());
		me.soapConn = new SoapConnection(config);
		
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
