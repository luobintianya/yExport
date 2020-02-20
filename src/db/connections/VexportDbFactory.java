package db.connections;

import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.DriverManager;
import java.util.Properties;

import de.hybris.platform.virtualjdbc.constants.VjdbcConstants.DB;
import de.simplicit.vjdbc.VirtualConnection;

public class VexportDbFactory {

	public static VirtualConnection getVjdbConnect(String url) {
		VirtualConnection vjdbcCon = null;
		try {
			String vjdbcId = "vjdbc";
			Class.forName(DB.VJDBC_DRIVER_CLASS).newInstance();
			// String url =
			// "";
			vjdbcCon = (VirtualConnection) DriverManager
					.getConnection("jdbc:hybris:flexiblesearch:" + url + "," + vjdbcId, getUserPrincipals(true));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return vjdbcCon;

	}
	public static VirtualConnection getDefaultVjdbConnect() {
		String url="https://adm.xxx.com/virtualjdbc/service?tenant=master";
		return getVjdbConnect(url);
	}
	static Properties getUserPrincipals(boolean readWrite) {
		Properties props = new Properties();
		if (readWrite) {
			props.put("vjdbc.login.user", "xxx");
			props.put("vjdbc.login.password", "Aa123456");
			//props.put("vjdbc.login.password", "1234");
		} else {
			props.put("vjdbc.login.user", "xxxr");
			props.put("vjdbc.login.password", "Aa123456");
			//props.put("vjdbc.login.password", "1234");
		}

		return props;
	}
	

	private static void testConnection(String url) throws Exception {
		HttpURLConnection conn = null;
		ObjectOutputStream oos = null;

		try {
			URL _url = new URL(url);
			conn = (HttpURLConnection) _url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setAllowUserInteraction(false);
			conn.setUseCaches(false);
			conn.setRequestProperty("Content-type", "binary/x-java-serialized");
			conn.setRequestProperty("vjdbc-method", "connect");
			oos = new ObjectOutputStream(conn.getOutputStream());
			oos.writeUTF("Some blah ....");
			oos.flush();
			conn.connect();
			System.out.print("----CONNECT---");
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (Exception var10) {
					System.out.println(var10.getMessage());
				}

				if (conn != null) {
					conn.disconnect();
				}
			}

		}

	}
}
