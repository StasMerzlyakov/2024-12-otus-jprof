# hw11-cache

Реализован MyCache на основе WeakHashMap.

- ru.otus.cache.CacheServiveDB и ru.otus.cache.MananagerServiveDB - cached-proxy к 
DBServiceClient и DBServiceMananager соответственно;

Для работы:
- запустить контейнер через скрипт hw11-cache/docker/runDb.sh
- запустить программу HomeWork
- в weak_map_result.png скрин вывода в лог (время получения данных из БД и из кэша)

~ 4.2 mls при получении из БД vs 0.3 mls при работе с кэшем

Проблемы:
- почему-то при запуске fatJar не подтягиваются миграции flyway
(предполагаю что проблема тянется из предыдущих занятий) 
