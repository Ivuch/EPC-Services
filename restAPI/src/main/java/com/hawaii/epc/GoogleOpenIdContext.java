package com.hawaii.epc;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.json.*;
import jakarta.security.enterprise.identitystore.openid.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@ApplicationScoped
@Path("/hello")
public class GoogleOpenIdContext implements OpenIdContext {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String tokenUri = "https://oauth2.googleapis.com/token";
    private String userInfoUri = "https://openidconnect.googleapis.com/v1/userinfo";



    private Map<String, String> claims = new HashMap<>();
    private EPCOpenIdClaims epcOpenIdClaims;
    private JsonObject claimsJson;

    public GoogleOpenIdContext() {
        // Load client ID, client secret, and redirect URI from config or environment variables
        clientId = "falseclient";
        clientSecret = "falsesecret";
        redirectUri = "https://localhost:8443/restAPI/hello";
    }

    public void login() {
        String authUrl = "https://accounts.google.com/o/oauth2/v2/auth" +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&scope=openid%20email%20profile";
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(authUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handle Google callback and exchange the authorization code for tokens
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void handleCallback(@QueryParam("code") String authorizationCode,
                               @Context HttpServletRequest request,
                               @Context HttpServletResponse response ) {
            log.info("codee");
        if (authorizationCode != null) {
            // Exchange authorization code for access and ID tokens
            log.info(authorizationCode);
            authenticate(authorizationCode, request);
        }

        try {
           
            response.sendRedirect(request.getContextPath() + "/login.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void authenticate(String authorizationCode, HttpServletRequest request) {
        // Step 1: Exchange authorization code for access token and ID token
        Client client = ClientBuilder.newClient();
        Form form = new Form()
                .param("code", authorizationCode)
                .param("client_id", clientId)
                .param("client_secret", clientSecret)
                .param("redirect_uri", redirectUri)
                .param("grant_type", "authorization_code");

        Response tokenResponse = client
                .target(tokenUri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.form(form));

        if (tokenResponse.getStatus() == 200) {
            JsonObject tokenJson = tokenResponse.readEntity(JsonObject.class);
            String accessToken = tokenJson.getString("access_token");

            // Step 2: Use the access token to fetch user info
            Response userInfoResponse = client
                    .target(userInfoUri)
                    .request(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + accessToken)
                    .get();






            //////////////////


            if (userInfoResponse.getStatus() == 200) {
                String userInfo = userInfoResponse.readEntity(String.class);
                //.replace("\"","");

                JsonReader jsonReader = Json.createReader(new StringReader(userInfo));
                this.claimsJson = jsonReader.readObject();
                populateClaims(this.claimsJson);

                //this.claims.replace("\"","");
            }



            HttpSession session =  request.getSession(true);
            session.setAttribute("claims", this.claims);

        }
        client.close();
    }


    public void populateClaims(JsonObject jsonClaims)

    {
        for (Map.Entry<String, JsonValue> entry : jsonClaims.entrySet()) {
            String key = entry.getKey();
            String value;

            // Handle JsonString to avoid extra quotes
            if (entry.getValue().getValueType() == JsonValue.ValueType.STRING) {
                value = ((JsonString) entry.getValue()).getString();
            } else {
                // Fallback to toString() for non-string values
                value = entry.getValue().toString();
            }

            // Add each claim to the Map without additional processing
            this.claims.put(key, value);
        }
        epcOpenIdClaims = new EPCOpenIdClaims(this.claims);
        epcOpenIdClaims.saveToProfile();
    }

    @Override
    public String getSubject() {
        return this.claims.get("sub");
    }

    @Override
    public String getTokenType() {
        return "";
    }

    @Override
    public AccessToken getAccessToken() {
        return null;
    }

    @Override
    public IdentityToken getIdentityToken() {
        return null;
    }

    @Override
    public Optional<RefreshToken> getRefreshToken() {
        return Optional.empty();
    }

    @Override
    public Optional<Long> getExpiresIn() {
        return Optional.empty();
    }

    @Override
    public JsonObject getClaimsJson() {
        return claimsJson;
    }

    @Override
    public EPCOpenIdClaims getClaims() {
        return epcOpenIdClaims;
    }

    @Override
    public JsonObject getProviderMetadata() {
        return null;
    }

    @Override
    public <T> Optional<T> getStoredValue(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String s) {
        return Optional.empty();
    }
}


