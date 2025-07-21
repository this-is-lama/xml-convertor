package my.project.xmlconverter.entities;

import java.util.Objects;

/**
 * Сущность, представляющая отдел.
 * Содержит идентификатор, ключ отдела (DepartmentKey) и описание.
 */
public class Department {

	/**
	 * Уникальный идентификатор отдела
	 */
	private Integer id;

	/**
	 * Ключ отдела (сочетание depCode и depJob)
	 */
	private DepartmentKey key;

	/**
	 * Описание отдела
	 */
	private String Description;

	/**
	 * Конструктор отдела
	 * @param id идентификатор
	 * @param key ключ отдела
	 * @param description описание
	 */
	public Department(Integer id, DepartmentKey key, String description) {
		this.id = id;
		this.key = key;
		Description = description;
	}

	public Department(DepartmentKey key, String description) {
		this.key = key;
		Description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public DepartmentKey getKey() {
		return key;
	}

	public void setKey(DepartmentKey key) {
		this.key = key;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	/**
	 * Сравнивает отделы по ключу (depCode и depJob)
	 * @param object объект для сравнения
	 * @return true если отделы равны
	 */
	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (!(object instanceof Department that)) return false;
		return Objects.equals(key, that.key);
	}

	/**
	 * Возвращает хэш-код на основе ключа отдела
	 * @return хэш-код
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(key);
	}
}