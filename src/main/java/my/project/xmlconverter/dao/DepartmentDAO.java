package my.project.xmlconverter.dao;

import my.project.xmlconverter.entities.Department;
import my.project.xmlconverter.entities.DepartmentKey;
import my.project.xmlconverter.utils.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * DAO (Data Access Object) для работы с сущностями Department в базе данных.
 * Обеспечивает CRUD операции.
 */
public class DepartmentDAO {

	private final Logger logger = LoggerFactory.getLogger(DepartmentDAO.class);

	private final static DepartmentDAO INSTANCE = new DepartmentDAO();

	private DepartmentDAO() {}

	/**
	 * Возвращает единственный экземпляр DepartmentDAO (реализация Singleton)
	 * @return экземпляр DepartmentDAO
	 */
	public static DepartmentDAO getInstance() {
		return INSTANCE;
	}

	/**
	 * Обновляет информацию об отделах в базе данных
	 * @param departments множество отделов для обновления
	 * @param connection соединение с базой данных
	 */
	public void updateAll(Set<Department> departments, Connection connection) {
		if (departments.isEmpty()) {
			return;
		}
		logger.info("Обновление всех сущностей");
		String sqlQuery = """
                UPDATE departments SET description = ? WHERE depcode = ? AND depjob = ?
                """;
		try (var statement = connection.prepareStatement(sqlQuery)) {
			for (Department department : departments) {
				statement.setString(1, department.getDescription());
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
	 * Удаляет отделы из базы данных
	 * @param departments множество отделов для удаления
	 * @param connection соединение с базой данных
	 */
	public void deleteAll(Set<Department> departments, Connection connection) {
		if (departments.isEmpty()) {
			return;
		}
		logger.info("Удаление ненужных сущностей");
		int[] ids = new int[departments.size()];
		int i = 0;
		for (Department department : departments) {
			ids[i++] = department.getId();
		}
		String placeholders = String.join(",", Collections.nCopies(departments.size(), "?"));
		String sqlQuery = "DELETE FROM departments WHERE id IN (" + placeholders + ")";
		try (var statement = connection.prepareStatement(sqlQuery)) {
			for (int j = 0; j < ids.length; j++) {
				statement.setInt(j + 1, ids[j]);
			}
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Сохраняет новые отделы в базу данных
	 * @param departments множество новых отделов
	 * @param connection соединение с базой данных
	 */
	public void saveAll(Set<Department> departments, Connection connection) {
		if (departments.isEmpty()) {
			return;
		}
		logger.info("Сохранение новых сущностей!");
		String sqlQuery = """
                INSERT INTO departments (depcode, depjob, description) VALUES (?, ?, ?)
                """;
		try (var statement = connection.prepareStatement(sqlQuery)) {
			for (Department department : departments) {
				statement.setString(1, department.getKey().getDepCode());
				statement.setString(2, department.getKey().getDepJob());
				statement.setString(3, department.getDescription());
				statement.addBatch();
			}
			statement.executeBatch();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Получает все отделы из базы данных
	 * @return множество всех отделов
	 */
	public Set<Department> getAll() {
		logger.info("Получение всех сущностей");
		Set<Department> departments = new HashSet<>();
		String sqlQuery = """
                select * from departments
                """;
		try (Connection connection = ConnectionManager.openConnection();
			 var statement = connection.prepareStatement(sqlQuery)) {
			var resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Integer id = resultSet.getInt("id");
				String DepCode = resultSet.getString("DepCode");
				String DepName = resultSet.getString("DepJob");
				String Description = resultSet.getString("Description");
				DepartmentKey key = new DepartmentKey(DepCode, DepName);
				Department department = new Department(id, key, Description);
				departments.add(department);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return departments;
	}
}