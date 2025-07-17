# XML Converter Application

## Описание проекта

Это Java-приложение, которое предоставляет две основные функции:
1. Экспорт данных из таблицы базы данных в XML-файл
2. Синхронизация данных в таблице базы данных с содержимым XML-файла

## Требования к системе

Для работы приложения необходимо:
- Java JDK 21
- База данных PostgreSQL 
- Maven для сборки проекта

## Структура проекта

- `src/main/java/my/project/xmlconverter/`
    - `controllers/` - классы для обработки командной строки
    - `dao/` - классы для работы с базой данных
    - `entities/` - сущности предметной области
    - `services/` - бизнес-логика приложения
    - `utils/` - вспомогательные утилиты
- `src/main/resources/` - файлы конфигурации
- `pom.xml` - конфигурация Maven

## Технологии

- Java 21
- JDBC для работы с базой данных
- SLF4J + Logback для логирования
- DOM для работы с XML
- Maven для сборки проекта

## Установка и настройка

### 1. Настройка базы данных

1. Создайте таблицу с помощью следующего SQL-запроса:

```sql
CREATE TABLE departments (
    id SERIAL PRIMARY KEY,
    depCode VARCHAR(20) NOT NULL,
    depJob VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    UNIQUE (depCode, depJob)
);
```
2. Заполните таблицу данными
```sql
INSERT INTO departments (DepCode, DepJob, Description) VALUES
    ('IT', 'Developer', 'Разработка программного обеспечения'),
    ('IT', 'QA Engineer', 'Тестирование программного обеспечения'),
    ('IT', 'DevOps', 'Обслуживание инфраструктуры и CI/CD'); 
```
3. В архиве проекта есть готовый файл со скрипами init.sql для создания и наполнения базы данных

### 2. Настройка приложения

В файле application.properties настройте подключение к вашей базе данных PostgreSQL

```properties
db.url=jdbc:postgresql://localhost:5432/ваша_база_данных
db.username=ваше_имя_пользователя
db.password=ваш_пароль
```

Замените значения на ваши реальные данные для подключения к базе данных.

## Сборка и запуск

### Сборка проекта

Для сборки проекта выполните команду:

```bash
mvn clean package
```

Собранный JAR-файл будет находиться в директории `target`.

### Запуск приложения

Приложение запускается из командной строки с указанием команды и (опционально) имени файла:

```bash
java -jar xml-converter.jar <command> [filename]
```

Доступные команды:
- `export <filename.xml>` - экспорт данных из БД в XML-файл
- `sync <filename.xml>` - синхронизация БД с данными из XML-файла

Если имя файла не указано, по умолчанию используется `test.xml`.
Для команды синхронизации указание файла обязательно

Примеры:
```bash
# Экспорт данных в файл data.xml
java -jar xml-converter.jar export data.xml

# Синхронизация с файлом updates.xml
java -jar xml-converter.jar sync updates.xml
```
### Запуск приложения с помощью скриптов .bat и .sh

Внутри архива есть два файла run.bat и run.sh

Для их запуска:

1. Откройте командную консоль
2. Перейдите в директорию распакованного архива
 ```bash
   cd "Путь до директории\xml-converter"
   ```
3. Запустите скрипт командой
 ```bash
run.bat export output.xml
```
или 
 ```bash
run.bat sync input.xml
```

## Использование

### Экспорт данных (export)

1. Запустите приложение с командой `export` и укажите имя файла для сохранения:
   ```bash
   run.bat export output.xml
   ```
2. Приложение создаст XML-файл со всеми записями из таблицы `departments` (кроме поля ID).

Формат XML-файла:
```xml
<departments>
  <department>
    <depCode>CODE1</depCode>
    <depJob>JOB1</depJob>
    <description>Description 1</description>
  </department>
  <department>
    <depCode>CODE2</depCode>
    <depJob>JOB2</depJob>
    <description>Description 2</description>
  </department>
</departments>
```

### Синхронизация данных (sync)

1. Подготовьте XML-файл в указанном выше формате
2. Запустите приложение с командой `sync` и укажите имя файла:
   ```bash
   run.bat sync updates.xml
   ```
3. Приложение выполнит синхронизацию данных:
    - Удалит записи, которые есть в БД, но отсутствуют в XML
    - Добавит новые записи из XML
    - Обновит существующие записи, если их описание изменилось

Все операции выполняются в одной транзакции. В случае ошибки изменения не будут применены.





