Creating a New App
------------------

GN5 has several mechanisms to make it easier to create new Applications.  In general, there are three main use-cases:

* [Curating existing Modules](#curating-existing-modules)
* [Changing the configuration of existing Modules](#changing-the-configuration-of-existing-modules)
* [Creating your own module](#creating-your-own-module)

# Curating existing Modules

Typically, this use case is to create an application based on existing modules.  For example, you might want to have an application that has OGCAPI-Records as well as configuring a proxy to GN4.  

These are easy to create - just add the needed modules as dependencies!

Process:

1. Checkout GN5 and build
2. Create a new directory in the `src/apps` folder
3. Create a `pom.xml` in your directory
4. Inside your `pom.xml`, fill in the basic outline (see `src/apps/workshop/workshop1_app/pom.xml` for an example)

    * Parent's `<artifactId>` should be `gn-apps`
    * Give your app a unique `<artifactId>`
    * Add a `<name>` and `<description>`
    * Ensure you have the `<build>` section so that spring can find the main Application to run
    * In the `<dependencies>` section, make sure you reference `gn-generic-app`:

    ``` xml
        <dependency>
            <groupId>org.geonetwork</groupId>
            <artifactId>gn-generic-app</artifactId>
            <version>${project.version}</version>
        </dependency>
    ```

    * Reference the other applications or modules you require in the  `<dependencies>` section:

    ``` xml
        <!-- this will add the OGCAPI-Records to your application-->
        <dependency>
            <groupId>org.geonetwork</groupId>
            <artifactId>gn-ogcapi-records-implementation</artifactId>
            <version>${project.version}</version>
        </dependency>

        <!-- this will add the GN4-proxy module to your application-->
        <dependency>
            <groupId>org.geonetwork</groupId>
            <artifactId>gn-4</artifactId>
            <version>${project.version}</version>
        </dependency>
    ```

    * Run your application

    ```bash
        mvn spring-boot:run
    ```

    * Your application will be available at  [http://localhost:7979/geonetwork](http://localhost:7979/geonetwork)
    * OGCAPI-Records will be available at [http://localhost:7979/geonetwork/ogcapi-records](http://localhost:7979/geonetwork/ogcapi-records)
    * The proxied GN4 will be available at  [http://localhost:7979/geonetwork/srv](http://localhost:7979/geonetwork/srv) NOTE: ensure you have a running GN4 at [http://localhost:8080/geotwork](http://localhost:8080/geotwork) and configure it accept security headers from GN5.


# Changing the configuration of existing Modules

In many cases you will want to change the configure of GN5 and its modules.  This is done by modifying the "`application.yml`" files.

!!! warning "Better way to do this in the future"
    This method involves hard-coding changes to the "`application.yml`" files in the repository. We are working on a simpler way to do this.  See [Multiple Application.yml](#multiple-applicationyml), below.


Process:

1. Create your [curated application](#curating-existing-modules)
2. Find the applicable "`application.yml`" files and modify them

    * The main "`application.yml`" is in `config/application.yml`
    * The OGCAPI-Records configuration is in `config/application-ogcapi-records.yml`
    * The GN4 Proxy configuration is in `src/modules/geonetwork4/src/main/resources/application-proxy.yml`

3. Rebuild GN5
4. Run your application

!!! warning "Finding the "`application.yml`" files"
    See [below](#) for some help finding the  "`application.yml`" files for a module/application.


# Creating your own Module

You can build your own modules.  In general, the directory structure is:

* `src/apps` - these are spring-boot applications and can be run
* `src/modules` - these define controllers for the spring-boot applications.  Typically these are very simple.
* `src/shared` - these are "groups" of functionality that can be shared with other modules.

## Very Simple Modules

These are module that are very simple with very little code.  They just defines a `src/app` application and do not need a `src/shared` module.  An example is the [workshop1_app](https://github.com/geonetwork/geonetwork/tree/dev/src/apps/workshop/workshop-app1).

This is a trivial module that just does a few calculations on what is in the GN `metadata` database table.

Lets look at this in a little more detail.  This only contains a `pom.xml` and a single java file (`WorkshopController.java`)

### `pom.xml`

See [Curating existing Modules](#curating-existing-modules), above, for more details.

This has a dependency on:

* `gn-generic-app` (which provides the GN5 spring-boot infrastructure)
* `gn-schemas` which pulls in the basic DB and schema support (this is a `src/shared` module)

It also has the `<build><plugin>` so spring-boot can find the main Application class.

### `WorkshopController.java`

This defines the following endpoints:

* [http://localhost:7979/geonetwork/workshop1](http://localhost:7979/geonetwork/workshop1)
* [http://localhost:7979/geonetwork/workshop1b](http://localhost:7979/geonetwork/workshop1b)
* [http://localhost:7979/geonetwork/workshop1c](http://localhost:7979/geonetwork/workshop1c)
* [http://localhost:7979/geonetwork/workshop1d](http://localhost:7979/geonetwork/workshop1d)
* [http://localhost:7979/geonetwork/workshop1e](http://localhost:7979/geonetwork/workshop1e)

OpenAPI (swagger) endpoints (for use with the `workshop1e` endpoint):

 * [http://localhost:7979/geonetwork/v3/api-docs?f=json](http://localhost:7979/geonetwork/v3/api-docs?f=json) JSON version of the OpenAPI document
 * [http://localhost:7979/geonetwork/swagger-ui/index.html](http://localhost:7979/geonetwork/swagger-ui/index.html) HTML version of teh OpenAPI document

These endpoints:

* workshop1 - returns hardcoded trivial hello-world `String`
* workshop1b - returns hardcoded `Map<String,Long>` 
* workshop1c - returns hardcoded `GeoNetworkMetadataSummary `
* workshop1d - returns with real DB results 
* workshop1e - same as workshop1d, but with openapi information

The endpoints go from very simple more complex (including communicating with the DB).

## More Complex Modules

An example is the [workshop2_app](https://github.com/geonetwork/geonetwork/tree/dev/src/apps/workshop/workshop-app2) does the same thing as workshop1's `workshop1e`.  However, we do this in a way that is more organized (and follows the way the OGCAPI-Records module is constructed).

The main differences between `workshop1_app` and `workshop2_app` are:

1. Instead of hand-coding our controllers and the controllers response object, we autogenerate them from OpenAPI (swagger) `.yml` files
2. We use the full `src/app`, `src/module`, and `src/shared` source layout
3. We use more spring-oriented Repository access (DB)


### Source code layout (`src/app`, `src/module`, and `src/shared`)

| Location | Content |
| -------- | ------- |
| `src/app` | This is the application - typically, it only contains a `pom.xml` to manage "pulling in" dependencies |
| `src/module` | This is where the spring controllers are defined.  In this example it contains two modules - one that auto-generates controller interface and response objects from OpenAPI `.yml` documents.  It also contains a trivial implementation of the controller API that "hands off" the work to a class in `src/shared`  |
| `src/shared`| This is where the actual code goes.  In this case, it accesses the database and creates the actual response object.  It can use other GN5 code by depending on other `/src/shared` modules.  |


### Generating OpenAPI (swagger) `.yml` files

In `workshop1_app`, we defined (by hand):

* Controllers methods (including some OpenAPI metadata in `workshop_1e`) in `WorkshopController`
* A controller response object (`GeoNetworkMetadataSummary`) 

In `workshop2_app`, we do this differently:

* We create a OpenAPI `.yml` document that defines our [controller methods and describes the response objects](https://github.com/geonetwork/geonetwork/tree/dev/src/apps/workshop/workshop-app2/module/workshop2-autogen-openapi/src/openapi-schema))
* We use this to auto-generate spring controller Java interface and the Java controller response object (see `Workshop2Api` and `Workshop2MetadataSummarySchemaDto`) 
* We create a (hand-made) trivial implementation of the controller interface (see `Workshop2ControllerImpl`).  This just hands the work off to methods in `shared`

This is all done in the `module` directory.

### Actual Implementation (`/src/shared`)

This is where the actual work is being done - we query the database and fill in the `Workshop2MetadataSummarySchemaDto` object.  In general, the code in the `src/module` should be very simple - just a few lines for each endpoint.  They should hand off to a class in a `src/shared` class which is more in-depth.  This helps to make sure that the `src/shared` code is stand-alone and re-usable by other modules.

### Spring-oriented Repository

`workshop2_app` also gives an example of more complex database interactions (cf. `SummarizingMetadataRepository`).  This is standard JPA, but worth pointing out.

## Support for Building your own Modules

The architecture of GN5 allows for building new and combing existing modules.  


| Location | Content |
| -------- | ------- |
| `src/app` | This is the spring-boot app.  Typically, it only contains a `pom.xml` to manage "pulling in" existing dependencies (modules) |
| `src/module` | This is where the spring controllers are defined.  This should NOT be complex - the controller endpoints should be trivial (only a few lines). |
| `src/shared`| This is where the actual code goes.  This should be fairly independent so it can be re-used elsewhere.  |

### "Curating" modules 

See [Curating existing Modules](#curating-existing-modules), above.  GN5 makes it easy to put together multiple spring-boot applications just inside the `pom.xml`.

### "Auto Generate" via OpenAPI 

See [section on OpenAPI generation](#generating-openapi-swagger-yml-files), above.  Although this is not necessary (see `workshop1_app`, above), it helps to make endpoint definitions more clear (and with documentation).  OGCAPI-Records was created this way - we took the OGCAPI OpenAPI definition and tweaked it a bit to produce good DTOs.


### Multiple `application.yml`

One of new things that GN5 introduces is the ability to have multiple `application.yml` file ([see above](#changing-the-configuration-of-existing-modules)).

Please see `IConfigurationLocator` and its implementations (especially `AbstractResourceApplicationYmlProvider`).

These `IConfigurationLocator` classes are picked up **VERY** early in the spring-boot start-up process.  They can then add an **ordered** `application.yml` file that can either add new configuration items or override the value of previous one.  They are ordered by *priority* (lower value mean loaded first) and then we use the [`spring.config.location`](https://docs.spring.io/spring-boot/reference/features/external-config.html) mechanism to load them.

This is really useful.  For example, in our example ([OGCAPI-Records and the GN5-to-GN4 proxy](#curating-existing-modules)) we have several `application.yml` loaded:

1. `config/application.yml` - this is the basic configuration of the GN5 spring-boot app with deals of PostgreSQL and Elastic connection details, etc...
2. `config/application-ogcapi-records.yml` - this configures the OGCAPI-Records module, especially dynamic properties and default profiles (see [Content Negotiation System](#content-negotiation-system))
3. `src/modules/geonetwork4/src/main/resources/application-proxy.yml` - defines the configuration of the spring gateway proxy to GN4.

If you make a new module that needs configuration, simple add an `application-*.yml` to your `src/java/main/resources` directory and include an `AbstractResourceApplicationYmlProvider` subclass (pointing to your `application-*.yml` with a given priority). This can override existing items (larger priority number "wins") or add new ones.


!!! warning "`IConfigurationLocator` implementations must be in the `org.geonetwork.application` package"
    In order to quickly scan for the `IConfigurationLocator` implementations, we only scan for them in the `org.geonetwork.application` package.  Implementation in other packages will **not** be detected.

### Test Containers (Integration Testing)

There is support for running sprint-boot integration tests - with a real PostgreSQL and Elastic running.  

The two main examples are:

* `QueryTest` and `DynamicPropertiesTest` - this starts PostgreSQL and Elastic pre-loaded with sample data (from the [GN-UI](https://github.com/geonetwork/geonetwork-ui) project).
* `EmptyIndexTest` - this starts Elastic **without** pre-loaded sample data.

These are very good creating specific integration tests - including how the elastic queries actually interact with the database.

!!! warning "Test Containers and spring-boot configuration"
    You may find the Test Container setup complicated.  This is mostly because of the integration between Test Containers and the spring-boot `ApplicationContextInitializer` interact with very expensive startup (i.e. it takes 20 seconds to startup the Elastic container). There is "fighting" with some things wanting to be `static` (which is difficult with subclass).  See `QueryTest`, `DynamicPropertiesTest`, and `EmptyIndexTest`.  Spring-boot will often create two copies of a class (one for the test case and one for the `ApplicationContextInitializer`).  The way it is currently setup avoids this (but makes the test cases a little bit more complicated).

### Content Negotiation System

OGCAPI-Records content negotiation is quite complicated.  In a typical spring-boot system, you would have controllers returns a java POJO that will then be typically transcribed as JSON or XML.  However, OGCAPI-Records is more complex as it introduces the concept of "profiles".

For example, we support `application/rdf+xml` (i.e. DCAT) with a profiles of `eu-dcat-ap-hvd`, `eu-geodcat-ap`, `dcat`, `eu-dcat-ap` (more being added).  These are dynamic (i.e. change as the XSLT schemas are updated).  However, the JSON/XML and RDF+XML representations aren't just re-encodings of the same object - they are quite different.  Also, each profile is quite different.  This doesn't quite match the simple spring-boot content management system.

In spring-boot, the [basic process](https://www.google.com/search?q=spring+boot+content+negotiation) is that the controller endpoint returns a POJO.  Spring-boot looks at the request to see what format has been requested (i.e. with "Accepts:" header or "?f=json" in the query). It will then look up a `HttpMessageConverter` that knows how to convert that POJO to the requested format.

In the OGCAPI-Records module, its a bit more complicated.

1. As normal, the request comes to the controller endpoint.
2. However, we do not return the "big" POJO of the response.  Instead we encapsulate the request parameter (and perhaps a small amount of actual data) in a simple `IControllerResponse` object.
  
  * `OgcApiLandingPageResponse` - for the OGCAPI-Records landing page (`/`)
  * `OgcApiRecordsCollectionsResponse` - for the OGCAPI-Records collections page  (`/collections`)
  * `OgcApiCollectionResponse` - for the OGCAPI-Records single-collection page (`/collections/<collectionId>`)
  * `OgcApiRecordsMultiRecordResponse` - for the OGCAPI-Records collection-items page  (`/collections/<collectionId>/items`)
  * `OgcApiRecordsSingleRecordResponse` - for the OGCAPI-Records collection-items-record page (`/collections/<collectionId>/items/<recordId>`)

3. These will typically have a `RequestMediaTypeAndProfile` which resolves the requests MIME TYpe as well as does content negotiation (including a default profile if one isn't specified)
4. These are then sent to "special" (`HttpMessageConverter`) that is profile-aware
5. These `HttpMessageConverter`s will actually do the "hard" work of creating the output content (i.e. in Java or in XSLT).

There are two main type of `HttpMessageConverter`:

* `IContentNegotiationInitializable` - This is a factory class for dynamic converters that return `IControllerResultFormatter`s.  These are used for runtime-based converters like the `FormatterApi` produces (ie. GN XSLT and `IndexRecord` based converters managed by the `SchemaManager`). These create a profile-aware class that 
* Regular `HttpMessageConverter` - These should have a `#write(...)` method that handles a specific `IControllerResponse` so it can be found with reflection.  These should handle one MimeType and and one `IControllerResponse` type.  See `CswCollectionMessageWriter` and `OgcApiCollectionResponseFormatter` for examples.

Basically, on startup, `WebConfig` will call `MessageWriterUtil` that looks for the `IContentNegotiationInitializable` to create the runtime (i.e. `FormatterApi`) message converters.  It also keeps track all the `HttpMessageConverter`s in the system.  `MimeAndProfilesForResponseType` is the key class for querying the content negotiation system.  

Profile negotiation is done by `RequestMediaTypeAndProfileBuilder` and creates a `RequestMediaTypeAndProfile` for each request.  These are typically placed in the `IControllerResults` object.  This helps simplify later logic since the negotiated MIME type and profile (including default profiles) are determined.


Please look into these classes:

* `IControllerResults` - interface to mark POJO objects as special objects that can be formatted.
* `MessageWriterUtil` - setups (`IContentNegotiationInitializable`) and contains a list of all the `HttpMessageConverter`s in the system.
* `MimeAndProfilesForResponseType` - given a `IControllerResults`, gives information about how it can be formatted.  This is a core class.
* `RequestMediaTypeAndProfile` and `RequestMediaTypeAndProfileBuilder` - this does the profile negotiation (typically attached to the `IControllerResults`).


Helpful hints:

* the order of the `MessageConverter`s are important - the first one to match "wins."  We put the more specific ones first in the list (i.e. the generic JSON writer is **not** preferred).
* One of the key concepts are the `ResponseTypeInfo`s that are determined by `MimeAndProfilesForResponseType` for a specific `IControllerResponse`.  This contains all the info about how that `IControllerResponse` can be formatted (including all the profiles).  This is used to [customize the OpenAPI document](#openapi-customizations) as well as the Link system.

### OpenAPI Customizations

We handle customizing the OpenAPI (swagger) document in the "standard" OpenAPI way (spring's `OpenApiCustomizer`).

Please see:

* `OgcApiRecordOpenApiConfigProperties` - add the dynamic properties (for metadata records) to the OpenAPI return object definition 
* `OgcApiRecordsOpenApiConfigMimeTypes` - add the MIME types available as responses to the controller endpoints (based on what `MessageConverter`s are available)


### Other Helpful info

1. [Dynamic Properties in OGCAPI-Records](ogcapi-records-dynamic-properties.md) - Creating a new app (introduction to the App Architecture)
2. [Link Management in OGCAPI-Records](ogcapi-record-link-management.md) - Creating a new app (introduction to the App Architecture)
3. [GN5 Workshops](workshop.md) 

