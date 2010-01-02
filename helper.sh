#!/bin/sh

# helper script: is used by buildbot
# commands:
#	build
#	test
#	release
#	clean

function run(){
	cp script.txt ~/.spring/testai.txt
	exec nice /usr/bin/spring --window -x 1024 -y 768 testai.txt
}

function release(){
	_c mkdir -p release/AI/Skirmish/AGAI/0.1
	_c cp AIInfo.lua release
	_c cp *.jar release/AI/Skirmish/AGAI/0.1
	_c cd release
	_c zip -r AGAI.zip .
	_c cd ..
	if [ "$1" != "" ]; then
		NEWFILE=AGAI-`date +"%Y%m%d%H%M%S"`-`git rev-parse HEAD`.zip
		_c mv release/AGAI.zip $1/$NEWFILE
		cd $1
		chmod 644 $NEWFILE 
		ln -s -f $NEWFILE AGAI-current.zip
		cd -
	else
		_c mv release/AGAI.zip .
	fi
	_c rm -rf release
}

function _c(){
	echo $@
	if ! $@; then
		echo failed
		exit 
	fi
}

function build(){
	mkdir classes
	SPRINGINTERFACEPATHS="$HOME/.spring /usr/share/games/spring"
	for i in $SPRINGINTERFACEPATHS; do
		SPRINGINTERFACE=$i/AI/Interfaces/Java/0.1
		if [ -e $SPRINGINTERFACE/AIInterface.jar ]; then
			echo found ai interface: $SPRINGINTERFACE
			break;
		fi
	done
	JAVAOTPS="-d classes -classpath ${SPRINGINTERFACE}/AIInterface.jar:${SPRINGINTERFACE}/jlib/vecmath.jar:${SPRINGINTERFACE}/jlib/jna.jar:SkirmishAI.jar"
	JAVASRC=`find src-ai -type f -name "*.java"`
	JAVASRCLOADER=`find src-loader -type f -name "*.java"`
	echo building loader
	if [ ! -s SkirmishAI.jar ]; then
		_c javac ${JAVAOTPS} ${JAVASRCLOADER};
		_c jar cf SkirmishAI.jar -C classes agai/loader
	fi
	_c javac ${JAVAOTPS} ${JAVASRC}
	_c jar cf UnderlyingAI-src.jar -C classes agai
}

function clean (){
	FILES="CMakeFiles/ classes/ SkirmishAI.jar UnderlyingAI-src.jar AGAI.zip"
	_c rm -rf $FILES
}

function test(){
	echo no tests implemented
}

echo $@
$@ $2 $3 $4
