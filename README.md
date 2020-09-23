### java 多线程demo
src/main/createThread/newThread.java  
&nbsp;1.测试过程中发现thread start和run方法都能输出测试结果，但是如果是start，是启动一个新线程，然后再执行其中的run方法，如果是直接调用run，执行调用了这个方法，并不是真正意义上的多线程。  
&nbsp; 2.ThreadGroup:main线程所在的threadgroup称为main，新建线程如果不明确指定threadgroup，那么和父线程同一个threadgroup(优先级也一样)。
