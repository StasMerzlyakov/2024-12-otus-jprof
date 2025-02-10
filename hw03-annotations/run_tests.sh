#!/bin/bash

../gradlew clean build

echo "ClassOk"
java -jar ./build/libs/gradleTestRunner-0.1.jar ru.otus.tester.testclasses.ClassOk

echo "AfterMethodException"
java -jar ./build/libs/gradleTestRunner-0.1.jar ru.otus.tester.testclasses.AfterMethodException

echo "BeforeMethodException"
java -jar ./build/libs/gradleTestRunner-0.1.jar ru.otus.tester.testclasses.BeforeMethodException

echo "TestMethodException"
java -jar ./build/libs/gradleTestRunner-0.1.jar ru.otus.tester.testclasses.TestMethodException
