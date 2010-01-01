#!/bin/sh


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

function build(){
SPRINGINTERFACEPATHS="$HOME/.spring /usr/share/games/spring"
for i in $SPRINGINTERFACEPATHS; do
	SPRINGINTERFACE=$i/AI/Interfaces/Java/0.1
	if [ -e $SPRINGINTERFACE/AIInterface.jar ]; then
		echo found ai interface: $SPRINGINTERFACE
		break;
	fi
done
JAVAOTPS="-d classes -classpath ${SPRINGINTERFACE}/AIInterface.jar:${SPRINGINTERFACE}/jlib/vecmath.jar:${SPRINGINTERFACE}/jlib/jna.jar:SkirmishAI.jar:SkirmishAIReal.jar"
JAVASRC=`find src-ai -type f -name "*.java"`
JAVASRCLOADER=agai/loader/*.java
javac ${JAVAOTPS} ${JAVASRC}
jar cmf manifest.mf SkirmishAIReal.jar -C classes agai

}

$@
