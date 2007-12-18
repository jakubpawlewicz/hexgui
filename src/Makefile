#----------------------------------------------------------------------------
# $Id$
#----------------------------------------------------------------------------
JAVA = java
JAVAC = javac
JAVADOC = javadoc
JFLAGS =
JAR = jar
CLASSPATH =  #'../lib/jfreechart/jcommon-1.0.12.jar;../lib/jfreechart/jfreechart-1.0.8a.jar;.'

DOCDIR = ../doc/
VERSIONDIR = hexgui/version/

#MANIFEST = Manifest.txt
#CLASS_FILES = Main.class HexGui.class HexBoard.class  \
#		BoardDrawer.class Field.class HexColor.class \
#		HexMenuBar.class \
#		RadialGradientContext.class RadialGradientPaint.class \
#		BoardLayout.class 

#IMAGE_FILES = images/wood.png

.PHONY: clean run compile build version buildnumber
default: version compile

#HexGui.jar: $(CLASS_FILES)
#	$(JAR) cvfm $@ $(MANIFEST) $(CLASS_FILES) $(IMAGE_FILES)
#	$(JAR) uf $@ Main\$$1.class

#%.class: %.java
#	$(JAVAC) $(JFLAGS) $<

version:
	cd hexgui/version && ./update.sh

buildnumber:
	$(RM) hexgui/version/svn_version
	svnversion -n . > hexgui/version/svn_version
	@echo "svn revision number stored."

compile:
	$(JAVAC) hexgui/version/*.java
	$(JAVAC) hexgui/util/*.java
	$(JAVAC) hexgui/hex/*.java
	$(JAVAC) hexgui/game/*.java
	$(JAVAC) hexgui/sgf/*.java
	$(JAVAC) hexgui/htp/*.java
	$(JAVAC) -cp "../lib/jfreechart/jcommon-1.0.12.jar;../lib/jfreechart/jfreechart-1.0.8a.jar;." hexgui/gui/*.java
	$(JAVAC) hexgui/Main.java

clean:
	$(RM) -rf $(DOCDIR)
	$(RM) hexgui/version/*.class
	$(RM) hexgui/util/*.class
	$(RM) hexgui/hex/*.class
	$(RM) hexgui/game/*.class
	$(RM) hexgui/sgf/*.class
	$(RM) hexgui/htp/*.class
	$(RM) hexgui/gui/*.class
	$(RM) hexgui/*.class

run:
	$(JAVA) -cp "../lib/jfreechart/jcommon-1.0.12.jar;../lib/jfreechart/jfreechart-1.0.8a.jar;." -ea hexgui/Main

docs:
	$(JAVADOC) -d $(DOCDIR) -subpackages hexgui

