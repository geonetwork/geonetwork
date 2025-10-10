# Simple Authentication between GN4 and GN5 with JSON

> [!WARNING]  
> If your GN4 is accessible through other means than GN5 (i.e. via the internet or intranet), use the [JWT version](auth-jwt.md).  
> A malicious actor can add a header to the requests and directly communicate with GN4.  GN4 would then trust this header.

### GN5 setup

In `application.yml` (cloud: gateway: mvc), add the "filters" (at the bottom):
```yaml
  cloud:
    gateway:
      mvc:
        routes:
          - id: geonetwork_route
            uri: ${geonetwork.4.url}
            predicates:
              - Path=/geonetwork/**
            filters:
              - addSimpleGn4SecurityHeader=gn5.to.gn4.trusted.json.auth
```
`gn5.to.gn4.trusted.json.auth` is the name of the header that will be attached to the requests so auth is recognized by GN4's JWT Headers Security module (see below).

This header will, typically, look like this:

```json
{
      "username":"david"
}      
```


### GN4 setup

1. You will need the JWT Headers security model (available in the latest GN4)
2. Set it up with the environment variables, see below.
3. Run with JWT Headers enabled `-Dgeonetwork.security.type=gn5`
4. GN4 must **NOT** be accessible other than via the GN4 Gateway proxy.  GN4 is trusting headers so they must be removed.

- If GN4 is available, you **MUST** remove the `gn5.to.gn4.trusted.json.auth` header from incoming requests (typically done with your webserver)

```sh
JWTHEADERS_UserNameHeaderName=gn5.to.gn4.trusted.json.auth
JWTHEADERS_UserNameFormat=JSON
JWTHEADERS_UserNameJsonPath=username
JWTHEADERS_JwtHeaderRoleSource=DB
JWTHEADERS_UpdateProfile=false
JWTHEADERS_UpdateGroup=false
```    

Environment variables meaning:

*  `JWTHEADERS_UserNameHeaderFormat`  - name of the header to use (should be the same as your GN5 `application.yml` filter config)
* `JWTHEADERS_UserNameFormat` - should be JSON (the header is a JSON string)
* `JWTHEADERS_UserNameJsonPath` - where in the JSON is the username
* `JWTHEADERS_JwtHeaderRoleSource` - this should be `DB` (GN DB will manage user-profile-groups)
* `JWTHEADERS_UpdateProfile` -  JWT Headers should **NOT** update the DB profiles (user allowed to edit in GUI)
* `JWTHEADERS_UpdateGroup` -  JWT Headers should **NOT** update the DB user-groups (user allowed to edit in GUI)


See the GN JWT Headers documentation for more info.


In `config-security.properties`, configure the URLs for login and logout to point to GN5:

```properties
logout.success.url=http://localhost:7979/geonetwork/srv/eng/catalog.search
loginForm=http://localhost:7979/geonetwork/srv/eng/catalog.signin
loginErrorForm=http://localhost:7979/geonetwork/srv/eng/catalog.signin?failure=true
```



In UI configuration, you need to enable the authentication and set the signin/signout URLs to use the GN5 URLs:

```json
     "authentication": {
          "enabled": true,
          "signinUrl": "../../{{node}}/{{lang}}/catalog.signin",
          "signinAPI": "../../api/user/signin",
          "signoutUrl": "../../api/user/signout"
```
