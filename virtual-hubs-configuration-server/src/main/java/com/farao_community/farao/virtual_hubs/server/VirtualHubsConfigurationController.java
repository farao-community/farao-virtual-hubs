/*
 * Copyright (c) 2020, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.virtual_hubs.server;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

/**
 * @author Sebastien Murgey {@literal <sebastien.murgey@rte-france.com>}
 */
@RestController
@RequestMapping("/virtual-hubs-configuration")
public class VirtualHubsConfigurationController {
    private final VirtualHubsConfigurationService service;

    public VirtualHubsConfigurationController(VirtualHubsConfigurationService service) {
        this.service = service;
    }

    @PostMapping("/publish")
    public void publishVirtualHubsConfiguration(MultipartFile configurationFile, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime validFrom, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime validTo) {
        service.publishVirtualHubsConfiguration(configurationFile, validFrom, validTo);
    }

    @GetMapping()
    public String getConfigurationAtDate(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime instant) {
        return service.getConfigurationAtDate(instant);
    }
}
