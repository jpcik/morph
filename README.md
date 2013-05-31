morph
=====

**If you don't care about compiling you can use morph** 
as in this sample Java project: https://github.com/jpcik/morph-starter
using the library through Maven or Sbt.


To build morph you need:

* jvm7
* sbt 0.12.3 (www.scala-sbt.org)

The scala version is 2.10.1, but sbt will take care of that ;)
To compile it, run sbt after downloading the code:

```
>sbt
>compile
```

To run the R2RML test cases:

```
>sbt
>project morph-r2rml-tc
>test
```

