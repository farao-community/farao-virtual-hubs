/*
 * Copyright (c) 2020, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.virtual_hubs.server;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDateTime;

/**
 * @author Sebastien Murgey {@literal <sebastien.murgey@rte-france.com>}
 */
@Entity
public class VirtualHubsConfigurationRecord {
    @Id
    private String id;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private LocalDateTime publishedOn;
    @Lob
    private String configurationJson;

    public VirtualHubsConfigurationRecord() {

    }

    public VirtualHubsConfigurationRecord(String id, LocalDateTime validFrom, LocalDateTime validTo, LocalDateTime publishedOn, String configurationJson) {
        this.id = id;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.publishedOn = publishedOn;
        this.configurationJson = configurationJson;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }

    public LocalDateTime getPublishedOn() {
        return publishedOn;
    }

    public String getConfigurationJson() {
        return configurationJson;
    }
}
