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

    public void addNetworkExtensions(Network network)
    {
        virtualHubs.forEach(vh -> addNetworkExtension(network, vh));
    }

    private void addNetworkExtension(Network network, VirtualHub virtualHub)
    {
        Optional<Bus> bus = findBusById(network, virtualHub.getNodeName());
        if (bus.isPresent()) {
            // virtual hub is on a real network node
            Generator generator = bus.get().getGenerators().iterator().next();

            if (Objects.isNull(generator)) {
                LOGGER.warn("Virtual hub cannot be assigned on bus {} as it does not contain any generator", virtualHub.getNodeName());
                return;
            }

            generator.newExtension(AssignedVirtualHubAdder.class)
                .withCode(virtualHub.getCode())
                .withEic(virtualHub.getEic())
                .withMcParticipant(virtualHub.isMcParticipant())
                .withNodeName(virtualHub.getNodeName())
                .withRelatedMa(Objects.isNull(virtualHub.getRelatedMa()) ? null : virtualHub.getRelatedMa().getCode())
                .add();
            return;
        }

        Optional<DanglingLine> danglingLine = findDanglingLineWithXNode(network, virtualHub.getNodeName());
        if (danglingLine.isPresent()) {
            // virtual hub is on a Xnode which has been merged in a dangling line during network import
            danglingLine.get().newExtension(AssignedVirtualHubAdder.class)
                .withCode(virtualHub.getCode())
                .withEic(virtualHub.getEic())
                .withMcParticipant(virtualHub.isMcParticipant())
                .withNodeName(virtualHub.getNodeName())
                .withRelatedMa(Objects.isNull(virtualHub.getRelatedMa()) ? null : virtualHub.getRelatedMa().getCode())
                .add();
            return;
        }

        LOGGER.warn("Virtual hub cannot be assigned on node {} as it was not found in the network", virtualHub.getNodeName());
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
