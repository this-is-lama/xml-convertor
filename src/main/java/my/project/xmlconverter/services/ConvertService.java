package my.project.xmlconverter.services;

import my.project.xmlconverter.entities.Department;
import my.project.xmlconverter.entities.DepartmentKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Сервис для преобразования между XML и объектами Department.
 * Обеспечивает двустороннюю конвертацию:
 * - из Map<DepartmentKey, Department> в XML-документ;
 * - из XML-файла в Map<DepartmentKey, Department>.
 */
public class ConvertService {

	private static final Logger log = LoggerFactory.getLogger(ConvertService.class);

	/**
	 * Преобразует множество отделов в XML-документ.
	 *
	 * @param departments  множество отделов (ключ - DepartmentKey, значение - Department)
	 * @return XML-документ, содержащий данные об отделах
	 * @throws RuntimeException если не удалось создать XML-документ
	 */
	public static Document convertDepartmentToXml(Map<DepartmentKey, Department> departments) {
		log.info("Конвертация сущности отдела в XML дерево");
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		Document doc;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();

			Element rootElement = doc.createElement("departments");

			for (var department : departments.entrySet()) {
				Element departmentElement = doc.createElement("department");

				Element depCodeElement = doc.createElement("depCode");
				depCodeElement.appendChild(doc.createTextNode(department.getKey().getDepCode()));
				departmentElement.appendChild(depCodeElement);

				Element depJobElement = doc.createElement("depJob");
				depJobElement.appendChild(doc.createTextNode(department.getKey().getDepJob()));
				departmentElement.appendChild(depJobElement);

				Element descriptionElement = doc.createElement("description");
				descriptionElement.appendChild(doc.createTextNode(department.getValue().getDescription()));
				departmentElement.appendChild(descriptionElement);

				rootElement.appendChild(departmentElement);
			}

			doc.appendChild(rootElement);
			log.info("Успешно создан XML документ");
		} catch (ParserConfigurationException e) {
			log.error("Ошибка построения XML дерева: {}", e.getMessage());
			throw new RuntimeException("Не удалось создать XML документ", e);
		}
		return doc;
	}

	/**
	 * Преобразует XML-файл в множество отделов.
	 *
	 * @param filename  путь к XML-файлу
	 * @return множество отделов (ключ - DepartmentKey, значение - Department)
	 * @throws RuntimeException если файл не существует или содержит дубликаты отделов
	 */
	public static Map<DepartmentKey, Department> convertXmlToDepartments(String filename) {
		log.info("Создание объектов из XML дерева");
		Map<DepartmentKey, Department> departments = new HashMap<>();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			File inputFile = new File(filename);
			if (!inputFile.exists()) {
				log.error("Файл {} не существует", filename);
				throw new RuntimeException("Файл не существует: " + filename);
			}

			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("department");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String depCode = eElement.getElementsByTagName("depCode").item(0).getTextContent();
					String depJob = eElement.getElementsByTagName("depJob").item(0).getTextContent();
					String description = eElement.getElementsByTagName("description").item(0).getTextContent();
					DepartmentKey key = new DepartmentKey(depCode, depJob);
					Department department = new Department(description);
					if (!departments.containsKey(key)) {
						departments.put(key, department);
					} else {
						log.error("Обнаружен дубликат департамента: {} {}", depCode, depJob);
						throw new RuntimeException("Обнаружен дубликат департамента: " + depCode + " " + depJob);
					}
				}
			}
			log.info("Объекты успешно созданы!");
		} catch (IOException | ParserConfigurationException | SAXException e) {
			log.error("Ошибка чтения из файла!");
		}
		return departments;
	}
}