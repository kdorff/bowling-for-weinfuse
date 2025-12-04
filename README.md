Install homebrew.  
https://brew.sh/

Install Java and Groovy, such as on Mac

```
brew install openjdk
brew install groovy
```

In your environment, define JAVA_HOME, GROOVY_HOME, and PATH. 
This was my definition, in my ~/.zshrc:

```
export GROOVY_HOME=/opt/homebrew/opt/groovy/libexec
export JAVA_HOME=/opt/homebrew/opt/openjdk
export PATH="${GROOVY_HOME}/bin:${JAVA_HOME}/bin:$PATH"
```

Source can be found in lib/src/main/groovy/org/bowling/Bowling.groovy
Tests can be found in lib/src/test/groovy/org/bowling/BowlingSpec.groovy

Run the tests

```
./gradlew test
```
