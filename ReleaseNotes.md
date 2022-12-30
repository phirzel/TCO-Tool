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
* Unit-Tests executable again
* Module JOMM focused on XML-Mapping only (SQL stuff is deprecated and not used by TCO-Tool)


# TODO
* known bugs:
  * if tree-element is double-clicked on name, the sub-window might be opened in background (workaround double-click on
    Icon in tree instead)
  * Deleting dependency by Edit-Dialog-Panel works but any open Dependency-Graphs are not visually updated yet (needs
    Dialog close and regenerate)
* improvements:
  * Dependencies in Panel on dialog show only suppliers having dependencies (Supplier's from Client side not shown yet)
  * upgrade JHotDraw lib
  * modernize old Java 1.4/6 code-style (Date, List, Apache libs, ..)
  * replace @Deprecated code
  * XmlObjectService::retrieveCodes -> logs missing DbCode's -> perhaps add a default Set to new configs?
  * replace softEnvironment logo (splash-screen, ..)