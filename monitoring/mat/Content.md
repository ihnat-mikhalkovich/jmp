**Topics:**
=======================
- JVM utilities to monitor and troubleshoot java applications
- Common mistakes in Java programs and how to identify them: OutOfMemory, deadlocks etc.

**Required time for the course:** 
=======================
Lectures: ~5h
Homework: ~8h

**Lectures:** 
=======================
[About JVM Performance Tuning at Twitter](https://www.youtube.com/watch?v=8wHx31mvSLY)
[Troubleshooting Memory Problems in Java Applications](https://www.youtube.com/watch?v=iixQAYnBnJw)

Here are two main articles you need to go through before jump into the homework
[Java 8 Troubleshoot Guide](https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/)
[Monitoring Tools](https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/tooldescr025.html)

Also, please, feel free to visit references in order to get more familiar with the tools

**Prerequisites**
======================
| JDK | 8.x | [Java 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)  |
| Maven | 3.6.x  | [Apache Maven](http://maven.apache.org/download.cgi)|

#### Webinar script for presenter

- We will work with Java 8 tutorials. There are some changes in further JDK versions, but they are not significant.
- We use only tools provided in JDK. In most cases they are enough to have.

## Java Flight Recorder

Java Flight Recorder (JFR) is a monitoring tool that collects information about the events in a JVM during the execution of Java application.

JFR is designed to affect the performance of a running application as little as possible.

JFR saves data about the events in a single output file, `flight.jfr`.

**References**
=======================
[Java VisualVM](https://docs.oracle.com/javase/8/docs/technotes/guides/visualvm/intro.html)
[JConsole](https://docs.oracle.com/javase/8/docs/technotes/guides/management/jconsole.html)
[The jcmd Utility](https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/tooldescr006.html)
[The jmap Utility](https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/tooldescr014.html)
[The jhat Utility](https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/tooldescr012.html)
[The jstack Utility](https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/tooldescr016.html)
[The jps Utility](https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/tooldescr015.html)
[JVM Tool Interface (JVM TI)](https://docs.oracle.com/javase/8/docs/technotes/guides/jvmti/)
[VisualVM Standalone](https://visualvm.github.io/)
[Analyzing a Heap Dump Using OQL](https://visualvm.github.io/documentation.html)
[Java Mission Control](https://github.com/openjdk/jmc)

