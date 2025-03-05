package com.hawaii.epc;



import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import jakarta.security.enterprise.identitystore.openid.OpenIdClaims;
import jakarta.security.enterprise.identitystore.openid.OpenIdContext;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static jakarta.security.enterprise.identitystore.IdentityStore.ValidationType.PROVIDE_GROUPS;

@Slf4j
@ApplicationScoped
public class AuthorizationIdentityStore implements IdentityStore {

    @Inject
    private GoogleOpenIdContext authContext;

    @Override
    public Set<ValidationType> validationTypes() {
        return EnumSet.of(PROVIDE_GROUPS);
    }


    @Override
    public Set<String> getCallerGroups(CredentialValidationResult result) {
        EPCOpenIdClaims claims = authContext.getClaims();
        Set<String> roles = Collections.emptySet();

        // Retrieve the user's claims from Google
        JsonObject claimsJson = authContext.getClaimsJson();
        Optional<JsonObject> subject = Optional.ofNullable(claimsJson);

        if (subject.isPresent() && subject.get().getString("sub").equals(result.getCallerPrincipal().getName())) {
            log.debug("Getting subject from auth: {}", subject);
            JsonObject attributes = subject.get();

            log.debug("Found user attributes: {}", attributes);


            // Retrieve roles from the "groups" or similar claim

            Optional<String> allRoles = Optional.ofNullable(attributes.getString("groups", null));
            if (allRoles.isPresent()) {
                roles = Stream.of(allRoles.get().split(",")).collect(Collectors.toSet());
                log.debug("Roles: {}", roles);
            }
           // if (allRoles != null) {
                //For allRoles type String   roles.addAll(Arrays.asList(allRoles.split(",")));
        //    }
        else {
                roles.add("GUEST");
            }
            return roles;
        }
        return roles;
    }
/*
    public Set<String> getCallerGroups2(CredentialValidationResult result) {
        Set<String> roles = Collections.emptySet();

        // Retrieve the user's claims from Google
        JsonObject claimsJson = authContext.getClaimsJson();
        Optional<JsonObject> subject = Optional.ofNullable(claimsJson);

        if (subject.isPresent() && subject.get().getString("sub").equals(result.getCallerPrincipal().getName())) {
            log.debug("Getting subject from auth: {}", subject);
            JsonObject attributes = subject.get();

            log.debug("Found user attributes: {}", attributes);

            // Google does not directly provide "roles" claims, so you may need to manage roles on your end
            Optional<String> allRoles = Optional.ofNullable(attributes.getString("groups", null));
            if (allRoles.isPresent()) {
                roles = Stream.of(allRoles.get().split(",")).collect(Collectors.toSet());
                log.debug("Roles: {}", roles);
            }
        }
        return roles;
    }
    */

}