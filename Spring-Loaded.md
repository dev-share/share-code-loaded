# Welcome to Spring-Loaded

## What is Spring Loaded?

Spring Loaded is a JVM agent for reloading class file changes whilst a JVM is running.  It transforms
classes at loadtime to make them amenable to later reloading. Unlike 'hot code replace' which only allows
simple changes once a JVM is running (e.g. changes to method bodies), Spring Loaded allows you
to add/modify/delete methods/fields/constructors. The annotations on types/methods/fields/constructors
can also be modified and it is possible to add/remove/change values in enum types.

Spring Loaded is usable on any bytecode that may run on a JVM, and is actually the reloading system
used in Grails 2.

# Installation

1.2.5 has now been released: [springloaded-1.2.5.RELEASE.jar](http://repo.spring.io/release/org/springframework/springloaded/1.2.5.RELEASE/springloaded-1.2.5.RELEASE.jar)

1.2.6 snapshots are in this repo area (grab the most recently built .jar):
<a href="https://repo.spring.io/webapp/#/artifacts/browse/tree/General/libs-snapshot-local/org/springframework/springloaded/1.2.6.BUILD-SNAPSHOT">repo.spring.io</a>

The download is the agent jar and needs no further unpacking before use.


# Running with reloading

	java -javaagent:<pathTo>/springloaded-{VERSION}.jar -noverify SomeJavaClass

The verifier is being turned off because some of the bytecode rewriting stretches the meaning of
some of the bytecodes - in ways the JVM doesn't mind but the verifier doesn't like.  Once up and
running what effectively happens is that any classes loaded from jar files (dependencies) are not
treated as reloadable, whilst anything loaded from .class files on disk is made reloadable. Once
loaded the .class file will be watched (once a second) and should a new version appear
SpringLoaded will pick it up. Any live instances of that class will immediately see the new form
of the object, the instances do not need to be discarded and recreated.

No doubt that raises a lot of questions and hopefully a proper FAQ will appear here shortly! But in
the meantime, here are some basic Qs and As:

Q. Does it reload anything that might change in a class file?
A. No, you can't change the hierarchy of a type. Also there are certain constructor patterns of
usage it can't actually handle right now.

Q. With objects changing shape, what happens with respect to reflection?
A. Reflection results change over time as the objects are reloaded.  For example, modifying a class
with a new method and calling `getDeclaredMethods()` after reloading has occurred will mean you see
the new method in the results. *But* this does mean if you have existing caches in your system
that stash reflective information assuming it never changes, those will need to be cleared
after a reload.

Q. How do I know when a reload has occurred so I can clear my state?
A. You can write a plugin that is called when reloads occur and you can then take the appropriate
action.  Create an implementation of `ReloadEventProcessorPlugin` and then register it via
`SpringLoadedPreProcessor.registerGlobalPlugin(plugin)`. (There are other ways to register plugins,
which will hopefully get some documentation!)

Q. What's the state of the codebase?
A. The technology is successfully being used by Grails for reloading. It does need some performance
work and a few smacks with a refactoring hammer. It needs upgrading here and there to tolerate
the invokedynamic instruction and associated new constant pool entries that arrived in Java 7.

# Working with the code

	git clone https://github.com/spring-projects/spring-loaded

Once cloned there will be some projects suitable for import into eclipse. The main project and
some test projects. One of the test projects is an AspectJ project (containing both Java
and AspectJ code), and one is a Groovy project. To compile these test projects
in Eclipse you will need the relevant eclipse plugins:

AJDT: update site: `http://download.eclipse.org/tools/ajdt/42/dev/update`
Groovy-Eclipse: update site: `http://dist.springsource.org/snapshot/GRECLIPSE/e4.2/`

After importing them you can run the tests.  There are two kinds of tests, hand crafted and
generated.  Running all the tests including the generated ones can take a while.
To run just the hand crafted ones supply this to the JVM when launching the tests:

    -Dspringloaded.tests.generatedTests=false

NOTE: When running the tests you need to pass `-noverify` to the JVM also.

Two launch configurations are already included if you are importing these projects into eclipse,
which run with or without the generated tests.

A gradle build script is included, run './gradlew build' to rebuild the agent - it will be created
as something like: `springloaded/build/libs/springloaded-1.1.5.BUILD-SNAPSHOT.jar`

# Can I contribute?

Sure! Just press *Fork* at the top of this github page and get coding. Before we accept pull
requests we just need you to sign a simple contributor's agreement - which you can find
[here](https://support.springsource.com/spring_committer_signup). Signing the contributor's
agreement does not grant anyone commit rights to the main repository, but it does mean that we
can accept your contributions, and you will get an author credit if we do. Active contributors
might be asked to join the core team, and given the ability to merge pull requests.

# Basic usage information
Out of the box SpringLoaded should be usable without extra configuration, it has sensible defaults.  Trying it out is as simple as:

    java -javaagent:<pathto>/springloaded.jar -noverify SomeJavaClass

What is the default behaviour?

Making a type reloadable is quite expensive so not everything the JVM loads goes through the process. When the JVM loads a type it can determine where the class was loaded from - this information is used to make a simple choice. If the class came from a .class file on disk, it is considered a piece of the application and made reloadable. If the class came from inside a zip/jar, it is considered a piece of the infrastructure and not made reloadable.  Now, it is sometimes expensive to track down where types have been loaded from so the process for rejecting some infrastructure types is accelerated by considering just the name of the type. The following packages are considered 'infrastructure':

    antlr/
    org/springsource/loaded/
    com/springsource/tcserver/
    com/springsource/insight/
    groovy/
    groovyjarjarantlr/
    groovyjarjarasm/
    grails/
    java/
    javassist/
    org/codehaus/groovy/
    org/apache/
    org/springframework/
    org/hibernate/
    org/hsqldb/
    org/aspectj/
    org/xml/
    org/h2/

If you use one of these packages as your application package prefix, it will not be made reloadable at the moment.  An aid in trying to diagnose when this is happening is the ````explain```` mode for springloaded, see [[Configuration Options]] for more information, but basically specifying this:

    java -Dspringloaded=explain -javaagent:<pathto>/springloaded.jar -noverify SomeJavaClass

will cause logging to come out explaining the decision process in springloaded:

    Feb 05, 2014 11:00:51 AM org.springsource.loaded.TypeRegistry couldBeReloadable
    INFO: WhyNotReloadable? The type org/apache/maven/model/building/ModelBuilder is using a package name 'org/apache/' which is 
         considered infrastructure and types within it are not made reloadable

Once a type is made reloadable the .class file it came from is watched for changes by a file system watcher thread. If there is a change (e.g. your IDE or maven build recompiles the code) the file system watcher kicks springloaded and tells it to load the new version.  To see this actually occurring you can turn on the ````verbose```` mode for SpringLoaded:

    java -Dspringloaded=verbose -javaagent:<pathto>/springloaded.jar -noverify SomeJavaClass

(Beware, verbose mode is very....verbose!)

    Watcher Observed last modification time change for /Users/aclement/play/grails10411/jira-reload/target/classes/br/
            com/app/domains/CategoryController.class (lastScanTime=1391628759166)
    Watcher Firing file changed event /Users/aclement/play/grails10411/jira-reload/target/classes/br/
            com/app/domains/CategoryController.class
    ReloadableType Loading new version of br/com/app/domains/CategoryController, identifying suffix OV1YS1A,
                   new data length is 27209bytes


# Configuration Options
SpringLoaded can be configured in two ways, through a system property or via a properties file.

**Configuring via system property:**

    java -Dspringloaded=XXX -javaagent:pathto/springloaded.jar -noverify MyCode

XXX should be a semicolon separated list of either individual directives or key=value pairs. For example:

    -Dspringloaded=verbose;explain;profile=grails

Supported directives:

````verbose```` - verbose causes SpringLoaded to produce java.util.Logging output which goes into great detail on the reloading process, for example: what types is it processing? when does the filesystem watcher notice class file changes?

````explain```` - produces some explanation as to why springloaded is making certain decisions. For example when SpringLoaded fails to make your type reloadable and you need to know why. Is it because you used a package name it considers infrastructure and deliberately doesn't make reloadable (like org.springframework)? Explain mode will tell you that.

````caching```` - during the first startup springloaded will store information in a cache folder to speed up later restarts. The default cache dir is ````<userhome>/.slcache````.


Supported key/value pairs:

````cacheDir=XXXX```` - overrides the default cache directory. e.g. ````cacheDir=/tmp````

````plugins=XXXX```` - a comma separated list of plugins for SpringLoaded to add to the build in set. New plugins enable reloading to play nicely with other frameworks/libraries. e.g. ````plugins=org.foo.MyPlugin,org.bar.MyOtherPlugin````

````profile=grails```` - A profile represents a particular configuration of SpringLoaded options, basically a shorthand for specifying all those options individually. The grails profile turns on the options ````caching````  and ````plugins=org.springsource.loaded.SystemPropertyConfiguredIsReloadableTypePlugin````. The default cache directory will be ````<userhome>/.grails```` unless the additional option ````cacheDir```` is specified.

**Configuring via properties file**

tbd

# Diagnosing reloading problems

The reloading process is quite complicated. A lot of bytecode is transformed as an application is loaded, not just application classes but system classes and other dependent classes in frameworks and libraries.

Turning on ````explain```` mode or ````verbose```` mode (see [[Basic usage information]]) may help you understand why something is misbehaving but once you get into turning on verbose you probably need to raise a github issue and attach any useful info you learn from the verbose trace.  Or even better include a testcase as the user did here: [[ Issue 34 | https://github.com/spring-projects/spring-loaded/issues/34 ]] - submitted testcases are invaluable.
