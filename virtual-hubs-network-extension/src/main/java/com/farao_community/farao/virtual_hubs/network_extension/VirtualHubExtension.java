/*
 * Copyright (c) 2020, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.virtual_hubs.network_extension;

import com.powsybl.commons.extensions.AbstractExtension;
import com.powsybl.iidm.network.VoltageLevel;

import java.util.Objects;

/**
 * @author Baptiste Seguinot {@literal <baptiste.seguinot@rte-france.com>}
 */
public class VirtualHubExtension extends AbstractExtension<VoltageLevel> {

    private final String code;
    private final String eic;
    private final boolean isMcParticipant;
    private final String nodeName;

    public VirtualHubExtension(String code, String eic, boolean isMcParticipant, String nodeName) {
        this.code = Objects.requireNonNull(code, "VirtualHub creation does not allow null code");
        this.eic = Objects.requireNonNull(eic, "VirtualHub creation does not allow null eic");
        this.isMcParticipant = isMcParticipant;
        this.nodeName = Objects.requireNonNull(nodeName, "VirtualHub creation does not allow null nodeName");
    }

    public String getCode() {
        return code;
    }

    public String getEic() {
        return eic;
    }

    public boolean isMcParticipant() {
        return isMcParticipant;
    }

    public String getNodeName() {
        return nodeName;
    }

    @Override
    public String getName() {
        return "virtualHubExtension";
    }
}
