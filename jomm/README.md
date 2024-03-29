# JOMM - Persistence-Framework

Rather a self-made legacy persistence framework, nowadays one would use JPA instead.

This library contains persistence Mapping classes:
* ch.softenvironment.jomm.* (basic structures & mechanisms)
* ch.softenvironment.jomm.mvc.* (GUI building support according to MVC-pattern)
* ch.softenvironment.target.* (concrete adaptions for Target-System according to JDO-Standard)
* ch.softenvironment.jomm.tools.* (graphical tools for Target-System administration)
* ch.softenvironment.jomm.serialize.* (Serializing tools for XML, HTML, etc)


This project is an updated version of the original [SourceForge **JOMM**](https://sourceforge.net/projects/jomm/) which is not maintained any more and slightly modernized because of its XML persistency used by **TCO-TOOL**.

JOMM depends on:
* sebase.jar 	-> @see http://sourceforge.net/projects/umleditor/
* jdo.jar	-> @see http://www.sun.com/software/communitysource/jdo/download.xml
* (jta.jar	-> @see http://java.sun.com/products/jta/)
* (xerces.jar	-> @see http://xml.apache.org/)
* (various Database drivers, such as H2, MySql, PostgreSQL, ..)