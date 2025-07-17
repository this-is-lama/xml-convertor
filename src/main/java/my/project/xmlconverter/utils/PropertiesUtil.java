package my.project.xmlconverter.utils;

import java.io.IOException;
import java.util.Properties;

/**
 * Утилита для работы с файлом конфигурации application.properties.
 * Обеспечивает загрузку и доступ к параметрам конфигурации.
 */
public final class PropertiesUtil {

	private static final Properties PROPERTIES = new Properties();

	static {
		loadProperties();
	}

	private PropertiesUtil() {}

	/**
	 * Загружает параметры из файла application.properties
	 */
	private static void loadProperties() {
		var inputStream = PropertiesUtil.class.getClassLoader()
				.getResourceAsStream("application.properties");
		try {
			PROPERTIES.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Получает значение параметра по ключу
	 * @param key ключ параметра
	 * @return значение параметра
	 */
	public static String get(String key) {
		return PROPERTIES.getProperty(key);
	}

	/**
	 * Получает значение параметра по ключу со значением по умолчанию
	 * @param key ключ параметра
	 * @param defaultValue значение по умолчанию
	 * @return значение параметра или defaultValue если параметр не найден
	 */
	public static String get(String key, String defaultValue) {
		return PROPERTIES.getProperty(key, defaultValue);
	}
}
