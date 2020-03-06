package db.connections;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.sql.DriverManager;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import de.hybris.platform.virtualjdbc.constants.VjdbcConstants.DB;
import de.simplicit.vjdbc.VirtualConnection;

public class VexportDbFactory {
	

	public static VirtualConnection getVjdbConnect(String url, String userName, String password
			) {
		trustAllHosts();
		VirtualConnection vjdbcCon = null;
		try {
			url=url+"/virtualjdbc/service?tenant=master";
			String vjdbcId = "vjdbc";
			Class.forName(DB.VJDBC_DRIVER_CLASS).newInstance();
//			if (flexibleSearch) {
				vjdbcCon = (VirtualConnection) DriverManager.getConnection(
						"jdbc:hybris:flexiblesearch:" + url + "," + vjdbcId, getUserPrincipals(userName, password));
//			} else {
//				vjdbcCon = (VirtualConnection) DriverManager.getConnection("jdbc:hybris:sql:" + url + "," + vjdbcId,
//						getUserPrincipals(userName, password));
//			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return vjdbcCon;

	}

	public static VirtualConnection getDefaultVjdbConnect(String url, String userName, String password
			) {

		return getVjdbConnect(url, userName, password);
	}

	static Properties getUserPrincipals(String userName, String passwd) {
		Properties props = new Properties();

		props.put("vjdbc.login.user", userName);
		props.put("vjdbc.login.password", passwd);

		return props;
	}
	
	
	/**
	 * ∫ˆ¬‘÷§ È¥ÌŒÛ
	 */
	private static void trustAllHosts() {

		try {

			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}

	};
}
