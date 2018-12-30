.PHONY: default
default: release;

release:
        javac keywordcounter.java

clean:
        rm -rf *.class
        rm -rf keywordcounter
