# job4j_microservices

Тестовое задание: два Spring Boot микросервиса, связанных через Apache Kafka, с хранением данных в PostgreSQL и файлов в MinIO.

## Описание

Пользователь отправляет GET-запрос на **service-r**. Сервис публикует запрос в Kafka, **service-s** читает студента из PostgreSQL и фото из MinIO, формирует XML-ответ и отправляет обратно через Kafka. **service-r** конвертирует XML в JSON и возвращает пользователю.

```
User → service-r (REST)
         ↓ Kafka (student-request, JSON)
       service-s → PostgreSQL (данные студента)
                 → MinIO (фото студента)
         ↓ Kafka (student-reply, XML)
       service-r → User (JSON)
```

## Архитектура (5 контейнеров)

| Контейнер   | Роль                                              | Порт  |
|-------------|---------------------------------------------------|-------|
| `service-r` | REST API для пользователя, Kafka producer/consumer | 8080  |
| `kafka`     | Apache Kafka в режиме KRaft (без Zookeeper)       | 9092  |
| `service-s` | Kafka consumer/producer, PostgreSQL, MinIO        | 8081  |
| `postgres`  | База данных с таблицей студентов                  | 5432  |
| `minio`     | S3-хранилище фотографий студентов                 | 9000  |

## Технологии

| Компонент   | Версия / Примечание           |
|-------------|-------------------------------|
| Java        | 21                            |
| Spring Boot | 3                             |
| Maven       | сборка                        |
| Kafka       | KRaft (без Zookeeper)         |
| PostgreSQL  | хранение данных студентов     |
| MinIO       | хранение фотографий (S3-совместимое) |
| Docker      | контейнеризация               |

## Запуск

```bash
docker-compose up --build
```

> **Примечание:** если на хост-машине запущен локальный PostgreSQL на порту 5432, он может конфликтовать с контейнером.
> Остановите его перед запуском: `sudo systemctl stop postgresql`

## Примеры запросов

Запрос существующего студента:

```bash
curl 'http://localhost:8080/api/students/ЗК-2021-001'
```

Ожидаемый ответ (200 OK):

```json
{
  "requestId": "...",
  "studentId": "ЗК-2021-001",
  "student": {
    "studentId": "ЗК-2021-001",
    "firstName": "Алексей",
    "lastName": "Иванов",
    "middleName": "Сергеевич",
    "faculty": "Факультет информационных технологий",
    "photoUrl": "photos/ivanov.jpg"
  },
  "photo": "...",
  "success": true
}
```

Запрос несуществующего студента:

```bash
curl 'http://localhost:8080/api/students/FAKEID'
```

Ожидаемый ответ (404 Not Found):

```json
{
  "error": "Student not found: FAKEID"
}
```
