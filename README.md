# job4j_microservices

Тестовое задание: два Spring Boot микросервиса, связанных через Apache Kafka.

## Описание

- **service-r** — REST-сервис, принимает запросы от пользователя и публикует сообщения в Kafka
- **service-s** — REST-сервис, потребляет сообщения из Kafka, сохраняет данные в PostgreSQL и файлы в MinIO

```
User → service-r (REST) → Kafka → service-s (REST) → PostgreSQL
                                                     → MinIO
```

## Стек

| Компонент   | Версия / Примечание        |
|-------------|----------------------------|
| Java        | 21                         |
| Spring Boot | 3                          |
| Maven       | сборка                     |
| Kafka       | KRaft (без Zookeeper)      |
| PostgreSQL  | хранение данных            |
| MinIO       | хранение файлов            |
| Docker      | контейнеризация            |

## Запуск

```bash
docker-compose up
```
