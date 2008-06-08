set appHome=..
set javaHome=C:/WINDOWS/system32/

set cp=%appHome%
set cp=%cp%;%appHome%/classes
set cp=%cp%;%appHome%/resources
set cp=%cp%;%appHome%/lib/liquidlnf.jar
set cp=%cp%;%appHome%/lib/API-9.4.jar
set cp=%cp%;%appHome%/lib/mail.jar
set cp=%cp%;%appHome%/lib/activation.jar
set cp=%cp%;%appHome%/lib/jcommon-1.0.9.jar
set cp=%cp%;%appHome%/lib/jfreechart-1.0.9.jar

set javaOptions=-Xms512M -Xmx512M -XX:+AggressiveHeap
set mainClass=com.jbooktrader.platform.startup.JBookTrader

%javaHome%javaw.exe -cp "%cp%" %javaOptions% %mainClass% "%appHome%"
