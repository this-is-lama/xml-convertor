package my.project.xmlconverter.entities;

/**
 * Сущность, представляющая отдел в организации.
 * Содержит описание отдела и связана с составным ключом DepartmentKey.
 */
public class Department {

	/**
	 * Идентификатор отдела.
	 */
	private int id;

	/**
	 * Описание отдела.
	 */
	private String description;

	/**
	 * Конструктор для создания отдела с заданным описанием.
	 *
	 * @param description описание отдела (не может быть null)
	 */
	public Department(String description) {
		this.description = description;
	}

	/**
	 * Возвращает идентификатор отдела.
	 *
	 * @return описание
	 */
	public int getId() {
		return id;
	}

	/**
	 * Устанавливает идентификатор отдела.
	 *
	 * @param id идентификатор
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Возвращает описание отдела.
	 *
	 * @return описание
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Устанавливает описание отдела.
	 *
	 * @param description описание
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}