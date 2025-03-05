package com.hawaii.epc;

import jakarta.security.enterprise.identitystore.openid.Claims;
import jakarta.security.enterprise.identitystore.openid.OpenIdClaims;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class EPCOpenIdClaims implements OpenIdClaims {

    private UUID uuid;
    private Map<String, String> claimsMap;

    public EPCOpenIdClaims(Map<String, String> claimsMap) {
        this.claimsMap = claimsMap;
        generateUniqueId();
        startSecurity();
    }

    private void generateUniqueId() {
       this.uuid = UUID.randomUUID();
    }

    public String getUUID(){ return this.uuid.toString(); }

    public Instant getSessionExpiration(){
       return Instant.now().plus(30, ChronoUnit.MINUTES);
    }

    @Override
    public String getSubject() {
        return claimsMap.get("sub");
    }

    public Optional<String> getClaim(String name) {
        return Optional.ofNullable(claimsMap.get(name));
    }

    // You can add more methods to retrieve other claims if needed
    public Optional<String> getName() {
        return Optional.ofNullable(claimsMap.get("name"));
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(claimsMap.get("email"));
    }

    @Override
    public Optional<String> getStringClaim(String s) {
        return Optional.empty();
    }

    @Override
    public Optional<Instant> getNumericDateClaim(String s) {
        return Optional.empty();
    }

    @Override
    public List<String> getArrayStringClaim(String s) {
        return List.of();
    }

    @Override
    public OptionalInt getIntClaim(String s) {
        return OptionalInt.empty();
    }

    @Override
    public OptionalLong getLongClaim(String s) {
        return OptionalLong.empty();
    }

    @Override
    public OptionalDouble getDoubleClaim(String s) {
        return OptionalDouble.empty();
    }

    @Override
    public Optional<Claims> getNested(String s) {
        return Optional.empty();
    }

    public void saveToProfile() {
        String insertSQL = "INSERT INTO hawaii.PROFILE (uuid, sub, name, given_name, family_name, picture) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = getDataSourceConnection();
             PreparedStatement stmt = con.prepareStatement(insertSQL)) {

            stmt.setString(1, getUUID());
            stmt.setString(2, getSubject());
            stmt.setString(3, getName().orElse(null));
            stmt.setString(4, getClaim("given_name").orElse(null));
            stmt.setString(5, getClaim("family_name").orElse(null));
            stmt.setString(6, getClaim("picture").orElse(null));

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Profile successfully inserted!");
            } else {
                System.out.println("Failed to insert profile.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error while saving profile: " + e.getMessage());
        }
    }

    private Connection getDataSourceConnection() throws Exception {
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/hawaii");
        return ds.getConnection();
    }

    private void startSecurity() {
        String insertSQL = "INSERT INTO hawaii.SECURITY (uuid, expires) " +
                "VALUES (?, ?)";

        try (Connection con = getDataSourceConnection();
             PreparedStatement stmt = con.prepareStatement(insertSQL)) {

            stmt.setString(1, getUUID());
            stmt.setTimestamp(2, java.sql.Timestamp.from(getSessionExpiration()));

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Security successfully inserted!");
            } else {
                System.out.println("Failed to insert security.");

            }
        }
        catch (Exception e) {
        e.printStackTrace();
        System.err.println("Error while saving security: " + e.getMessage());
    }
    }


}
