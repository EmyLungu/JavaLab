# make sever TYPE=homwork
TYPE ?= compulsory

ifeq ($(TYPE),homework)
    PKG = homework
else ifeq ($(TYPE),advanced)
    PKG = advanced
else
    PKG = compulsory
endif

MAIN_CLASS = ro.uaic.$(PKG).App

server:
	@cd Server && mvn exec:java -Dexec.mainClass="$(MAIN_CLASS)"

client:
	@cd Client && mvn exec:java -Dexec.mainClass="$(MAIN_CLASS)"

random-bot:
	@cd Client && mvn exec:java -Dexec.mainClass="ro.uaic.homework.RandomBot"

llm-bot:
	@cd Client && mvn exec:java -Dexec.mainClass="ro.uaic.homework.LLMBot"

clean:
	@rm -rf Client/.settings Server/.settings
	@rm -rf Client/target Server/target
	@rm -rf Client/bin Server/bin
	@rm Client/.project Server/.project
	@rm Client/.classpath Server/.classpath
