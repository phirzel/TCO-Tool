# TCO-Tool

Total Cost of Ownership calculation (Standalone **Java Swing based UI Application with XML persistency files** for your
Project-Data).

Now on [Github **TCO-Tool**](https://github.com/phirzel/TCO-Tool):

* (Originally hosted at [SourceForge **TCO-Tool**](https://sourceforge.net/projects/tcotool/) where coding is
  dicontinued!)

# How to build (for e.g. IntelliJ)

1. add `TCO-Tool\lib\jhotdraw60b1\jhotdraw.jar` to classpath (seems not to be provided by MAVEN central yet or any more)
2. mvn clean install
3. run main: org.tcotool.application.LauncherView (javax.swing UI-Application)
4. To get a first sample impression:
    * Menu File->Open... open a [demo sample configuration](./TCO-Client_Sample_en.xml) (or any other in
      tco-tool-core/test/resources/TEST_*.xml)
5. (Define log-level in tco-tool-core/src/main/resources/logback.xml)

[Manual](./tco-tool-core/src/main/resources/manual/UserManual.md)