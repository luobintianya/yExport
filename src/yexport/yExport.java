package yexport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import org.apache.log4j.Logger;

import db.connections.VexportDbFactory;
import db.utils.ConnectUtils;
import de.simplicit.vjdbc.VirtualConnection;

public class yExport {
	private static final Logger LOG = Logger.getLogger(yExport.class);

	public static void main(String args[]) throws Exception {
		String serverUrl = "https://10.255.251.20:9002";
		String useName = "robin";// 用户名
		String password = "1234"; // 密码
		String outPutfile = "D://mydata_vendor.txt"; // 输出到的目录文件
		String flexibleSql = "SELECT {code} FROM {vendors}";
		String sql = "SELECT p_code FROM vendors item_t0";

		VirtualConnection vjdbcCon = VexportDbFactory.getDefaultVjdbConnect(serverUrl, useName, password);
		defaultQuery(vjdbcCon, sql, outPutfile);//执行flexible 或者原生sql
	}

	
	
	
	
	
	
	
	
	
	static void defaultQuery(final VirtualConnection con, final String sql, final String outFile) {
		ResultSet rs = null;
		Statement stmt = null;
		try {
			LOG.info(con.isClosed());
			LOG.info(" " + con.getMetaData().getURL());
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			writeIntoFile(rs, outFile);
		} catch (final Exception ex) {
			ex.printStackTrace();
			LOG.error(ex);
		} finally {

			ConnectUtils.tryToCloseJDBC(con, stmt, rs, true);

		}
	}

	static void writeIntoFile(ResultSet rs, String outFile) throws Exception {

		File exportFile = new File(outFile);
		if (exportFile.exists()) {
			exportFile.delete();
		}
		FileOutputStream fo = null;
		try {
			exportFile.createNewFile();
			fo = new FileOutputStream(exportFile);
			ResultSetMetaData rm = rs.getMetaData();
			StringBuffer sb = new StringBuffer();
			LOG.info(rm.getColumnCount());
			String colume = new String();
			for (int i = 1; i <= rm.getColumnCount(); i++) {

				if (i != rm.getColumnCount()) {
					colume += rm.getColumnName(i) + ",";
				} else {
					colume += rm.getColumnName(i);
				}

			}
			sb.append(colume + "\n");
			while (rs.next()) {
				String sb2 = new String();
				for (int i = 1; i <= rm.getColumnCount(); i++) {
					// System.out.println(rs.getString(i));
					if (i != rm.getColumnCount()) {
						sb2 += (rs.getString(i) + "|");
					} else {
						sb2 += (rs.getString(i));
					}
				}
				sb.append(sb2);
				sb.append("\n");
			}
			fo.write(sb.toString().getBytes());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fo != null) {
				fo.close();
			}
		}

	}

}
