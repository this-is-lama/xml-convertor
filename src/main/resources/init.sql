CREATE TABLE departments (
   ID SERIAL PRIMARY KEY,
   DepCode VARCHAR(20) NOT NULL,
   DepJob VARCHAR(100) NOT NULL,
   Description VARCHAR(255),
   CONSTRAINT natural_key UNIQUE (DepCode, DepJob)
);

INSERT INTO departments (DepCode, DepJob, Description) VALUES
   ('IT', 'Developer', 'Разработка программного обеспечения'),
   ('IT', 'QA Engineer', 'Тестирование программного обеспечения'),
   ('IT', 'DevOps', 'Обслуживание инфраструктуры и CI/CD'),
   ('HR', 'Recruiter', 'Подбор персонала'),
   ('HR', 'HR Manager', 'Управление персоналом и кадровые вопросы'),
   ('Finance', 'Accountant', 'Бухгалтерский учет и отчетность'),
   ('Finance', 'Financial Analyst', 'Финансовый анализ и планирование'),
   ('Sales', 'Sales Manager', 'Продажи и работа с клиентами'),
   ('Sales', 'Sales Director', 'Управление отделом продаж'),
   ('Marketing', 'SEO Specialist', 'Поисковая оптимизация и продвижение'),
   ('Marketing', 'Content Manager', 'Создание и управление контентом'),
   ('Support', 'Technical Support', 'Техническая поддержка пользователей'),
   ('Support', 'Customer Support', 'Обслуживание клиентов и решение проблем');

