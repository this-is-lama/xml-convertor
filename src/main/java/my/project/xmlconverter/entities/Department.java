package my.project.xmlconverter.entities;

/**
 * Сущность, представляющая отдел.
 * Содержит идентификатор, ключ отдела (DepartmentKey) и описание.
 */
public class Department {

	/**
	 * Описание отдела
	 */
	private String description;

	public Department(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}