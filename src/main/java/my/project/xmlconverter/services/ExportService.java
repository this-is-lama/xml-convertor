package my.project.xmlconverter.services;


import my.project.xmlconverter.dao.DepartmentDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Сервис для экспорта данных из базы данных в XML файл.
 */
public class ExportService  {

	private final static DepartmentDAO dao = DepartmentDAO.getInstance();
	private static final Logger log = LoggerFactory.getLogger(ExportService.class);

	/**
	 * Экспортирует данные из базы данных в XML файл
	 * @param fileName имя файла для экспорта
	 */
	public void export(String fileName) {
		log.info("Начало экспорта данных в файл {}", fileName);
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			var doc = Converter.convertDepartmentToXml(dao.getAll());
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(fileName));
			transformer.transform(source, result);
			log.info("Экспорт успешно завершен, файл сохранен: {}", fileName);
			System.out.println("XML файл сохранен в " + fileName);
		} catch (TransformerException e) {
			log.error("Ошибка трансформации XML: {}", e.getMessage());
			throw new RuntimeException("Не удалось экспортировать данные в XML", e);
		}
	}

}
