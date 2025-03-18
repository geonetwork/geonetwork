# GeoNetwork

This project is an experimental Architecture for GeoNetwork 5 development.


## Maven Build

Building the project happens at three speeds as outlined below.

### Relaxed Build

If you are building with `-DskipTests` the maven profiles and properties have been configured to get out of the way so you can focus on troubleshooting.

```bash
./mvnw clean install -Drelax
```

The expectation is that a relaxed build is used for local development and troubleshooting.

### Default Build

The default build will run unit tests and perform some formatting and header checks in order to prep your code to share with other developers.

```bash
./mvnw install
```

The expectation is that you do a default build before making making a commit or pull request.

The `process-sources` stage is used for code formatting (disable with ``-Dformat.skip=true``):

* [spotless](https://github.com/diffplug/spotless): responsible for ``java`` formatting, using the google formatting convention
* [sortpom](https://github.com/Ekryd/sortpom): responsible for maven ``pom.xml`` formatting
* [Maven License Plugin](https://oss.carbou.me/license-maven-plugin/) formats headers


### Full Build

A full build with integration tests, quality assurance checks in place is used to catch common java problems, check javadocs.

```bash
./mvnw clean install -Dqa
```

The expectation is the github workflow `full-build` will run this on your behalf when making a pull request. If your pull request fails you may need to run this one locally.

The full build checks spotless, sortpom, and maven license header formatting. 

The quality assurance checks have been broken out into different profiles so you can test just what you need to fix.

* [Spotless](https://github.com/diffplug/spotless) - code formatting indentation
  
  ```bash
  ./mvnw spotless:apply
  ```
  
  Used to apply 120 column, lamda friendly formatting, [palantir java format](https://github.com/palantir/palantir-java-format) (an improved google-java-format). Using a tool prevents nit-picking during pull request reviews.

* [CheckStyle](https://checkstyle.org) - code formatting to check naming conventions
  
  ```bash
  ./mvnw -DskipTests verify -Pcheckstyle
  ```
 
* [SpotBugs](https://spotbugs.github.io/) - post-compile bytecode analyzer common programming mistakes.

  ```bash
  ./mvnw verify -Pspotbugs
  ```
  
  It is highly recommended to use the spotbugs-gui to review test results:

  ```bash
  ./mvnw spotbugs:gui
  ```
    
* [Find Security Bugs](https://find-sec-bugs.github.io) SpotBugs plugin for security audits of Java web applications.

  ```bash
  ./mvnw verify -Pspotbugs-security
  ```

* [Maven License Plugin](https://oss.carbou.me/license-maven-plugin/) check headers

  ```bash
  ./mvnw verify -Pcheck-headers
  ```

* [Sort Pom](https://github.com/Ekryd/sortpom) - code formatting for pom.xml files
  
### Integration tests

When writing integration tests which use `@SpringBootTest`, name the class file with `IntegrationTest` suffix.

If the test requires GeoNetwork 4, a Postgres database and Elasticsearch extends `GeoNetwork4BasedIntegrationTest`.
If the test only requires Elasticsearch extends `ElasticsearchBasedIntegrationTest`.

To skip integration tests, use the `-DskipITs` command:

  ```bash
  ./mvnw clean install -DskipITs
  ```
