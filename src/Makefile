#----------------------------------------------------------------------------
# $Id$
#----------------------------------------------------------------------------
JAVA = java
JAVAC = javac
JAVADOC = javadoc
JFLAGS =
JAR = jar

DOCDIR = ../doc/

#MANIFEST = Manifest.txt
#CLASS_FILES = Main.class HexGui.class HexBoard.class  \
#		BoardDrawer.class Field.class HexColor.class \
#		HexMenuBar.class \
#		RadialGradientContext.class RadialGradientPaint.class \
#		BoardLayout.class 

#IMAGE_FILES = images/wood.png

.PHONY: clean run compile
default: compile

#HexGui.jar: $(CLASS_FILES)
#	$(JAR) cvfm $@ $(MANIFEST) $(CLASS_FILES) $(IMAGE_FILES)
#	$(JAR) uf $@ Main\$$1.class

#%.class: %.java
#	$(JAVAC) $(JFLAGS) $<

compile:
	$(RM) Main.class >> /dev/null
	$(JAVAC) hexgui/util/*.java
	$(JAVAC) hexgui/hex/*.java
	$(JAVAC) hexgui/game/*.java
	$(JAVAC) hexgui/sgf/*.java
	$(JAVAC) hexgui/gui/*.java
	$(JAVAC) Main.java

clean:
	$(RM) -rf $(DOCDIR)
	$(RM) hexgui/util/*.class
	$(RM) hexgui/hex/*.class
	$(RM) hexgui/game/*.class
	$(RM) hexgui/sgf/*.class
	$(RM) hexgui/gui/*.class
	$(RM) *.class

run:
	$(JAVA) -ea Main

docs:
	$(JAVADOC) -d $(DOCDIR) hexgui