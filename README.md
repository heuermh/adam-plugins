adam-plugins
============

External plugins in Java and Scala for ADAM: Genomic Data System.  Apache 2 licensed.


###Hacking adam-plugins

Install

 * JDK 1.7 or later, http://openjdk.java.net
 * Scala 2.10.4 or later, http://www.scala-lang.org
 * Apache Maven 3.2.5 or later, http://maven.apache.org
 * Apache Spark 1.2.0 or later, http://spark.apache.org
 * ADAM: Genomic Data System 0.16.0 or later, https://github.com/bigdatagenomics/adam


To build

    $ mvn install


To add the plugins in this repository to the ADAM command line, add the jar to ```$ADDL_JARS```

    $ cp target/adam-plugins-0.16.1-SNAPSHOT.jar $ADAM_DIR
    $ cd $ADAM_DIR
    $ ADDL_JARS=adam-plugins-0.16.1-SNAPSHOT.jar bin/adam-submit


####Examples

    $ ADDL_JARS=adam-plugins-0.16.1-SNAPSHOT.jar bin/adam-submit \
            plugin com.github.heuermh.adam.plugins.CountAlignments \
            adam-core/src/test/resources/small.sam
     
    (1,20)


    $ ADDL_JARS=adam-plugins-0.16.1-SNAPSHOT.jar bin/adam-submit \
            plugin com.github.heuermh.adam.plugins.CountAlignmentsPerRead \
            adam-core/src/test/resources/small.sam
     
    (simread:1:237728409:true,1)
    (simread:1:195211965:false,1)
    (simread:1:163841413:false,1)
    (simread:1:231911906:false,1)
    (simread:1:26472783:false,1)
    (simread:1:165341382:true,1)
    (simread:1:240344442:true,1)
    (simread:1:50683371:false,1)
    (simread:1:240997787:true,1)
    (simread:1:14397233:false,1)
    (simread:1:207027738:true,1)
    (simread:1:20101800:true,1)
    (simread:1:101556378:false,1)
    (simread:1:37577445:false,1)
    (simread:1:189606653:true,1)
    (simread:1:5469106:true,1)
    (simread:1:186794283:true,1)
    (simread:1:89554252:false,1)
    (simread:1:153978724:false,1)
    (simread:1:169801933:true,1)
