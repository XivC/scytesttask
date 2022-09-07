# Тестовое задание в Scytec Games

## Установка и запуск
>mvn clean package
> java -jar target/scytesttask-1.0-jar-with-dependencies.jar

Сервер разворачивается на 80-м порте.
Документация к api прилагается в файле **api.yaml**

## Краткое описание
Задание сделано в соответствии с описанием на testtask.scytecgames.com

Т.к потенциальных источников и владельцев золота может быть много: кланы, игроки, персонажи, счета и т.д
было принято решение выделить сущность "платёжный аккаунт" и работать с ней. Целостность транзакций поддерживается валидаторами.


## Что можно улучшить

1) Логирование - тз не предполагает использования высокоуровненвых бибилиотек, но тем не менее не помешало бы добавить агрегацию логов для лучшего поиска ошибок
2) Больше DI. DI реализован на уровне сервисов. Для облегчения инъекции зависимостей можно разработать небольшой DI - механизм поверх обработчика запросов, чтобы, например, подставлять новый экземпляр репозитории для каждого запроса.
3) ORM - разработать более полноценный механизм каста строк таблицы в объекты, чтобы избавиться от ручного проставления индексов и упростить работу в сервисах (убрать лишние getById и хранить вложенные объекты вместо их идентификаторов)




