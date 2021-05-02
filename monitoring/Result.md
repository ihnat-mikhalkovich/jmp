# Monitoring module

---
In `mat` directory you can find applications
   on which monitoring utilities were used.  
Also, there you can find the [Homework](./mat/Homework.md), 
   the tasks of which are solved in the next section below.
___
## Task 1. OutOfMemory (OOM) error troubleshooting
1. Get OOM error
    ![img.png](./images/img.png)
1. Use jvisualvm to observe OOM
   ![img_1.png](./images/img_1.png)
1. Get heap dump
    - Using -XX:+HeapDumpOnOutOfMemoryError option
      ![img_2.png](./images/img_2.png)
    - [Optional] Using jcmd
      1. ![img_3.png](./images/img_3.png)
      1. ![img_4.png](./images/img_4.png)
    - [Optional] Using jmap
      1. save PID
      1. ![img_5.png](./images/img_5.png)
1. Get heap histogram
    - Using jcmd
      ![img_6.png](./images/img_6.png)
    - Using jmap
      ![img_7.png](./images/img_7.png)
1. Analyze heap dump
    - Using Java Visual VM 
      1. File -> Load -> find the heap dump in finder -> Open
      1. ![img_8.png](./images/img_8.png)
1. OQL
    - jvisualvm
      1. ![img_9.png](./images/img_9.png)
      1. ![img_10.png](./images/img_10.png)
      1. ![img_11.png](./images/img_11.png)
    - jhat
      1. ![img_12.png](./images/img_12.png)
      1. ![img_13.png](./images/img_13.png)
      1. ![img_14.png](./images/img_14.png)
      1. ![img_15.png](./images/img_15.png)
    - The difference in syntax between jvisualvm and jhat are:
          1. select in java.lang.Object mess
            - jhat - `[Ljava.lang.Object;`
            - visualvm - `java.lang.Object[]`
---
## Task 2. Deadlock troubleshooting
1. Get deadlock
   ![img_16.png](./images/img_16.png)
1. Get thread dump
   1.jstack
   ![img_17.png](./images/img_17.png)
   1. kill -3 - _don't work for me, so I have just closed by `control+C`_
   ![img_18.png](./images/img_18.png)
   1. jvisualvm
   ![img_19.png](./images/img_19.png)
   1. Windows (Ctrl + Break) - _I'm using macOS, so as far as I got `control+C` for me_
   ![img_20.png](./images/img_20.png)
   1. jcmd
   ![img_21.png](./images/img_21.png)
---
## Task 3. Remote JVM profiling
1. Run application insecure remote connection
   ![img_22.png](./images/img_22.png)
1. Connect to JVM using jconsole
   ![img_23.png](./images/img_23.png)
---
## Task 4. FlightRecorder
1. Execute JVM with two special parameters
   ![img_24.png](./images/img_24.png)
   On my macbook I have OpenJdk 16, so here I don't need to add `-XX:+UnlockCommercialFeatures` parameter.
   Also, I don't need to use `-XX:+FlightRecorder` option, because `-XX:StartFlightRecording` parameter, as far as I got, include it in OpenJdk
   About `FlightRecording` function in OpenJdk you can read [here](https://habr.com/ru/company/krista/blog/532632/)
1. Enable Flight Recording on JVM without these parameters
   ![img_25.png](./images/img_25.png)
1. Open Java Mission Control and connect to default HotSpot of our JVM
   1. Open Zulu Mission Control
   ![img_26.png](./images/img_26.png)
   1. Open saved Flight Recording file in ZMC (Zulu Mission Control)
   ![img_27.png](./images/img_27.png)
---
## Task 5. jinfo
Print system properties and command-line flags that were used to start the JVM
   ![img_28.png](./images/img_28.png)
___