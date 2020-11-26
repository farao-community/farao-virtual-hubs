/*
 * Copyright (c) 2020, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.virtual_hubs.network_extension;

import com.powsybl.commons.extensions.AbstractExtensionAdder;
import com.powsybl.iidm.network.VoltageLevel;

/**
 * @author Baptiste Seguinot {@literal <baptiste.seguinot@rte-france.com>}
 */
public class VirtualHubExtensionAdder extends AbstractExtensionAdder<VoltageLevel, VirtualHubExtension> {

    private String code;
    private String eic;
    private boolean isMcParticipant;
    private String nodeName;

    public VirtualHubExtensionAdder(VoltageLevel voltageLevel) {
        super(voltageLevel);
    }

    public VirtualHubExtensionAdder withCode(String code) {
        this.code = code;
        return this;
    }

    public VirtualHubExtensionAdder withEic(String eic) {
        this.eic = eic;
        return this;
    }

    public VirtualHubExtensionAdder withMcParticipant(boolean isMcParticipant) {
        this.isMcParticipant = isMcParticipant;
        return this;
    }

    public VirtualHubExtensionAdder withNodeName(String nodeName) {
        this.nodeName = nodeName;
        return this;
    }

    @Override
    public Class<VirtualHubExtension> getExtensionClass() {
        return VirtualHubExtension.class;
    }

    @Override
    public VirtualHubExtension createExtension(VoltageLevel voltageLevel) {
        return new VirtualHubExtension(code, eic, isMcParticipant, nodeName);
    }
}
