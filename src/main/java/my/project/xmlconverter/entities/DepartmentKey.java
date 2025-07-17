package my.project.xmlconverter.entities;

import java.util.Objects;

/**
 * Ключ департамента, состоящий из кода департамента (depCode) и должности (depJob).
 * Используется для идентификации департаментов.
 */
public class DepartmentKey {

	/**
	 * Код отдела
	 */
	private String DepCode;

	/**
	 * Должность в отделе
	 */
	private String DepJob;

	/**
	 * Конструктор ключа отдела
	 * @param depCode код отдела
	 * @param depJob должность
	 */
	public DepartmentKey(String depCode, String depJob) {
		DepCode = depCode;
		DepJob = depJob;
	}

	public String getDepCode() {
		return DepCode;
	}

	public void setDepCode(String depCode) {
		DepCode = depCode;
	}

	public String getDepJob() {
		return DepJob;
	}

	public void setDepJob(String depJob) {
		DepJob = depJob;
	}

	/**
	 * Сравнивает ключи отделов по depCode и depJob
	 * @param object объект для сравнения
	 * @return true если ключи равны
	 */
	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (!(object instanceof DepartmentKey key)) return false;
		return Objects.equals(DepCode, key.DepCode) && Objects.equals(DepJob, key.DepJob);
	}

	/**
	 * Возвращает хэш-код на основе depCode и depJob
	 * @return хэш-код
	 */
	@Override
	public int hashCode() {
		return Objects.hash(DepCode, DepJob);
	}
}