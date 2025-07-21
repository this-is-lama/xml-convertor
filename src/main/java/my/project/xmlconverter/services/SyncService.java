package my.project.xmlconverter.services;

import my.project.xmlconverter.dao.DepartmentDAO;
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
 * Сервис для синхронизации данных между базой данных и XML-файлом.
 * Выполняет сравнение данных и применяет изменения (вставка, обновление, удаление)
 * в рамках транзакции.
 */
public class SyncService {

	private final DepartmentDAO dao = DepartmentDAO.getInstance();
	private static final Logger log = LoggerFactory.getLogger(SyncService.class);

	/**
	 * Синхронизирует данные между базой данных и XML-файлом.
	 * - Удаляет из БД записи, которых нет в XML.
	 * - Обновляет записи с изменённым описанием.
	 * - Добавляет новые записи из XML.
	 *
	 * @param filename  путь к XML-файлу для синхронизации
	 * @throws RuntimeException если произошла ошибка SQL или синхронизации
	 */
	public void sync(String filename) {
		log.info("Начало синхронизации с файлом {}", filename);
		Connection connection = ConnectionManager.openConnection();
		try {
			connection.setAutoCommit(false);
			Map<DepartmentKey, Department> dbDep = dao.getAll();
			Map<DepartmentKey, Department> xmlDep = ConvertService.convertXmlToDepartments(filename);

			Map<DepartmentKey, Department> depToInsert = new HashMap<>();
			Map<DepartmentKey, Department> depToUpdate = new HashMap<>();

			for (var department : xmlDep.entrySet()) {
				if (dbDep.containsKey(department.getKey())) {
					Department dbDepartment = dbDep.get(department.getKey());
					Department xmlDepartment = xmlDep.get(department.getKey());
					if (!dbDepartment.getDescription().equals(xmlDepartment.getDescription())) {
						depToUpdate.put(department.getKey(), xmlDepartment);
					}
					dbDep.remove(department.getKey());
				} else {
					depToInsert.put(department.getKey(), department.getValue());
				}
			}

			dao.deleteAll(dbDep, connection);
			dao.updateAll(depToUpdate, connection);
			dao.saveAll(depToInsert, connection);

			connection.commit();
			log.info("Транзакция успешно завершена!");
			System.out.println("Синхронизация успешно завершена!");
		} catch (SQLException e) {
			log.error("Ошибка при синхронизации: {}", e.getMessage());
			try {
				connection.rollback();
				log.info("Откат транзакции");
			} catch (SQLException ex) {
				log.error("Ошибка при откате транзакции: {}", ex.getMessage());
				throw new RuntimeException("Не удалось откатить транзакцию", ex);
			}
			throw new RuntimeException("Ошибка синхронизации", e);
		} finally {
			try {
				if (connection != null) {
					connection.close();
					log.debug("Соединение закрыто");
				}
			} catch (SQLException e) {
				log.error("Ошибка при закрытии соединения: {}", e.getMessage());
			}
		}
	}
}