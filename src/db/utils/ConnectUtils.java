package db.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectUtils {

	public static final void tryToCloseJDBC(Connection connection, Statement statement, ResultSet resultSet,
			boolean ignoreErrors) {
		SQLException ex = null;

		try {
			if (resultSet != null) {
				resultSet.close();
			}
		} catch (SQLException var8) {
			ex = var8;
		}

		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException var7) {
			ex = var7;
		}

		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException var6) {
			ex = var6;
		}

		if (ex != null && !ignoreErrors) {
			throw new RuntimeException(ex);
		}
	}
}
