#!/bin/bash

../gradlew clean build

JAVA_OPTS="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs/heapdump.hprof -XX:+UseG1GC"

mkdir -p logs

echo "Original"
mkdir -p orig
for i in `seq 1 16`;do
    heapSize=$(( $i * 256 ))
    heapOpt="-Xms${heapSize}m -Xmx${heapSize}m"
    gcLogOpt="-Xlog:gc=debug:file=./logs/gc-CalcDemo_${heapSize}-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m"
    fileName="orig/CalcDemo_${heapSize}.log"
    java $JAVA_OPTS $heapOpt $gcLogOpt -cp build/libs/testGc-0.1-all.jar ru.calculator.CalcDemo &> $fileName
    time=`cat $fileName |grep sec | awk '{ print $8}' | sed -e 's/sec://g'`
    echo "$heapSize	$time"
done

dir="opt1"
mkdir -p ${dir}
name="CalcDemoOpt1"
echo $name
for i in `seq 1 16`;do
    heapSize=$(( $i * 256 ))
    heapOpt="-Xms${heapSize}m -Xmx${heapSize}m"
    gcLogOpt="-Xlog:gc=debug:file=./logs/gc-${name}_${heapSize}-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m"
    fileName="${dir}/${name}_${heapSize}.log"
    java $JAVA_OPTS $heapOpt $gcLogOpt -cp build/libs/testGc-0.1-all.jar ru.${dir}.CalcDemo &> $fileName
    time=`cat $fileName |grep sec | awk '{ print $8}' | sed -e 's/sec://g'`
    echo "$heapSize	$time"
done

dir="opt2"
mkdir -p $dir
name="CalcDemoOpt2"
echo $name
for i in `seq 1 16`;do
    heapSize=$(( $i * 256 ))
    heapOpt="-Xms${heapSize}m -Xmx${heapSize}m"
    gcLogOpt="-Xlog:gc=debug:file=./logs/gc-${name}_${heapSize}-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m"
    fileName="${dir}/${name}_${heapSize}.log"
    java $JAVA_OPTS $heapOpt $gcLogOpt -cp build/libs/testGc-0.1-all.jar ru.${dir}.CalcDemo &> $fileName
    time=`cat $fileName |grep sec | awk '{ print $8}' | sed -e 's/sec://g'`
    echo "$heapSize	$time"
done

dir="opt3"
mkdir -p $dir
name="CalcDemoOpt3"
echo $name
for i in `seq 1 16`;do
    heapSize=$(( $i * 256 ))
    heapOpt="-Xms${heapSize}m -Xmx${heapSize}m"
    gcLogOpt="-Xlog:gc=debug:file=./logs/gc-${name}_${heapSize}-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m"
    fileName="$dir/${name}_${heapSize}.log"
    java $JAVA_OPTS $heapOpt $gcLogOpt -cp build/libs/testGc-0.1-all.jar ru.${dir}.CalcDemo &> $fileName
    time=`cat $fileName |grep sec | awk '{ print $8}' | sed -e 's/sec://g'`
    echo "$heapSize	$time"
done

dir="opt4"
mkdir -p $dir
name="CalcDemoOpt4"
echo $name
for i in `seq 1 16`;do
    heapSize=$(( $i * 256 ))
    heapOpt="-Xms${heapSize}m -Xmx${heapSize}m"
    gcLogOpt="-Xlog:gc=debug:file=./logs/gc-${name}_${heapSize}-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m"
    fileName="$dir/${name}_${heapSize}.log"
    java $JAVA_OPTS $heapOpt $gcLogOpt -cp build/libs/testGc-0.1-all.jar ru.${dir}.CalcDemo &> $fileName
    time=`cat $fileName |grep sec | awk '{ print $8}' | sed -e 's/sec://g'`
    echo "$heapSize	$time"
done



echo "Done"


