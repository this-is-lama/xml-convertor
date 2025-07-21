package my.project.xmlconverter.dao;

import my.project.xmlconverter.entities.Department;
import my.project.xmlconverter.entities.DepartmentKey;
import my.project.xmlconverter.utils.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Access Object (DAO) для работы с сущностями Department в базе данных.
 * Обеспечивает основные CRUD-операции: сохранение, обновление, удаление и получение данных.
 * Реализован как Singleton.
 */
public class DepartmentDAO {

	private final Logger logger = LoggerFactory.getLogger(DepartmentDAO.class);
	private final static DepartmentDAO INSTANCE = new DepartmentDAO();

	private DepartmentDAO() {}

	/**
	 * Возвращает единственный экземпляр DepartmentDAO.
	 *
	 * @return экземпляр DepartmentDAO
	 */
	public static DepartmentDAO getInstance() {
		return INSTANCE;
	}

	/**
	 * Обновляет записи отделов в базе данных на основе переданного множества.
	 *
	 * @param departments  множество отделов для обновления (ключ - DepartmentKey, значение - Department)
	 * @param connection  соединение с базой данных
	 * @throws RuntimeException если произошла ошибка SQL
	 */
	public void updateAll(Map<DepartmentKey, Department> departments, Connection connection) {
		if (departments.isEmpty()) {
			return;
		}
		logger.info("Обновление всех сущностей");
		String sqlQuery = """
                UPDATE departments SET description = ? WHERE depcode = ? AND depjob = ?
                """;
		try (var statement = connection.prepareStatement(sqlQuery)) {
			for (var department : departments.entrySet()) {
				statement.setString(1, department.getValue().getDescription());
				statement.setString(2, department.getKey().getDepCode());
				statement.setString(3, department.getKey().getDepJob());
				statement.addBatch();
			}
			statement.executeBatch();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Удаляет записи отделов из базы данных на основе переданного множества.
	 *
	 * @param departments  множество отделов для удаления (ключ - DepartmentKey, значение - Department)
	 * @param connection  соединение с базой данных
	 * @throws RuntimeException если произошла ошибка SQL
	 */
	public void deleteAll(Map<DepartmentKey, Department> departments, Connection connection) {
		if (departments.isEmpty()) {
			return;
		}
		logger.info("Удаление ненужных сущностей");
		String sqlQuery = """
                DELETE FROM departments WHERE depcode = ? AND depjob = ?
                """;
		try (var statement = connection.prepareStatement(sqlQuery)) {
			for (var department : departments.entrySet()) {
				statement.setString(1, department.getKey().getDepCode());
				statement.setString(2, department.getKey().getDepJob());
				statement.addBatch();
			}
			statement.executeBatch();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Сохраняет новые записи отделов в базу данных.
	 *
	 * @param departments  множество новых отделов (ключ - DepartmentKey, значение - Department)
	 * @param connection  соединение с базой данных
	 * @throws RuntimeException если произошла ошибка SQL
	 */
	public void saveAll(Map<DepartmentKey, Department> departments, Connection connection) {
		if (departments.isEmpty()) {
			return;
		}
		logger.info("Сохранение новых сущностей!");
		String sqlQuery = """
                INSERT INTO departments (depcode, depjob, description) VALUES (?, ?, ?)
                """;
		try (var statement = connection.prepareStatement(sqlQuery)) {
			for (var department : departments.entrySet()) {
				statement.setString(1, department.getKey().getDepCode());
				statement.setString(2, department.getKey().getDepJob());
				statement.setString(3, department.getValue().getDescription());
				statement.addBatch();
			}
			statement.executeBatch();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Возвращает все отделы из базы данных в виде Map.
	 * Ключ - составной ключ DepartmentKey, значение - сущность Department.
	 *
	 * @return множество всех отделов
	 * @throws RuntimeException если произошла ошибка SQL
	 */
	public Map<DepartmentKey, Department> getAll() {
		logger.info("Получение всех сущностей");
		HashMap<DepartmentKey, Department> departments = new HashMap<>();
		String sqlQuery = """
                SELECT * FROM departments
                """;
		try (Connection connection = ConnectionManager.openConnection();
			 var statement = connection.prepareStatement(sqlQuery)) {
			var resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String depCode = resultSet.getString("DepCode");
				String depJob = resultSet.getString("DepJob");
				String description = resultSet.getString("Description");
				DepartmentKey key = new DepartmentKey(depCode, depJob);
				Department department = new Department(description);
				departments.put(key, department);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return departments;
	}
}