package yexport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import db.connections.VexportDbFactory;
import db.utils.ConnectUtils;
import de.simplicit.vjdbc.VirtualConnection;

public class yExport {
	private static final Logger LOG = Logger.getLogger(yExport.class);

	public static void main(String args[]) throws Exception {

		VirtualConnection vjdbcCon = VexportDbFactory.getDefaultVjdbConnect();
		// System.out.println(" " + vjdbcCon.getMetaData().getURL());
		defaultQuery(vjdbcCon, true);
	}

	protected static void verifyUnderlyingConnection(Connection vjdbcCon) throws SQLException {
		System.out.println(vjdbcCon.isClosed());

	}

	static void defaultQuery(final VirtualConnection con, final boolean closeConnection) {
		ResultSet rs = null;
		Statement stmt = null;

		try {
			System.out.println(con.isClosed()); 
			stmt = con.createStatement();
//			rs = stmt.executeQuery("select {bp.code} as code ,{bp.saleunit[zh]} as saltunit,{vp.code} as vpcode, {vp.saleunit[zh]} as vsaleunit,{m3c.code} as  m3code,{m3c.name[zh]},{m2c.code} ,{m2c.name[zh]},{m1c.code} ,{m1c.name[zh]}  from {gpproduct as vp left join gpproduct as bp on {vp.baseproduct}={bp.pk} and {bp.masterproduct}=1  left join CategoryProductRelation as cpr on {cpr.target} = {vp.pk} join Category as m3c   on {m3c.pk} = {cpr.source} join CategoryCategoryRelation as ccr2 on {ccr2.target} = {m3c.pk}\r\n"
//							+ "join CategoryCategoryRelation as ccr1 on {ccr2.source} = {ccr1.target} join category as m2c on {m2c.pk} = {ccr2.source} join category as m1c on {ccr1.source} = {m1c.pk} and {m3c.code} like 'mc_3%'  } where {vp.code} like 'JJWP%' or {vp.code} like 'JJSP%' ");

			rs = stmt.executeQuery("select {ve.originalCode} as ocode,{ve.code} as vcode,{code},{name[zh]},{taxClassCode},{taxClassName} from {gpproduct as gp left join ProductVendorRelation as pvr on {pvr.source} ={gp.pk}   left join vendor as ve on {pvr.target}={ve.pk} } where {ve.originalCode} in ('CO10000915','CO10001229','CO10001800','CO10000469','CO10000670','CO10001047','CO10001231','CO10000357','CO10001212','CO10001242','CO10001211','CO10000474','CO10000993','CO10002104','CO10000911','CO10000689','CO10001237','CO10000831','CO10000730','CO10001961','CO10002881','CO10000685','CO10001163','CO10000485','CO10000716','CO10001023','CO10002520','CO10001244','CO10000855','CO10001112','CO10000537','CO10001120','CO10001239','CO10001215','CO10002180','CO10000869','CO10000475','CO10000338','CO10001213','CO10000658','CO10000930','CO10000657','CO10001408','CO10003629','CO10000471','CO10000391','CO10000966')");
			// System.out.println(rs.next() + "" + rs.getString("code"));
			writeIntoFile(rs);
		} catch (final Exception ex) {
			ex.printStackTrace();
			LOG.error(ex);
		} finally {
			if (closeConnection) {
				ConnectUtils.tryToCloseJDBC(con, stmt, rs, true);
			}
		}
	}

	static void writeIntoFile(ResultSet rs) throws Exception {

		File exportFile = new File("D://mydata3.txt");
		if (exportFile.exists()) {
			exportFile.delete();
		}
		FileOutputStream fo = null;
		try {
			exportFile.createNewFile();
			fo = new FileOutputStream(exportFile);
			ResultSetMetaData rm = rs.getMetaData();
			StringBuffer sb = new StringBuffer();
			//System.out.println(rm.getColumnCount());
//			String colume = new String();
//			for (int i = 1; i <= rm.getColumnCount(); i++) {
//
//				if (i != rm.getColumnCount()) {
//					colume += rm.getColumnName(i) + ",";
//				} else {
//					colume += rm.getColumnName(i);
//				}
//				
//			}
//			/sb.append(colume+"\n");
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
