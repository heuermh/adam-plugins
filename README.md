adam-plugins
============

External plugins for ADAM: Genomic Data System


###Hacking adam-plugins

Install

 * JDK 1.7 or later, http://openjdk.java.net
 * Scala 2.10.3 or later, http://www.scala-lang.org
 * Apache Maven 3.2.1 or later, http://maven.apache.org
 * ADAM: Genomic Data System 0.12.0 or later, https://github.com/bigdatagenomics/adam


To build

    $ mvn install


To add the plugins in this repository to the ADAM command line

    $ cp target/adam-plugins-1.0-SNAPSHOT.jar $ADAM_DIR
    $ cd $ADAM_DIR
    $ java -classpath "adam-cli/target/adam-$VERSION.jar:adam-plugins-$VERSION.jar" \
            org.bdgenomics.adam.cli.ADAMMain


####Examples

    $ java -classpath adam-cli/target/adam-0.12.1-SNAPSHOT.jar:adam-plugins-1.0-SNAPSHOT.jar \
            org.bdgenomics.adam.cli.ADAMMain \
            plugin com.github.heuermh.adam.plugins.CountAlignments \
            adam-core/src/test/resources/small.sam 
    
    (1,10)
    (unmapped,10)


    $ java -classpath adam-cli/target/adam-0.12.1-SNAPSHOT.jar:adam-plugins-1.0-SNAPSHOT.jar \
            org.bdgenomics.adam.cli.ADAMMain \
            plugin com.github.heuermh.adam.plugins.CountAlignmentsPerRead \
            adam-core/src/test/resources/small.sam 
     
    (simread:1:89554252:false,1)
    (simread:1:195211965:false,1)
    (simread:1:101556378:false,1)
    (simread:1:231911906:false,1)
    (unmapped,10)
    (simread:1:14397233:false,1)
    (simread:1:153978724:false,1)
    (simread:1:37577445:false,1)
    (simread:1:26472783:false,1)
    (simread:1:50683371:false,1)
    (simread:1:163841413:false,1)
