package my.project.xmlconverter.entities;

import java.util.Objects;

/**
 * Составной ключ для сущности Department, состоящий из кода отдела (depCode) и должности (depJob).
 * Используется для однозначной идентификации отделов в базе данных.
 */
public class DepartmentKey {

	/**
	 * Код отдела.
	 */
	private String DepCode;

	/**
	 * Должность в отделе.
	 */
	private String DepJob;

	/**
	 * Конструктор для создания составного ключа отдела.
	 *
	 * @param depCode код отдела (не может быть null)
	 * @param depJob  должность в отделе (не может быть null)
	 */
	public DepartmentKey(String depCode, String depJob) {
		DepCode = depCode;
		DepJob = depJob;
	}

	/**
	 * Возвращает код отдела.
	 *
	 * @return код отдела
	 */
	public String getDepCode() {
		return DepCode;
	}

	/**
	 * Устанавливает код отдела.
	 *
	 * @param depCode код отдела
	 */
	public void setDepCode(String depCode) {
		DepCode = depCode;
	}

	/**
	 * Возвращает должность в отделе.
	 *
	 * @return должность
	 */
	public String getDepJob() {
		return DepJob;
	}

	/**
	 * Устанавливает должность в отделе.
	 *
	 * @param depJob должность
	 */
	public void setDepJob(String depJob) {
		DepJob = depJob;
	}

	/**
	 * Сравнивает текущий ключ с другим объектом на равенство.
	 * Два ключа считаются равными, если их depCode и depJob совпадают.
	 *
	 * @param object объект для сравнения
	 * @return true, если ключи равны, иначе false
	 */
	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (!(object instanceof DepartmentKey key)) return false;
		return Objects.equals(DepCode, key.DepCode) && Objects.equals(DepJob, key.DepJob);
	}

	/**
	 * Возвращает хэш-код ключа на основе полей depCode и depJob.
	 *
	 * @return хэш-код
	 */
	@Override
	public int hashCode() {
		return Objects.hash(DepCode, DepJob);
	}
}