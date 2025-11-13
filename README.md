## N-th Minimum element Finder

REST API для поиска N-го минимального элемента в Excel файле. 

## Особенности:
- Чтение файла формата .xlsx, содержащего целые числа.
- Поиск N-го минимального элемента с использованием алгоритма на основе кучи.
- Валидация данных входных параметров.
- Покрытие юнит- и интеграционными тестами основной бизнес-логики.

## Стек: 
Java 17, Spring Boot 3.x, Spring Web MVC, Apache POI, SpringDoc OpenAPI, Maven

## Запуск:
1. Склонировать репозиторий:
> git clone https://github.com/DariaB14/fileReader.git
>> cd fileReader
2. Запустить приложение:
> mvn spring-boot:run

Или: 

> mvn clean package 
>> java -jar target/fileReader-0.0.1-SNAPSHOT.jar

## Swagger документация доступна по адресу: 
> http://localhost:8080/swagger-ui.html

## Идеи по улучшению: 
- Реализовать загрузку файла вместо передачи пути. 
- Добавить обработку файлов с нечисловыми значениями. 
- Реализовать постраничное чтение для больших файлов. 
- Обрабатывать ошибки с помощью @ControllerAdvice, добавить логирование.
