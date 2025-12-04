Install Homebrew or SDKMan to install Java (version 21 or later)
https://brew.sh/, https://sdkman.io/

Install `Java`, such as with Homebrew on Mac:

```
brew install openjdk
```

In your environment, define `JAVA_HOME` and `PATH.`  These are in my `~/.zshrc`:

```
export JAVA_HOME=/opt/homebrew/opt/openjdk
export PATH="${JAVA_HOME}/bin:$PATH"
```

Important source code files:

* Source can be found in [lib/src/main/groovy/org/bowling/Bowling.groovy](lib/src/main/groovy/org/bowling/Bowling.groovy)
* Tests can be found in [lib/src/test/groovy/org/bowling/BowlingSpec.groovy](lib/src/test/groovy/org/bowling/BowlingSpec.groovy)

Run all tests with

```
./gradlew test
```
