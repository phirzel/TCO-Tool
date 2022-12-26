# TCO-Tool

Total Cost of Ownership calculation (Standalone **Java Swing based UI Application with XML persistency files** for your Project-Data).

Now on [Github **TCO-Tool**](https://github.com/phirzel/TCO-Tool):

* (Originally hosted at [SourceForge **TCO-Tool**](https://sourceforge.net/projects/tcotool/) where coding is dicontinued!)

# How to build
1. add `TCO-Tool\lib\jhotdraw60b1\jhotdraw.jar` to classpath (seems not to be provided by MAVEN central yet or any more)
2. mvn install **without test** (not all tests run through yet, especially Module "jomm")
3. main: org.tcotool.application.LauncherView (javax.swing UI-Application)
4. Menu File->Open... tco-tool-core/test/resources/*.xml (for a sample file)

[Manual](./tco-tool-core/src/main/resources/manual/UserManual.md)