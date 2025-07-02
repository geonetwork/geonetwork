Security inside GN5
-------------------

NOTE: this is about the internal GN5 security.  See the GN4-Integration for detail on the GN5->GN4 proxy security.

Overview
--------

1. Use `AuthenticationFacade` for most of your authentication needs
2. `AuthenticationFacade#geonetworkPermissions()` has most of the permissions you will need.  It is built when a user logs in.  It contains;

    a. Username

    b. UserId (as per GN database)

    c. HighestProfile (ie. GN database's Profile)

    d. Map that goes from Profile -> List of group ids the user has that profile for
    
    e. Map that goes from Group id -> List of profiles that the user has for that profile

    NOTE: for anonymous user, they do not have a username/userid or permissions.  They only have `Profile.Guest` as their highest profile.

3. You shouldn't be accessing `Usergroups` or the `UsergroupRepository` - use the  `AuthenticationFacade` permissions.
4. You shouldn't be doing anything with `"ROLE_*"` type roles - use the  `AuthenticationFacade` permissions.