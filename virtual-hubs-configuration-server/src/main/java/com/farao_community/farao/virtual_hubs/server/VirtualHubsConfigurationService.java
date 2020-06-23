/*
 * Copyright (c) 2020, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.virtual_hubs.server;

import com.farao_community.farao.virtual_hubs.VirtualHubsConfiguration;
import com.farao_community.farao.virtual_hubs.json.JsonVirtualHubsConfiguration;
import com.farao_community.farao.virtual_hubs.xml.XmlVirtualHubsConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
@Service
public class VirtualHubsConfigurationService {
    private final VirtualHubsConfigurationRepository repository;

    public VirtualHubsConfigurationService(VirtualHubsConfigurationRepository repository) {
        this.repository = repository;
    }

    public void publishVirtualHubsConfiguration(MultipartFile configurationFile, LocalDateTime validFrom, LocalDateTime validTo) {
        VirtualHubsConfigurationRecord record = createNewConfigurationRecord(configurationFile, validFrom, validTo);
        repository.save(record);
    }

    public String getConfigurationAtDate(LocalDateTime date) {
        VirtualHubsConfigurationRecord record = repository.findFirstByValidFromLessThanEqualAndValidToGreaterThanOrderByPublishedOnDesc(date, date);
        return record.getConfigurationJson();
    }

    private VirtualHubsConfigurationRecord createNewConfigurationRecord(MultipartFile configurationFile, LocalDateTime validFrom, LocalDateTime validTo) {
        return new VirtualHubsConfigurationRecord(UUID.randomUUID().toString(), validFrom, validTo, LocalDateTime.now(), getJsonConfigurationFromMultipart(configurationFile));
    }

    private String getJsonConfigurationFromMultipart(MultipartFile configurationFile) {
        try {
            VirtualHubsConfiguration configuration = XmlVirtualHubsConfiguration.importConfiguration(configurationFile.getInputStream());
            return getJsonString(configuration);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String getJsonString(VirtualHubsConfiguration configuration) {
        StringWriter writer = new StringWriter();
        JsonVirtualHubsConfiguration.exportConfiguration(writer, configuration);
        return writer.toString();
    }
}
