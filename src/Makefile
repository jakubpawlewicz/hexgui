JAVAC = javac
JFLAGS =
JAR = jar

#MANIFEST = Manifest.txt
#CLASS_FILES = Main.class HexGui.class HexBoard.class  \
#		BoardDrawer.class Field.class HexColor.class \
#		HexMenuBar.class \
#		RadialGradientContext.class RadialGradientPaint.class \
#		BoardLayout.class 

#IMAGE_FILES = images/wood.png

.PHONY: clean

#HexGui.jar: $(CLASS_FILES)
#	$(JAR) cvfm $@ $(MANIFEST) $(CLASS_FILES) $(IMAGE_FILES)
#	$(JAR) uf $@ Main\$$1.class

#%.class: %.java
#	$(JAVAC) $(JFLAGS) $<

Main.class:
	$(JAVAC) hexgui/util/*.java
	$(JAVAC) hexgui/hex/*.java
	$(JAVAC) hexgui/gui/*.java
	$(JAVAC) Main.java

clean:
	$(RM) hexgui/util/*.class
	$(RM) hexgui/hex/*.class
	$(RM) hexgui/gui/*.class
	$(RM) *.class
