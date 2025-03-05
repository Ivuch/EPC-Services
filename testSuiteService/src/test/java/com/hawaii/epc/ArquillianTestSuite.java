package com.hawaii.epc;

import com.hawaii.epc.iosystem.IOSystemService;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.junit.Test;
import org.junit.runner.RunWith;




import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class ArquillianTestSuite {

    @Inject
    private IOSystemService ioSystemService;

    // No Deployment method is needed if using an existing WAR/EAR

    @RunAsClient
    @Test
    public void testListFilesStress() {
        int requestCount = 100; // Set the number of requests for stress testing
        for (int i = 0; i < requestCount; i++) {
            try {
                var files = ioSystemService.listFilesInDirectory("/media/ubuntu/25683426-685a-4ff9-a404-ed0d164f9736/home/Max/work");
                assertNotNull("List of files should not be null", files);
                // Optionally log or process the results
                System.out.println("Request " + i + ": " + files);
            } catch (Exception e) {
                e.printStackTrace(); // Handle exceptions appropriately
            }
        }
    }
}

