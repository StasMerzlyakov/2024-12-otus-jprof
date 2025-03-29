#!/bin/bash

../../gradlew clean build

java  -Xms128m -Xmx128m -jar ./build/libs/cache-0.1.jar  ru.otus.HomeWork
