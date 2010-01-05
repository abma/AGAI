#!/bin/sh

# helper script: is used by buildbot
# commands:
#	build
#	test
#	release
#	clean
# run is used to run spring with agai

function install(){
	AIPATH=~/.spring/AI/Skirmish/AGAI/`cat VERSION`
	_c mkdir -p $AIPATH
	_c cp script.txt ~/.spring/testai.txt
	_c cp data/* *.jar $AIPATH
}

function run(){
	install
	_c exec nice spring --window -x 1024 -y 768 testai.txt
}

function release(){
	RELEASEPATH=release/AI/Skirmish/AGAI/`cat VERSION`
	_c mkdir -p ${RELEASEPATH}
	_c cp data/* *.jar ${RELEASEPATH}
	_c cd release
	_c zip -r AGAI.zip .
	_c cd ..
	if [ "$1" != "" ]; then
		NEWFILE=AGAI-`date +"%Y%m%d%H%M%S"`-`git rev-parse HEAD`.zip
		_c mv release/AGAI.zip $1/$NEWFILE
		_c cd $1
		_c chmod 644 $NEWFILE 
		_c ln -s -f $NEWFILE AGAI-current.zip
		_c cd -
	else
		_c mv release/AGAI.zip .
	fi
	_c rm -rf release
}

function _c(){
	echo $@
	if ! $@; then
		echo failed
		exit -1
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
	_c jar cf UnderlyingAI.jar -C classes agai
}

function clean (){
	FILES="CMakeFiles/ classes/ SkirmishAI.jar UnderlyingAI.jar AGAI.zip"
	_c rm -rf $FILES
}

function test(){
	install
	if [ "`spring --list-skirmish-ais |grep AGAI`" == "" ]; then
		echo "AGAI couldn't be detected by the spring engine"
		exit -1
	fi

}

function doc(){
	if [ "$1" == "" ];  then
		echo "javadoc: needs path to create doc"
		exit -1
	fi
	_c javadoc -sourcepath . -d $1 `find -type f -name "*.java"`
	_c chmod -R 755 $1
}

echo $@
$@ $2 $3 $4
