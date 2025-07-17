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
import java.util.HashSet;
import java.util.Set;

/**
 * Утилита для преобразования между XML и объектами Department.
 * Обеспечивает двустороннюю конвертацию данных.
 */
public class Converter {

	private static final Logger log = LoggerFactory.getLogger(Converter.class);

	/**
	 * Преобразует множество отделов в XML документ
	 * @param departments множество отделов
	 * @return XML документ
	 */
	public static Document convertDepartmentToXml(Set<Department> departments) {
		log.info("Конвертация сущности отдела в XML дерево");
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		Document doc;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();

			Element rootElement = doc.createElement("departments");

			for (Department department : departments) {
				//корневой
				Element departmentElement = doc.createElement("department");
				//поля
				Element depCodeElement = doc.createElement("depCode");
				depCodeElement.appendChild(doc.createTextNode(department.getKey().getDepCode()));
				departmentElement.appendChild(depCodeElement);

				Element depJobElement = doc.createElement("depJob");
				depJobElement.appendChild(doc.createTextNode(department.getKey().getDepJob()));
				departmentElement.appendChild(depJobElement);

				Element descriptionElement = doc.createElement("description");
				descriptionElement.appendChild(doc.createTextNode(department.getDescription()));
				departmentElement.appendChild(descriptionElement);
				//добавляем корень
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
	 * Преобразует XML файл в множество отделов
	 * @param filename имя XML файла
	 * @return множество отделов
	 */
	public static Set<Department> convertXmlToDepartments(String filename) {
		log.info("Создание объектов из XML дерева");
		Set<Department> departments = new HashSet<>();
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
					Department department = new Department(null, key, description);
					if (!departments.contains(department)) {
						departments.add(department);
					} else {
						log.error("Обнаружен дубликат департамента: {} {}", depCode, depJob);
						throw new RuntimeException("Обнаружен дубликат департамента: " + depCode + " " + depJob);
					}
				}
			}
			log.info("Объекты успешно созданы!");
		} catch (IOException | ParserConfigurationException | SAXException e) {
			log.error("Oшибка чтения из файла!");
		}
		return departments;
	}
}
