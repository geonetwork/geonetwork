# GeoNetwork frontend

GeoNetwork front end is a web application that provides a user interface to search and view metadata records stored in a GeoNetwork instance.
It is composed of 4 projects:

* `gapi` - GeoNetwork Open API client automatically generated from the OpenAPI specification of the GeoNetwork REST API.
* `glib` - GeoNetwork component library that provides reusable components for the GeoNetwork front end.
* `gapp` - GeoNetwork front end application that uses the `gapi` and `glib` projects.
* `gwebcomponents` - GeoNetwork web components that provide reusable web components.

## Requirements

* Install [GeoNetwork](https://docs.geonetwork-opensource.org/4.4/install-guide/) 
* Install [nvm](https://github.com/nvm-sh/nvm?tab=readme-ov-file#installing-and-updating)
* Install [Node.js](https://nodejs.org/en/download/) using nvm

```bash
nvm install 18.19
```

## Build and run

* Fetch all project dependencies

```bash
npm install
```

* Install Angular CLI

```bash
 npm install -g @angular/cli
 ```

* Build all

```bash
npm run build-all
```

* Run the application

```bash
npm run start
```

Once started the application is available at http://localhost:4200/.


## Development

While working on the main app, you can run the following command to watch the `glib` project and rebuild it automatically
in order to  change components and automatically refresh the main app.

```bash
npm run start
npm run watch-lib-glib 
```

To work on the webcomponents, use

```bash
npm run start-webcomponents
npm run watch-lib-glib
```

See [package.json](package.json) for more details.


## Projects

### Main dependencies

* [PrimeNG](https://primeng.org/) is used for the UI components.
* [Tailwind CSS](https://tailwindcss.com/) is used for the styling.
* [FortAwesome](https://fontawesome.com/) is used for the icons.
* [Elasticsearch search API](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-with-elasticsearch.html)

### The OpenAPI clients

One for GeoNetwork 4 in `gapi` project, the [OpenAPI specification](projects/gapi/gapi.json) can be updated from http://localhost:8080/geonetwork/srv/api/doc.
One for GeoNetwork 5 in `g5api` project, the [OpenAPI specification](projects/g5api/g5api.json) can be updated from http://localhost:8080/geonetwork/v3/api-docs.

To update run the following commands:

```bash
curl http://localhost:8080/geonetwork/srv/api/doc > projects/gapi/gapi.json
npm run openapi-generator-g4api
curl http://localhost:8080/geonetwork/v3/api-docs > projects/g5api/g5api.json
npm run openapi-generator-g5api
npm run prettier
```
Java is required to build the OpenAPI client with [OpenAPI Generator](https://github.com/OpenAPITools/openapi-generator).


Items to be improved in the OpenAPI specification:
* in GeoNetwork 5, the IndexRecord model does not contain the `additionalProperties` field (cf. [commit](https://github.com/geonetwork/geonetwork/commit/afb1b923befd2564dad349b6982beb87a3b35901#diff-d9cfcd6cc9d2800df4ef55f435ed0b0a93d4fcc8c97f2cd8a7bfa177cf6ed799L36)). Update the model in the OpenAPI specification `g5api.json` before generating the client.

```json
      "IndexRecord": {
         "type": "object",
         "additionalProperties": true,
```

* in [projects/gapi/src/lib/apis/SearchApi.ts](projects/gapi/src/lib/apis/SearchApi.ts), Elasticsearch types are manually added in order to have a better typing for search requests and responses.
* some errors in the generated code to fix manually
    * enum issue (eg. PutResourceVisibilityEnum)
    * runtime.js override
```ts
override name: 'FetchError' = 'FetchError';
   constructor(
     public override cause: Error,
     msg?: string
   ) {
     super(msg);
   }
```
  * `headerParameters['Accept'] = 'application/json';` to support `Accept: */*` related to Spring error `Could not find acceptable representation`
* [projects/gapi/src/lib/ui-settings.ts](projects/gapi/src/lib/ui-settings.ts) is the model of the UI settings (to be created server side)

To configure and use the OpenAPI client, use the following code (eg. [in the search service](projects/glib/src/lib/search/search.service.ts)):

```typescript
apiConfiguration = inject(API_CONFIGURATION);

searchApi = computed(() => {
  return new SearchApi(this.apiConfiguration());
});

search(searchRequestParameters: SearchRequestParameters) {
  return this.searchApi().search({
    body: this.buildQuery(searchRequestParameters),
  });
}
```

### The component library

The library is organized [per features](projects/glib/src/lib) (eg. search, record, etc.) and provides reusable components.

State management is done using [signalStore](https://ngrx.io/guide/signals/signal-store) (eg. [search state](projects/glib/src/lib/search/search.state.ts)) which facilitate the communication between components.


#### Components for searches

The search components are organized in the [search](projects/glib/src/lib/search) feature.
User can easily register a search in an application using `gSearchContext`: 

```html
<div
  gSearchContext="highlights"
  size="5"
  [sort]="[{ changeDate: 'desc' }, '_score']">
```

Then interacting with aggregation or search results can be done using the reference of the search context:

* Displaying results as a timeline:
 
```html
<g-search-results-timeline scope="highlights" />
```

* Displaying aggregations, paging and results as a list:

```html
<div class="basis-1/4">
  <g-search-aggs-container scope="main"/>
</div>
<div class="basis-3/4">
  <g-search-paging scope="main" />
  <g-search-results scope="main" />
</div>
```

* Adding a search filter:

```html
 <input gSearchQuerySetter="main" pInputText placeholder="Search ..." />
```

* Displaying an aggregation:
  
```html
<g-search-agg aggregation="cl_topic" scope="main" />
```

If the requested aggregation is not available in the search context, it will be automatically configured to be requested from the server (as a term aggregation).



### The front end application

The front end application is using GeoNetwork user interface configuration managed in the admin console.
A [default configuration](projects/glib/src/lib/config/constants.ts) is used if not configured in the admin console.

First step is [loading the UI configuration from the server](projects/glib/src/lib/config/config.loader.ts) which is done by the main application [here](projects/gapp/src/main.ts).

The front end application is providing the following features:
* home page
* search
* map
* record view
* record authoring
* admin console


### The web components

The web components are used to provide reusable components that can be used in any third party website.

Web components prefix is `gc-`.

Web components are available in [projects/gwebcomponents/src/app/components](projects/gwebcomponents/src/app/components) and initialized in [projects/gwebcomponents/src/app/app.module.ts](projects/gwebcomponents/src/app/app.module.ts).

Then they can be used in any website by:
* importing the library
* importing the styling
* adding the web component in the HTML

```html
<script src="https://apps.titellus.net/webcomponents/main.js" type="module"></script>
<link rel="stylesheet" href="https://apps.titellus.net/webcomponents/styles.css" media="all" onload="this.media='all'">
<gc-search-results-table-component
          api-url="https://metawal.wallonie.be/geonetwork/srv/api"
          filter="+th_high-value-dataset-category.link:* +isTemplate:n +isPublishedToAll:true -resourceType:service -resourceType:application"
          size="15"
          list-of-label="Aperçu,Titre,Type,Catégories HVD,Législations,Status,API,Organisme"
          list-of-field="overview[*],resourceTitleObject.default,resourceType[0],th_high-value-dataset-category[*],th_high-value-dataset-applicable-legislation[*],cl_status[0].default,link[?(@.protocol == 'OGC:WMS' || @.protocol == 'OGC:WFS' || @.protocol == 'ESRI:REST' || @.protocol == 'atom:feed')],ownerOrgForResource_tree"
          list-of-filter="resourceType,th_high-value-dataset-category.default,th_high-value-dataset-applicable-legislation.default,cl_status.default,ownerOrgForResourceObject.default"
          landing-page="https://geodata.wallonie.be/id/${uuid}"
          landing-page-link-on="resourceTitleObject.default"
          scroll-height="80vh"></gc-search-results-table-component>
</div>
```

## Running unit tests

Run `ng test` to execute the unit tests - work in progress -.
Run `ng e2e` to execute the end-to-end tests using [cypress](https://www.cypress.io/) - work in progress -.

## Internationalization

To be done.

## Deployment

On Apache, the following configuration can be used to redirect all requests to the index.html file.
Install Apache with `mod_rewrite` enabled:

```bash 
sudo apt install apache2
sudo a2enmod rewrite
sudo vi /etc/apache2/sites-available/000-default.conf
```

`AllowOverride` for configuration using `.htaccess` files:

```
<Directory /var/www/html>
    AllowOverride All
</Directory>
```

Create a `.htaccess` file in the `/var/www/html` directory:

```bash
vi /var/www/html/.htaccess
```

with the following redirection:

```
RewriteEngine on
RewriteCond %{REQUEST_FILENAME} -s [OR]
RewriteCond %{REQUEST_FILENAME} -l [OR]
RewriteCond %{REQUEST_FILENAME} -d
RewriteRule ^.*$ - [NC,L]

RewriteRule ^(.*) /index.html [NC,L]
```

For SpringBoot app, the following configuration can be used to redirect all requests to the index.html file (see https://github.com/GeoCat/geonetwork/blob/frontend-exp/src/main/java/org/geonetwork/config/MvcConfiguration.java).


# Development

## New component

All components are standalone and can be generated using the following command:

```bash
ng generate component auth/signin-form --standalone --project=glib
```

Once created expose the new component in the `public-api.ts` file.

```typescript
export * from './lib/auth/signin-form/signin-form.component';
```

And use it in your application.

```html
<g-signin-form/>
```


## Common issues

### Circular dependency

```
File change detected. Starting incremental compilation...
ERROR: Entry point glib has a circular dependency on itself.
```

Search for `from 'glib'` in the code to find the import using the lib name instead of relative path.

### Proxy 

```
8:53:03 AM [vite] http proxy error: /geonetwork/srv/api/search/records/_search
Error: connect ECONNREFUSED 127.0.0.1:8080
    at TCPConnectWrap.afterConnect [as oncomplete] (node:net:1549:16) (x4)
```

GeoNetwork is not running or the proxy is not correctly configured.

 
