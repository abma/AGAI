SPRING=/home/buildslave/.spring
SPRINGINTERFACE=${SPRING}/AI/Interfaces/Java/0.1
JAVAOTPS=-d classes -classpath ${SPRINGINTERFACE}/AIInterface.jar:${SPRINGINTERFACE}/jlib/vecmath.jar:${SPRINGINTERFACE}/jlib/jna.jar:SkirmishAI.jar:SkirmishAIReal.jar
JAVASRC=agai/*.java agai/info/*.java agai/manager/*.java agai/task/*.java agai/unit/*.java
JAVASRCLOADER=agai/loader/*.java
all: loader ai

ai:
	javac ${JAVAOTPS} ${JAVASRC}
	jar cmf manifest.mf SkirmishAIReal.jar -C classes agai
loader:
	javac ${JAVAOTPS} ${JAVASRCLOADER}
	jar cmf manifest.mf SkirmishAI.jar -C classes agai/loader

run:
	cp script.txt ~/.spring/testai.txt
	nice /usr/bin/spring --window -x 1024 -y 768 testai.txt

