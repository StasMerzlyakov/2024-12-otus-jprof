#!/bin/bash

../../gradlew clean build

# тут проблемы с миграциями
java -jar ./build/libs/cache-0.1.jar  ru.otus.HomeWork
