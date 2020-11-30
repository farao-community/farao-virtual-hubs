/*
 * Copyright (c) 2020, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.virtual_hubs.network_extension;

import com.powsybl.commons.extensions.ExtensionAdder;
import com.powsybl.iidm.network.Injection;

/**
 * @author Baptiste Seguinot {@literal <baptiste.seguinot@rte-france.com>}
 */
public interface AssignedVirtualHubAdder<T extends Injection<T>> extends ExtensionAdder<T, AssignedVirtualHub<T>> {

    @Override
    default Class<AssignedVirtualHub> getExtensionClass() {
        return AssignedVirtualHub.class;
    }

    AssignedVirtualHubAdder withCode(String code);

    AssignedVirtualHubAdder withEic(String eic);

    AssignedVirtualHubAdder withMcParticipant(boolean isMcParticipant);

    AssignedVirtualHubAdder withNodeName(String nodeName);

    AssignedVirtualHubAdder withRelatedMa(String relatedMa);
}
