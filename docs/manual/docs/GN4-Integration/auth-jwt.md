# Secure Authentication between GN4 and GN5 with JWT


## Securing GN5 to GN4 with signed JWT


A more secure way to secure send information to GN4 is to use signed JWT.  GN4 will verify they are correctly signed and prevent a malicious actor from faking a GN4 authentication token.

To set this up:

1. Generate an RSA private and public key
2. Create a JWK Set containing the public key
3. Configure GN5
4. Configure GN4

### Generate an RSA private and public key

```
openssl req -x509 -sha256 -nodes -days 365 -newkey rsa:2048 -keyout private.key -out certificate_pub.crt
```

This will generate:

1. `private.key` - use this in GN5.  Put this where it is accessible to GN5.  This **must** be secured since the private key is sensitive.
2. `certificate_pub.crt` - use this in the next step for GN4.  Put this where it is accessible to GN4.

### Create a JWK Set containing the public key

The standard way of handling JWT signature validation is to use a JWK Set.  This is fairly easy to do.

1. Use [https://jwkset.com/generate](https://jwkset.com/generate) to convert your `certificate_pub.crt` to a JWK.<br>
  a. copy the text from `certificate_pub.crt` into the first section - "`PEM encoded key or certificate`" <br>
  b. choose a `Key ID` (you'll need this, below)<br>
  c. `Key Algorithm` is "RSA256"<br>
  d. `Key Use` is "Signature"<br>
  e. Press "Generate"
2. Create a file using the output JWT in the following format:
```
{
	"keys": [
		 ... TEXT FROM ABOVE ...
	]
}
```
3. Save the file and make it accessible by GN4

NOTE: There are other online/offline tools that do this.

### Configure GN5

In `application.yml`:

```
  cloud:
    gateway:
      mvc:
        routes:
         ...
            filters:
              - addSimpleJwtGn4SecurityHeader=gn5.to.gn4.trusted.json.auth,file:///Users/db/delme/key/private.key,mykeyid
```

The parameters to the `addSimpleJwtGn4SecurityHeader` filter are: <br>
a) name of the header to use (must be the same as the GN4 configuration)<br>
b) URL to the private key (`private.key` generated above)<br>
c) Name of the `Key ID` (from above)

### Configure GN4

Sent environment variables like this;

```
JWTHEADERS_ValidateTokenAudienceClaimValue=g4.from.g5.proxy
JWTHEADERS_ValidateTokenAudience=true
JWTHEADERS_ValidateTokenAudienceClaimName=aud
JWTHEADERS_ValidateTokenExpiry=true
JWTHEADERS_ValidateToken=true
JWTHEADERS_ValidateTokenSignatureURL=file:///Users/db/delme/jws.json
JWTHEADERS_UpdateProfile=false
JWTHEADERS_UpdateGroup=false
JWTHEADERS_RolesHeaderName=gn5.to.gn4.trusted.json.auth
JWTHEADERS_UserNameFormat=JWT
JWTHEADERS_JwtHeaderRoleSource=DB
JWTHEADERS_UserNameJsonPath=username
JWTHEADERS_ValidateTokenAgainstURL=false
JWTHEADERS_UserNameHeaderName=gn5.to.gn4.trusted.json.auth
```

#### Audience Validation

This audience is put into the JWT by GN5.  It's optional (but recommended) to validate it.

`JWTHEADERS_ValidateTokenAudienceClaimValue=g4.from.g5.proxy`
`JWTHEADERS_ValidateTokenAudience=true`
`JWTHEADERS_ValidateTokenAudienceClaimName=aud`

#### Token Expiry

Ensure the token hasn't expired yet.

`JWTHEADERS_ValidateTokenExpiry=true`

#### Validate Signature

This should be URL to the JWK Set you created, above.  
NOTE: see below if you are using a file instead of a https location

`JWTHEADERS_ValidateTokenSignatureURL=file:///Users/db/delme/jws.json`

#### Other configuration

This is the meaning of the other configuration options:

`JWTHEADERS_UserNameFormat=JWT` -- expect identity information to be in a JWT

`JWTHEADERS_UserNameJsonPath=username` -- json path (inside the JWT payload) of the username
`JWTHEADERS_ValidateTokenAgainstURL=false` -- do not validate the token against the IDP
`JWTHEADERS_UserNameHeaderName=gn5.to.gn4.trusted.json.auth`- name of the request header containing the JWT.  Must be the same as the GN5 configuration.

`JWTHEADERS_UpdateProfile=false` - manage user permissions and groups in the DB
`JWTHEADERS_UpdateGroup=false`- manage user permissions and groups in the DB
`JWTHEADERS_JwtHeaderRoleSource=DB` - manage user permissions and groups in the DB

#### JWK Set in File

The older implementation of JWT Headers isn't compatible with file-base URLs for the JWKSet.  This is fixed, but it will take a while for this to become "official" through the build chains.

Use `python` to serve your JWK Set file;

1. make a directory and put the JWK Set json file in it
2. run:
   `python3 -m http.server 9000`
3. Use "http://localhost:9000/jwksets.json" as the JWK Set url

See the python documentation for security related to doing this.
