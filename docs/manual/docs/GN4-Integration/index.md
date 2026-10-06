# GeoNetwork 5 Integration with GeoNetwork 4 {#toc}

During the development of GN5, GN4 will be used for some of the GN functionality.  GN5 will route some
requests to a GN4 instance (connected to the same DB and Elastic Index) for processing.


## Setting up GN5 and GN4

1. Configure GN5 (`application.yml`) so that the `cloud: gateway: mvc:` configuration points to your GN4
2. Ensure that GN4 is setup to be proxied:

    a) Go the GN4's settings ("Catalog server" section) <br>
    b) Set this section up so it will create urls that will be correctly routed through GN5 (ie. use GN5's hostname and port)<br>
    c) Your GN4's path should be the same as the GN5's proxy path.  This will ensure that GN4's session cookies are correctly maintained<br>
       For example, GN5's proxy at `http://<host>:<port>/geonetwork` and GN4 at `http://<other host>:<other port>/geonetwork`<br>
       (both at `/geonetwork`)

3. Configure GN4 with JWT Headers Security and add the GN5 `application.yml` `cloud: gateway: mvc:` filter.

    a) [JWT-Based Authentication](auth-jwt.md) - recommended for most installations <br>
    b) [Simple Authentication](auth-json.md) - easier to configure, but GN4 must **not** be accessible via the internet or intranet.<br>
 

You will manage users in the GN database.

## Using GitHub OIDC Authentication

Typically, you would use the standard username/password authentication thats built into GN5.  However, GitHub authentication is also available.

1. Create a GitHub application (this will give you a `clientId`).  Got to https://github.com/settings/profile then "Developer settings" (left column) then "OAuth Apps" (left column).  See online tutorials on how to create one.
    * "Authorization callback URL" -  http://localhost:7979/login/oauth2/code/github (use your GN5's host address)
2. Create a GitHub clientSecret (this will give you a `clientSecret`)
3. Add the `clientId` and `clientSecret` to GN5's `application.yml` section `security: oauth2: client: registration: github:`

    * fill in the  `clientId:` and `clientSecret:` sections
    * choose the attribute name to use as the GeoNetwork username

```yml
 security:
    oauth2:
      client:
        registration:
          github:
            clientId:  ...from your GitHub application...
            clientSecret: ...from your GitHub application...
        # this is optional: 
        provider:
          github:
            user-name-attribute: login
```

You can also customize how the user is created in the GeoNetwork database from attributes in the OIDC/OAUTH2 user from the IDP:

```yml
geonetwork:
  security:
    oauth2:
      userCreationRegistration:
        github:
          email: email
          name: login
          organization: company
          surname: lastname
        custom:
          email: email
          name: first_name
          organization: company
          surname: last_name
```

One the main GN5 (http://localhost:7979) click "GitHub" and you will be redirected for GitHub's authentication.

Once the user logs into GN5, it will create a user in the database.  The username will either be their email (if they have configured GitHub to have their email address public) or their GitHub username.

Use the tools in the GN Administration -> "Users and Groups" to set the user's permissions.
   

## Setting groups and profiles from the token

Instead of assigning groups and profiles by hand, GN5 can set them at each login from a claim of the token (for example `groups`).
This is configured per registration, under the same `userCreationRegistration` key (the id is the one used in `spring.security.oauth2.client.registration`):

```yml
geonetwork:
  security:
    oauth2:
      userCreationRegistration:
        keycloak:
          rolesClaim: groups            # top-level claim, default "groups"
          rolePattern: '^(?<group>[^:]+):(?<profile>[^:]+)$'   # optional
          roleMappings:                 # optional
            - claimValue: catalogue-editors
              group: DRAFTS             # no group = global profile
              profile: Editor           # default Editor
            - claimValue: catalogue-editors   # same value, another target
              group: SAMPLE
              profile: Reviewer
          createMissingGroups: false
          updateGroup: false
```

* `roleMappings` maps an exact claim value to a group and profile. Repeat a `claimValue` to give several group/profile pairs.
* `rolePattern` is a regular expression matched against the whole claim value. The named group `profile` is required, the named group `group` is optional.
  Both sources can be used together, and the results add up.
* Without `rolePattern` and `roleMappings` nothing changes. If the claim is missing from the token, the user is not modified (unless `updateGroup` is true, see below).
* The user's global profile is raised to the highest mapped profile (GN4 reads it from the database). A mapped `Administrator` on a group is stored as `UserAdmin`, and a `Reviewer` on a group also gets the `Editor` row, as GN4 does.
* `Monitor` is not part of the profile hierarchy and only applies globally: it is ignored on a group, and as a global profile it only replaces `RegisteredUser` or `Guest`. Any other mapped profile replaces `Monitor`, and `Administrator` already includes it.
* A user disabled in GeoNetwork cannot sign in with OAuth2 either, and their groups and profile are not modified.
* Groups that do not exist are skipped with a warning, unless `createMissingGroups` is true.
* With `updateGroup: true` the identity provider is the only source: at each login the user's groups and profile are replaced by the mapped ones, **administrators included**. A token without the claim counts as "no groups", so the user loses all groups and drops to `RegisteredUser`.
  Map an administrator role first, and keep a local (form login) administrator account as the way back in.
* Permissions are applied when the user logs in, and the resulting authority is kept in the session. GN5 also caches it per username for 5 minutes, so a user who logs in again within that time can still get the previous permissions.
  Role changes in the identity provider, **demotions with `updateGroup` included**, only apply to the next login after the cache has expired.

!!! warning

    With `rolePattern` and `createMissingGroups` together, whoever names roles in the identity provider can create catalogue groups.
