# J2CRPC
Java to C remote procedure call.
This project has a C file rpc_agent.c and two java files GetLocalTime.java and GetLocalOS.java.

Run the rpc_agent.c first, then run any of the java files.
Compile:
    gcc -o rpc_agent rpc_agent.c -lpthread
    javac *.java
 Run:
    ./rpc_agent
    java GetLocalTime    OR      java GetLocalOS
    
There are also C_int.java and C_char.java files that represent C int and C char data types.
Finally, there's a Test.java junit file written in junit5 to test the C_char and C_int implementations.
