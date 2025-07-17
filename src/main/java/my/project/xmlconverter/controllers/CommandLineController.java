package my.project.xmlconverter.controllers;

import my.project.xmlconverter.services.ExportService;
import my.project.xmlconverter.services.SyncService;
import my.project.xmlconverter.utils.LoggerConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Контроллер командной строки для управления экспортом и синхронизацией данных.
 * Обрабатывает аргументы командной строки и запускает соответствующие сервисы.
 */
public class CommandLineController {

	/**
	 * Логгер для записи событий
	 */
	private static final Logger logger = LoggerFactory.getLogger(CommandLineController.class);

	/**
	 * Сообщение со справкой по использованию программы
	 */
	private static final String HELP_MESSAGE = """
            Использование: <команда> [имя файла]
            Команды:
            export <имя файла.xml> - экспорт БД в XML
            sync <имя файла.xml> - синхронизация БД с XML
            """;

	static {
		LoggerConfigurator.configure();
	}

	/**
	 * Главный метод приложения, обрабатывающий аргументы командной строки
	 * @param args аргументы командной строки
	 */
	public static void main(String[] args) {
		ExportService exportService = new ExportService();
		SyncService syncService = new SyncService();
		if (args.length >= 1 && args.length <= 2) {
			String command = args[0];
			String fileName = "test.xml";
			logger.info("Старт приложения с командой {}", command);
			switch (command) {
				case "export" -> {
					if (args.length == 2) {
						fileName = args[1];
					}
					exportService.export(fileName);
				}
				case "sync" -> {
					if (args.length == 2) {
						fileName = args[1];
						syncService.sync(fileName);
					} else {
						System.out.println(HELP_MESSAGE);
					}
				}
				default -> System.out.println(HELP_MESSAGE);
			}
		} else {
			System.out.println(HELP_MESSAGE);
		}
	}
}