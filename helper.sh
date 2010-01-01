#!/bin/sh

# helper script: is used by buildbot
# commands:
#	build
#	test
#	release
#	clean

function run(){
	cp script.txt ~/.spring/testai.txt
	nice /usr/bin/spring --window -x 1024 -y 768 testai.txt
}

function release(){
	mkdir -p release/AI/Skirmish/AGAI/0.1
	cp AIInfo.lua release
	cp *.jar release/AI/Skirmish/AGAI/0.1
	cd release
	zip -r AGAI.zip .
	cd ..
	rm -rf release
	echo AGAI.zip is ready!
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
	_c javac ${JAVAOTPS} ${JAVASRCLOADER};
	_c jar cf SkirmishAI.jar -C classes agai/loader
	_c javac ${JAVAOTPS} ${JAVASRC}
	_c jar cf UnderlyingAI-src.jar -C classes agai
}

function clean (){
	FILES=CMakeFiles/
	echo rm -rf $FILES
	rm -rf $FILES
}

function test(){
	echo no tests implemented
}

echo $@
$@
