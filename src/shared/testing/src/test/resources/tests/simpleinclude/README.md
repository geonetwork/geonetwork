See XsltUtilTest.java

This requires that the input XSLT is loaded via a JAR via a URL.

This is how the main application will be loading XSLT resources, so its important the test case use the jar:...!... protocol.

