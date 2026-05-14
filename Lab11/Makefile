server:
	@cd Server && mvn exec:java -Dexec.mainClass="ro.uaic.App"

client:
	@cd Client && mvn exec:java -Dexec.mainClass="ro.uaic.App"

test:
	@cd Server && mvn compile exec:java -Dexec.mainClass="ro.uaic.TestJMH"

clean:
	@rm -rf Client/.settings Server/.settings
	@rm -rf Client/target Server/target
	@rm -rf Client/bin Server/bin
	@rm Client/.project Server/.project
	@rm Client/.classpath Server/.classpath
