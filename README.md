# PowerStat's FBMinApi

This is a minimal Java implementation of the FritzBox AHA and TR64 APIs which will be extended by generated code.

See:

* [AVM Entwicklungssupport](https://avm.de/service/schnittstellen/)

Please note that I am not related to AVM in any way and that AVM will not support this code in any way!

## Installation

Because this library is only useful for developers the installation depends on your build environment.

For example when using Apache Maven you could add the following dependency to your project:

    <dependency>
      <groupId>de.powerstat.fb</groupId>
      <artifactId>minapi</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>

Other build tools like gradle will work analogous.

To compile this project yourself you could use:

    mvn clean install org.pitest:pitest-maven:mutationCoverage site
    
or simply:

     mvn clean install
     
To find newer dependencies:

    mvn versions:display-dependency-updates
    
To find newer plugins:

    mvn versions:display-plugin-updates
    
To make a new release:

    mvn --batch-mode release:clean release:prepare release:perform
    git push -–tags
    git push origin master

## Usage

For usage in your own projects please read the Javadoc's and follow the examples in the unittests.
Last but not least it would be better to use the full FB api that is based on this project and will be released separately.

## Contributing

If you would like to contribute to this project please read [How to contribute](CONTRIBUTING.md).

## License

This code is licensed under the [Apache License Version 2.0](LICENSE.md).
