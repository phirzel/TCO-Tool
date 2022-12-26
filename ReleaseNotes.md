@Author: Peter Hirzel, Switzerland

2022 moved to [Github **TCO-Tool**](https://github.com/phirzel/TCO-Tool):
* (Originally hosted at [SourceForge **TCO-Tool**](https://sourceforge.net/projects/tcotool/) where coding was terminated and migrated to Github instead!)

# v1.6.0
* code initially migrated from [SourceForge](https://sourceforge.net/projects/tcotool/) 1.5.2-SNAPSHOT
* ANT to Maven build refactored
* update to OpenJDK 11 (tested by OpenJDK 19 as well)
* library updates (where found on Maven central)
* optional commercial softEnvironment Plugins added/sponsored
* JPF (Java Plugin Framework removed, considered as old and badly maintained)


# TODO
* provide sample.xml
* bug fixes:
  * Dialogs open in background
  * https://sourceforge.net/p/tcotool/discussion/491266/thread/e234d2db/?limit=25#1dfc Deleting dependency
* upgrade JHotDraw lib
* modernize old Java 1.4/6 code-style
* XmlObjectService::retrieveCodes -> logs missing DbCode's -> perhaps add a default Set to new configs?