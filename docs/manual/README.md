# GeoNetwork Manual and Help

Documentation for GeoNetwork opensource is available via https://docs.geonetwork-opensource.org.

This documentation is written under the Creative Commons license [Attribution-ShareAlike 3.0 Unported (CC BY-SA 3.0)](LICENSE).

## Material for MkDocs

Documentation is [MkDocs-material](https://squidfunk.github.io/mkdocs-material/) which is a Markdown documentation framework written on top of [MkDocs](https://www.mkdocs.org/).

1. Install virtual environment:

   ```bash
   virtualenv venv
   ```

2. Activate virtual environment and install (or update) requirements:
   ```bash
   source venv/bin/activate
   pip install -r requirements.txt
   ```
   
3. Use ***mkdocs*** to preview from virtual environment:

   ```bash
   mkdocs serve
   ```

3. Preview: http://localhost:8000

   Preview uses a single version, so expect some warnings from version chooser:
   ```
   "GET /versions.json HTTP/1.1" code 404
   ```

4. Optional: Preview online help:
   
   ```bash
   mkdocs serve --config-file help.yml  
   ```

## Maven Integration

1. Build documentation with ``compile`` phase:
   ```
   mvn compile
   ```

2. Assemble ``zip`` with ``package`` phase:
   ```bash
   mvn package
   ```

3. Both ``install`` and ``deploy`` are skipped (so ``mvn clean install`` is fine).

4. Use default profile to only build the default English docs:

   ```
   mvn install -Pdefault
   ```
   
## Publish Documentation

We use ``mike`` for publishing (from the `gh-pages` branch). Docs are published by the ``.github/workflows/docs.yml`` automation each time pull-request is merged.

If you wish to preview using your own `gh-pages` branch:

1. To deploy 5.0 docs as latest from the `main` branch to website `gh-pages` branch:

   ```bash
   mike deploy --title "5.0 Latest" --alias-type=copy --update-aliases 5.0 development
   ```
 
2. When starting a new branch you can make it the default:
   
   ```bash
   mike set-default --push 5.1
   ```
    
   Hint: When starting a new branch update `overview/changelog/history/index.md` headings for latest, maintenance, stable (for prior branches also).

3. To publish documentation for a maintenance release:

   ```bash
   mike deploy --push --alias-type=copy 3.12 maintenance
   ```

4. To show published versions:

   ```bash
   
   mike list
   ```

5. To preview things locally (uses your local ``gh-pages`` branch):
   
   ```bash
   mike serve
   ```

Reference:

* https://squidfunk.github.io/mkdocs-material/setup/setting-up-versioning/
* https://github.com/squidfunk/mkdocs-material-example-versioning
* https://github.com/jimporter/mike
