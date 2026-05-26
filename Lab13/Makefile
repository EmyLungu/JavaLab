# args=ro.uaic.unknown.Test1
# hw_args=/home/eeemy/Work/Info/Java/Lab12/test_homework/
# adv_args=/home/eeemy/Work/Info/Java/Lab12/test_homework/

all:
	mvn exec:java -Dexec.mainClass="ro.uaic.app.LocaleExplore"

# compulsory:
# 	mvn exec:java -Dexec.mainClass="ro.uaic.Compulsory" -Dexec.args="${args}"
#
# homework:
# 	javac test_homework/*.java; \
# 	mvn exec:java -Dexec.mainClass="ro.uaic.Homework" -Dexec.args="${hw_args}"
#
# advanced:
# 	export MAVEN_OPTS="--add-opens java.base/java.lang=ALL-UNNAMED"; \
# 	mvn exec:java -Dexec.mainClass="ro.uaic.Advanced" \
# 		-Dexec.args="${adv_args}"

clean:
	@rm -rf .settings
	@rm -rf target
	@rm -rf bin
	@rm .project
	@rm .classpath
