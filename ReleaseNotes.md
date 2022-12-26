@Author: Peter Hirzel, Switzerland

Moved to [Github **TCO-Tool**](https://github.com/phirzel/TCO-Tool):
* (Originally hosted at [SourceForge **TCO-Tool**](https://sourceforge.net/projects/tcotool/) where coding was terminated and migrated to Github instead!)

# v1.6.0
* code initially migrated from [SourceForge](https://sourceforge.net/projects/tcotool/) 1.5.2-SNAPSHOT
* ANT to Maven build refactored
* update to OpenJDK 11 (tested by OpenJDK 19 as well)
* library updates (where found on Maven central)
* optional commercial softEnvironment Plugins added/sponsored
* JPF (Java Plugin Framework removed, considered as old and badly maintained)

# How to build
1. add `TCO-Tool\lib\jhotdraw60b1\jhotdraw.jar` to classpath (seems not to be provided by MAVEN central yet or any more)
2. mvn install **without test** (not all tests run through yet)
3. main: org.tcotool.application.LauncherView (javax.swing UI-Application)


# TODO
* provide sample.xml
* bug fixes:
    * Dialogs open in background
* upgrade JHotDraw lib