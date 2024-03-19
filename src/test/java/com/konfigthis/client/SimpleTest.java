package com.konfigthis.client;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;

public class SimpleTest {
    final String MOCK_SERVER_URL = "http://localhost:4010";

    @Test
    public void initClientTest() {
        Configuration configuration = new Configuration();
        configuration.host = "https://api.digitalocean.com";
        
        // Configure HTTP bearer authorization: bearer_auth
        configuration.token = "BEARER TOKEN";
        DigitalOcean client = new DigitalOcean(configuration);
        assertNotNull(client);
    }
}
