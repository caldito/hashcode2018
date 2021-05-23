all: build

build: 
	javac -d bin src/*/*.java 

run: build
	java -cp bin simulation.Main

clean: 
	rm -rf bin

