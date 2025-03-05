package com.hawaii.epc;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
-+
@ExtendWith(ArquillianExtension.class)
public class ProfileTableTest {

    @Resource(name = "java:comp/env/jdbc/testDB")
    private DataSource dataSource;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml");
    }

    @Test
    public void testProfileAndSecurityTables() throws Exception {
        String testUuid = "123e4567-e89b-12d3-a456-426614174000";

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement()) {

            // Inserción en la tabla PROFILE
            String insertProfileSQL = """
                INSERT INTO PROFILE (uuid, sub, name, given_name, family_name, picture)
                VALUES ('123e4567-e89b-12d3-a456-426614174000', 'sub-123', 'John Doe', 'John', 'Doe', 'http://example.com/pic.jpg')
            """;
            stmt.executeUpdate(insertProfileSQL);

            // Inserción en la tabla SECURITY
            String insertSecuritySQL = String.format("""
                INSERT INTO SECURITY (uuid, expires)
                VALUES ('%s', '%s')
            """, testUuid, Timestamp.from(Instant.now().plusSeconds(30 * 60)));
            stmt.executeUpdate(insertSecuritySQL);

            // Verificar datos en PROFILE
            ResultSet rsProfile = stmt.executeQuery("SELECT * FROM PROFILE WHERE uuid = '" + testUuid + "'");
            assertNotNull(rsProfile);
            if (rsProfile.next()) {
                assertEquals(testUuid, rsProfile.getString("uuid"));
                assertEquals("sub-123", rsProfile.getString("sub"));
                assertEquals("John Doe", rsProfile.getString("name"));
                assertEquals("John", rsProfile.getString("given_name"));
                assertEquals("Doe", rsProfile.getString("family_name"));
                assertEquals("http://example.com/pic.jpg", rsProfile.getString("picture"));
            } else {
                throw new AssertionError("No data found in PROFILE table.");
            }

            // Verificar datos en SECURITY
            ResultSet rsSecurity = stmt.executeQuery("SELECT * FROM SECURITY WHERE uuid = '" + testUuid + "'");
            assertNotNull(rsSecurity);
            if (rsSecurity.next()) {
                assertEquals(testUuid, rsSecurity.getString("uuid"));
                assertNotNull(rsSecurity.getTimestamp("expires"));
            } else {
                throw new AssertionError("No data found in SECURITY table.");
            }
        }