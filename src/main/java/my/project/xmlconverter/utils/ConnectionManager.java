package my.project.xmlconverter.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Утилита для управления соединениями с базой данных.
 * Обеспечивает создание и настройку соединений.
 */
public final class ConnectionManager {

	private static final String DB_URL = "db.url";
	private static final String DB_USER = "db.username";
	private static final String DB_PASSWORD = "db.password";

	private ConnectionManager() {}

	/**
	 * Открывает новое соединение с базой данных
	 * @return объект Connection
	 */
	public static Connection openConnection() {
		try {
			return DriverManager.getConnection(
					PropertiesUtil.get(DB_URL),
					PropertiesUtil.get(DB_USER),
					PropertiesUtil.get(DB_PASSWORD));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
