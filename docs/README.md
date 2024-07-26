# GeoNetwork

This project is an experimental Architecture for GeoNetwork 5 development.


## Maven Build

Building the project happens at three speeds as outlined below.

### Relaxed Build

If you are building with `-DskipTests` the maven profiles and properties have been configured to get out of the way so you can focus on troubleshooting.

```bash
mvn clean install -DskipTests -Drelax
```

The expectation is that a relaxed build is used for local development and troubleshooting.

### Default Build

The default build will run unit tests and perform some formatting and header checks in order to prep your code to share with other developers.

```bash
mvn install
```

The expectation is that you do a default build before making making a commit or pull request.

The `process-sources` stage is used for code formatting (disable with ``-Dformat.skip=true``):

* [spotless](https://github.com/diffplug/spotless): responsible for ``java`` formatting, using the google formatting convention
* [sortpom](https://github.com/Ekryd/sortpom): responsible for maven ``pom.xml`` formatting
* [Maven License Plugin](https://oss.carbou.me/license-maven-plugin/) formats headers


### Full Build

A full build with integration tests, quality assurance checks in place is used to catch common java problems, check javadocs.

```bash
mvn clean install -Dqa
```

The expectation is the github workflow `full-build` will run this on your behalf when making a pull request. If your pull request fails you may need to run this one locally.

The full build checks spotless, sortpom, and maven license header formatting. 

The quality assurance checks have been broken out into different profiles so you can test just what you need to fix.

* [CheckStyle](https://checkstyle.org) - code formatting
  
  ```
  mvn -DskipTests verify -Pcheckstyle
  ```
 
* [SpotBugs](https://spotbugs.github.io/) - post-compile bytecode analyzer common programming mistakes.

  ```
  mvn verify -Pspotbugs
  ```
  
  It is highly recommended to use the spotbugs-gui to review test results:
  ```
  mvn spotbugs:gui
  ```
    
* [Find Security Bugs](https://find-sec-bugs.github.io) SpotBugs plugin for security audits of Java web applications.

  ```
  mvn verify -Pspotbugs-security
  ```

* [Maven License Plugin](https://oss.carbou.me/license-maven-plugin/) check headers

  ```
  mvn verify -Pcheck-headers
  ```
  
