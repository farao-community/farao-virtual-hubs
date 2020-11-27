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
public class AssignedVirtualHubAdderImpl extends AbstractExtensionAdder<VoltageLevel, AssignedVirtualHub> implements AssignedVirtualHubAdder {

    private String code;
    private String eic;
    private boolean isMcParticipant;
    private String nodeName;
    private String relatedMa;

    public AssignedVirtualHubAdderImpl(VoltageLevel voltageLevel) {
        super(voltageLevel);
    }

    public AssignedVirtualHubAdderImpl withCode(String code) {
        this.code = code;
        return this;
    }

    public AssignedVirtualHubAdderImpl withEic(String eic) {
        this.eic = eic;
        return this;
    }

    public AssignedVirtualHubAdderImpl withMcParticipant(boolean isMcParticipant) {
        this.isMcParticipant = isMcParticipant;
        return this;
    }

    public AssignedVirtualHubAdderImpl withNodeName(String nodeName) {
        this.nodeName = nodeName;
        return this;
    }

    public AssignedVirtualHubAdder withRelatedMa(String relatedMa) {
        this.relatedMa = relatedMa;
        return this;
    }

    @Override
    public AssignedVirtualHubImpl createExtension(VoltageLevel voltageLevel) {
        return new AssignedVirtualHubImpl(code, eic, isMcParticipant, nodeName, relatedMa);
    }
}
