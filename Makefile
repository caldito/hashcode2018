all: build

build: 
	javac src/*/*.java 

run: build
	java src.simulation.Main

clean: 
	rm src/*/*.class

