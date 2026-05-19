# Database module

[Liquibase](https://www.liquibase.com/) is used to manage database schema creation and changes on a per-feature basis.

Documentation about Liquibase can be found in the [Liquibase documentation](https://docs.liquibase.com/oss/implementation-guide-4-33/intro-to-liquibase).

## Database initialization

GeoNetwork 5 depends on the GeoNetwork 4 database module to initialize the database using Liquibase.

Additional changes made by GeoNetwork 5 should be added in a new GeoNetwork 5 database module, 
which will contain [changesets specific to GeoNetwork 5](src/main/resources/db/changesets).

Both GeoNetwork 4 and GeoNetwork 5 can create or migrate the database.
