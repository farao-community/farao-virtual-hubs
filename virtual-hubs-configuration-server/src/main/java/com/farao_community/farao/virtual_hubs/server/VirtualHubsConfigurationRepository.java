/*
 * Copyright (c) 2020, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.virtual_hubs.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * @author Sebastien Murgey {@literal <sebastien.murgey at rte-france.com>}
 */
@Repository
public interface VirtualHubsConfigurationRepository extends JpaRepository<VirtualHubsConfigurationRecord, String> {
    VirtualHubsConfigurationRecord findFirstByValidFromLessThanEqualAndValidToGreaterThanOrderByPublishedOnDesc(LocalDateTime date1, LocalDateTime date2);
}
