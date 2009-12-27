#!/bin/sh
AIPATH=AI/Skirmish/AGAI/0.1/
TMP=`mktemp -d`
DIR=`pwd`/AGAI.zip

cp AIInfo.lua ${TMP}
mkdir -p ${TMP}/${AIPATH}
cp *.jar ${TMP}/${AIPATH}
cd ${TMP}
rm -f ${DIR}
zip -r ${DIR} .

echo ${DIR} is ready!
