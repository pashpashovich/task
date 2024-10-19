# Задание
Backend Restful API поиска товаров с помощью поискового движка ElasticSearch
## Содержание 
- [Технологии](#технологии)
- [Инструкция по запуску](#инструкция-по-запуску)
- [API](#API)
## Технологии
- Java 21
- Gradle 8.5
- Spring Boot 
- ElasticSearch
- PostgreSQL
- Docker
- Lombok
## Инструкция по запуску
1. Скачайте или склонируйте репозиторий.
2. Запустите Docker и введите следующую команду (включите VPN во избегания проблем со скачиванием контейнера ElasticSearch):
```
docker-compose up -d 
```
3. Запустите проект.
4. Для включения фильтров (допзадание) перейдите в файл application.properties в корне проекта и поставьте значение filter.enabled на true. Также можно поменять цвет ску и доступность товара:
```
filter.enabled=false
filter.color=Red
filter.available=true
```
5. Можете выполнять запросы, которые определены в разделе API,с помощью Postman или другого инструмента тестирования API.
## API
- GET http://localhost:8080/api/load/active
Загрузка в индекс ElasticSearch товаров, которые в продаже.
Пример ответа:
```
Active products loaded to Elasticsearch
```
- GET http://localhost:8080/api/load/afterStartDate?startDate=2024-10-15
  Загрузка в индекс ElasticSearch товаров, которые поступили после startDate (формат даты: yyyy-MM-dd).

Пример ответа:
```
Products after 2024-10-15 loaded to Elasticsearch
```
- GET http://localhost:8080/api/search/products?query=Sprite
  Поиск товара по запросу (в названии и в описании).

Пример ответа (выключенный фильтр):
```
[
    {
        "id": 2,
        "name": "Sprite",
        "description": "Lime-flavoured soft drink",
        "inSale": true,
        "quantity": 20,
        "dateOfCreation": "2024-10-15T00:09:03.000+00:00",
        "skus": [
            {
                "id": 4,
                "color": "Red",
                "available": true
            },
            {
                "id": 5,
                "color": "Black",
                "available": false
            }
        ]
    }
]
```
Пример ответа (включенный фильтр):
```
[
    {
        "id": 2,
        "name": "Sprite",
        "description": "Lime-flavoured soft drink",
        "inSale": true,
        "quantity": 20,
        "dateOfCreation": "2024-10-15T00:09:03.000+00:00",
        "skus": [
            {
                "id": 4,
                "color": "Red",
                "available": true
            }
        ]
    }
]
```
- GET http://localhost:8080/api/products

Пример ответа:
```
[
    {
        "id": 20,
        "name": "Schweppes",
        "description": "Tonic water with a hint of lemon",
        "inSale": true,
        "quantity": 11,
        "dateOfCreation": "2024-10-15T21:00:00.000+00:00",
        "skus": [
            {
                "id": 57,
                "color": "Copper",
                "available": true
            },
            {
                "id": 58,
                "color": "Zinc",
                "available": true
            },
            {
                "id": 59,
                "color": "Nickel",
                "available": false
            }
        ]
    },
    ....
    ....
```
- GET http://localhost:8080/api/products/2

Пример ответа:
```
{
    "id": 2,
    "name": "Sprite",
    "description": "Lime-flavoured soft drink",
    "inSale": true,
    "quantity": 20,
    "dateOfCreation": "2024-10-15T00:09:03.000+00:00",
    "skus": [
        {
            "id": 4,
            "color": "Red",
            "available": true
        },
        {
            "id": 5,
            "color": "Black",
            "available": false
        }
    ]
}
```
- POST http://localhost:8080/api/products

Пример тела запроса:
```
{
    "name": "New Product",
    "description": "New description",
    "inSale": true,
    "quantity": 50,
    "dateOfCreation": "2023-10-05T00:00:00.000+00:00"
}
```
Пример ответа:
```
{
    "id": 42,
    "name": "New Product",
    "description": "New description",
    "inSale": true,
    "quantity": 50,
    "dateOfCreation": "2023-10-05T00:00:00.000+00:00",
    "skus": null
}
```
- PUT http://localhost:8080/api/products/42

Пример тела запроса:
```
{
    "name": "Updated Product",
    "description": "Updated description",
    "inSale": false,
    "quantity": 30,
    "dateOfCreation": "2023-09-30T00:00:00.000+00:00"
}
```
Пример ответа:
```
{
    "id": 42,
    "name": "Updated Product",
    "description": "Updated description",
    "inSale": false,
    "quantity": 30,
    "dateOfCreation": "2023-09-30T00:00:00.000+00:00",
    "skus": []
}
```
- DELETE http://localhost:8080/api/products/42

Пример ответа:
```
Product deleted
```
- GET http://localhost:8080/api/skus

Пример ответа:
```
[
    {
        "id": 4,
        "color": "Red",
        "available": true
    },
    {
        "id": 5,
        "color": "Black",
        "available": false
    },
    {
        "id": 6,
        "color": "White",
        "available": true
    },
    {
        "id": 7,
        "color": "Purple",
        "available": true
    },
    ....
    ....
```
- GET http://localhost:8080/api/skus/22

Пример ответа:
```
{
    "id": 22,
    "color": "Silver",
    "available": true
}
```
- POST http://localhost:8080/api/skus/product/2

Пример тела запроса:
```
{
    "color": "Green",
    "available": true
}
```
Пример ответа:
```
{
    "id": 61,
    "color": "Green",
    "available": true
}
```
- PUT http://localhost:8080/api/skus/20

Пример тела запроса:
```
{
    "color": "Updated Color",
    "available": false
}
```
Пример ответа:
```
{
    "id": 20,
    "color": "Updated Color",
    "available": false
}
```
- DELETE http://localhost:8080/api/skus/1

Пример ответа:
```
Sku deleted
```