/*
 * Copyright (c) 2020, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.virtual_hubs.network_extension_builder;

import com.farao_community.farao.virtual_hubs.VirtualHub;
import com.farao_community.farao.virtual_hubs.VirtualHubsConfiguration;
import com.farao_community.farao.virtual_hubs.network_extension.AssignedVirtualHubAdder;
import com.powsybl.iidm.network.Bus;
import com.powsybl.iidm.network.DanglingLine;
import com.powsybl.iidm.network.Network;
import com.powsybl.iidm.network.VoltageLevel;

import java.util.List;
import java.util.Optional;

/**
 * @author Baptiste Seguinot {@literal <baptiste.seguinot@rte-france.com>}
 */
public class VirtualHubAssigner {

    private List<VirtualHub> virtualHubs;

    public VirtualHubAssigner(List<VirtualHub> virtualHubs) {
        this.virtualHubs = virtualHubs;
    }

    public void addNetworkExtensions(Network network)
    {
        virtualHubs.forEach(vh -> addNetworkExtension(network, vh));
    }

    private void addNetworkExtension(Network network, VirtualHub virtualHub)
    {
        Optional<Bus> bus = findBusById(network, virtualHub.getNodeName());
        if (bus.isPresent()) {
            // virtual hub is on a real network node
            assignVirtualHub(bus.get().getVoltageLevel(), virtualHub);
            return;
        }

        Optional<DanglingLine> danglingLine = findDanglingLineWhichContainId(network, virtualHub.getNodeName());
        if (danglingLine.isPresent()) {
            // virtual hub is on a Xnode which has been merged in a dangling line during network import
            assignVirtualHub(danglingLine.get().getTerminal().getVoltageLevel(), virtualHub);
            return;
        }

        // add log

    }

    private Optional<Bus> findBusById(Network network, String id) {
        return network.getVoltageLevelStream()
            .flatMap(vl -> vl.getBusView().getBusStream())
            .filter(bus -> bus.getId().equals(id))
            .findFirst();
    }

    private Optional<DanglingLine> findDanglingLineWhichContainId(Network network, String id) {
        return network.getDanglingLineStream()
            .filter(danglingLine -> danglingLine.getId().contains(id))
            .findFirst();
    }

    private void assignVirtualHub(VoltageLevel voltageLevel, VirtualHub virtualHub) {
        voltageLevel.newExtension(AssignedVirtualHubAdder.class)
            .withCode(virtualHub.getCode())
            .withEic(virtualHub.getEic())
            .withMcParticipant(virtualHub.isMcParticipant())
            .withNodeName(virtualHub.getNodeName())
            .withRelatedMa(virtualHub.getRelatedMa().getCode())
            .add();
    }

}
