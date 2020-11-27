/*
 * Copyright (c) 2020, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.virtual_hubs.network_extension;

import com.powsybl.iidm.import_.Importers;
import com.powsybl.iidm.network.Network;
import com.powsybl.iidm.network.VoltageLevel;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Baptiste Seguinot {@literal <baptiste.seguinot@rte-france.com>}
 */
public class AssignedVirtualHubTest {

    private static final String SMALL_NETWORK_FILE_NAME = "12Nodes_with_Xnodes.xiidm";

    @Test
    public void testConstructor() {
        AssignedVirtualHub virtualHub = new AssignedVirtualHubImpl("code", "10XAAAUDHGKAAAAS", false, "12345678", "FR");
        assertEquals("code", virtualHub.getCode());
        assertEquals("10XAAAUDHGKAAAAS", virtualHub.getEic());
        assertFalse(virtualHub.isMcParticipant());
        assertEquals("12345678", virtualHub.getNodeName());
        assertEquals("FR", virtualHub.getRelatedMa());
    }

    @Test
    public void testExtensionAdder1() {
        Network network = Importers.loadNetwork(SMALL_NETWORK_FILE_NAME, getClass().getResourceAsStream("/" + SMALL_NETWORK_FILE_NAME));
        VoltageLevel anyVoltageLevel = network.getVoltageLevels().iterator().next();

        anyVoltageLevel.newExtension(AssignedVirtualHubAdder.class)
            .withCode("CODE__")
            .withEic("19VDUEGOLKAAAAS")
            .withMcParticipant(true)
            .withNodeName("")
            .withRelatedMa("BE")
            .add();

        AssignedVirtualHub virtualHub = anyVoltageLevel.getExtension(AssignedVirtualHub.class);

        assertNotNull(virtualHub);
        assertEquals("CODE__", virtualHub.getCode());
        assertEquals("19VDUEGOLKAAAAS", virtualHub.getEic());
        assertTrue(virtualHub.isMcParticipant());
        assertEquals("", virtualHub.getNodeName());
        assertEquals("BE", virtualHub.getRelatedMa());
    }
}
