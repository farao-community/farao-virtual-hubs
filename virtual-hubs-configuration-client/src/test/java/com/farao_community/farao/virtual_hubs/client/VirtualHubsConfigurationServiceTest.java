package com.farao_community.farao.virtual_hubs.client;

import com.farao_community.farao.virtual_hubs.VirtualHubsConfiguration;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;


class VirtualHubsConfigurationServiceTest {
    @Test
    public void myTest() {
        try (VirtualHubsConfigurationService service = new VirtualHubsConfigurationService("localhost", 8080)) {
            service.publish(getClass().getResourceAsStream("/coresoExampleFile.xml"), LocalDateTime.parse("2016-11-09T11:44:44"), LocalDateTime.parse("2026-11-09T11:44:44"));
            VirtualHubsConfiguration configuration = service.retrieve(LocalDateTime.parse("2020-11-09T11:44:44"));
            System.out.println(configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}