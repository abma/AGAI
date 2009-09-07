SPRING=/usr

SPRINGINTERFACE=${SPRING}/lib/spring/AI/Interfaces/Java/0.1
JAVAOTPS=-d classes -classpath ${SPRINGINTERFACE}/AIInterface.jar:${SPRINGINTERFACE}/jlib/vecmath.jar:${SPRINGINTERFACE}/jlib/jna.jar

JAVASRC=agai/*.java

all:
	javac ${JAVAOTPS} ${JAVASRC}
	jar cmf manifest.mf SkirmishAI.jar -C classes agai
run: all
	cp script.txt ~/.spring/testai.txt
	${SPRING}/bin/spring --window -x 1024 -y 768 testai.txt

