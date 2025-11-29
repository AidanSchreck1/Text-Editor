CLASSPATH = .:Test/:Test/junit4.jar:lanterna-3.0.1.jar
JFLAGS = -g -cp $(CLASSPATH)
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Rope.java \
	Document.java \
	View.java \
	Position.java \
	Editor.java \
	Gui.java

TESTCLASSES = \
	Test/TestRunner.java \
	Test/RopeTest.java \
	Test/DocumentTest.java

default: classes

classes: $(CLASSES:.java=.class) $(TESTCLASSES:.java=.class)

clean:
	$(RM) *.class Test/*.class LOG.txt out.txt

run: classes
	java -cp $(CLASSPATH) Editor Test/foo.txt

test: classes
	java -cp $(CLASSPATH) org.junit.runner.JUnitCore RopeTest DocumentTest

debug-rope: classes
	jdb -classpath $(CLASSPATH) org.junit.runner.JUnitCore RopeTest

debug-document: classes
	jdb -classpath $(CLASSPATH) org.junit.runner.JUnitCore DocumentTest

test-rope: classes
	java -cp $(CLASSPATH) org.junit.runner.JUnitCore RopeTest

test-document: classes
	java -cp $(CLASSPATH) org.junit.runner.JUnitCore DocumentTest

test-rope-create: classes
	java -cp $(CLASSPATH) TestRunner RopeTest#testCreate

test-rope-totalweight: classes
	java -cp $(CLASSPATH) TestRunner RopeTest#testTotalWeight

test-rope-isleaf: classes
	java -cp $(CLASSPATH) TestRunner RopeTest#testIsLeaf

test-rope-collect: classes
	java -cp $(CLASSPATH) TestRunner RopeTest#testCollect

test-rope-charat: classes
	java -cp $(CLASSPATH) TestRunner RopeTest#testCharAt

test-rope-concat: classes
	java -cp $(CLASSPATH) TestRunner RopeTest#testConcat

test-rope-tail: classes
	java -cp $(CLASSPATH) TestRunner RopeTest#testTail

test-rope-head: classes
	java -cp $(CLASSPATH) TestRunner RopeTest#testHead

test-rope-subrope: classes
	java -cp $(CLASSPATH) TestRunner RopeTest#testSubrope

test-rope-delete: classes
	java -cp $(CLASSPATH) TestRunner RopeTest#testDelete

test-rope-insert: classes
	java -cp $(CLASSPATH) TestRunner RopeTest#testInsert

test-rope-reduce: classes
	java -cp $(CLASSPATH) TestRunner RopeTest#testReduce

test-document-makewordrope: classes
	java -cp $(CLASSPATH) TestRunner DocumentTest#testMakeWordRope

test-document-read: classes
	java -cp $(CLASSPATH) TestRunner DocumentTest#testRead

test-document-write: classes
	java -cp $(CLASSPATH) TestRunner DocumentTest#testWrite

test-document-collect: classes
	java -cp $(CLASSPATH) TestRunner DocumentTest#testCollect

test-document-getset: classes
	java -cp $(CLASSPATH) TestRunner DocumentTest#testGetSet

test-document-add: classes
	java -cp $(CLASSPATH) TestRunner DocumentTest#testAdd

test-document-delete: classes
	java -cp $(CLASSPATH) TestRunner DocumentTest#testDelete

test-style:
	java -cp .:Style/checkstyle-10.12.7-all.jar \
	com.puppycrawl.tools.checkstyle.Main \
	-c Style/google_checks.xml \
	Rope.java Document.java View.java Editor.java
