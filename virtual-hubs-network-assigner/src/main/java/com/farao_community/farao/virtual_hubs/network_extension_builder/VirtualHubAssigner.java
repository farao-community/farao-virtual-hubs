/*
 * Copyright (c) 2020, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.virtual_hubs.network_extension_builder;

import com.farao_community.farao.virtual_hubs.VirtualHub;
import com.farao_community.farao.virtual_hubs.network_extension.AssignedVirtualHubAdder;
import com.powsybl.iidm.network.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Baptiste Seguinot {@literal <baptiste.seguinot@rte-france.com>}
 */
public class VirtualHubAssigner {

    private static final Logger LOGGER = LoggerFactory.getLogger(VirtualHubAssigner.class);
    private List<VirtualHub> virtualHubs;

    public VirtualHubAssigner(List<VirtualHub> virtualHubs) {
        this.virtualHubs = virtualHubs;
    }

    public void addVirtualLoads(Network network) {
        virtualHubs.forEach(vh -> addVirtualLoad(network, vh));
    }

    private void addVirtualLoad(Network network, VirtualHub virtualHub) {

        Optional<Bus> bus = findBusById(network, virtualHub.getNodeName());
        if (bus.isPresent()) {
            // virtual hub is on a real network node
            addVirtualHubOnNewFictitiousLoad(bus.get(), virtualHub);
            return;
        }

        Optional<DanglingLine> danglingLine = findDanglingLineWithXNode(network, virtualHub.getNodeName());
        if (danglingLine.isPresent()) {
            // virtual hub is on a Xnode which has been merged in a dangling line during network import
            if (danglingLine.get().getTerminal().isConnected()) {
                addVirtualHubOnNewFictitiousLoad(danglingLine.get().getTerminal().getBusBreakerView().getConnectableBus(), virtualHub);
            } else {
                LOGGER.warn("Virtual hub {} was not assigned on node {} as it is disconnected from the main network", virtualHub.getEic(), virtualHub.getNodeName());
            }
            return;
        }

        LOGGER.warn("Virtual hub {} cannot be assigned on node {} as it was not found in the network", virtualHub.getEic(), virtualHub.getNodeName());
    }

    private void addVirtualHubOnNewFictitiousLoad(Bus bus, VirtualHub virtualHub) {
        // add a fictitious load to this bus
        Load load = bus.getVoltageLevel().newLoad()
            .setBus(bus.getId())
            .setId(virtualHub.getEic() + "_virtualLoad")
            .setEnsureIdUnicity(true)
            .setLoadType(LoadType.FICTITIOUS)
            .setP0(0.).setQ0(0.)
            .add();

        // the virtual hub is assigned on this load
        load.newExtension(AssignedVirtualHubAdder.class)
            .withCode(virtualHub.getCode())
            .withEic(virtualHub.getEic())
            .withMcParticipant(virtualHub.isMcParticipant())
            .withNodeName(virtualHub.getNodeName())
            .withRelatedMa(Objects.isNull(virtualHub.getRelatedMa()) ? null : virtualHub.getRelatedMa().getCode())
            .add();

        LOGGER.info("A fictitious load {} has been added to {} in order to assign the virtual hub {}", load.getId(), bus.getId(), virtualHub.getEic());
    }

    private Optional<Bus> findBusById(Network network, String id) {
        return network.getVoltageLevelStream()
            .flatMap(vl -> vl.getBusBreakerView().getBusStream())
            .filter(bus -> bus.getId().equals(id))
            .findFirst();
    }

    private Optional<DanglingLine> findDanglingLineWithXNode(Network network, String xNodeId) {
        return network.getDanglingLineStream()
            .filter(danglingLine -> danglingLine.getUcteXnodeCode().equals(xNodeId))
            .findFirst();
    }
}
