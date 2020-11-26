/*
 * Copyright (c) 2020, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.virtual_hubs.network_extension;

import com.google.auto.service.AutoService;
import com.powsybl.commons.extensions.AbstractExtensionXmlSerializer;
import com.powsybl.commons.extensions.ExtensionXmlSerializer;
import com.powsybl.commons.xml.XmlReaderContext;
import com.powsybl.commons.xml.XmlWriterContext;
import com.powsybl.iidm.network.VoltageLevel;

import javax.xml.stream.XMLStreamException;

/**
 * @author Baptiste Seguinot {@literal <baptiste.seguinot@rte-france.com>}
 */
@AutoService(ExtensionXmlSerializer.class)
public class VirtualHubExtensionSerializer extends AbstractExtensionXmlSerializer<VoltageLevel, VirtualHubExtension> {

    public VirtualHubExtensionSerializer() {
        // TODO : understand what to put in this constructor
        super("virtualHubExtension", "network", VirtualHubExtension.class, false, "", "", "");
    }

    @Override
    public void write(VirtualHubExtension virtualHubExtension, XmlWriterContext context) throws XMLStreamException {
        context.getExtensionsWriter().writeAttribute("code", virtualHubExtension.getCode());
        context.getExtensionsWriter().writeAttribute("eic", virtualHubExtension.getEic());
        context.getExtensionsWriter().writeAttribute("isMcParticipant", Boolean.toString(virtualHubExtension.isMcParticipant()));
        context.getExtensionsWriter().writeAttribute("nodeName", virtualHubExtension.getNodeName());
    }

    @Override
    public VirtualHubExtension read(VoltageLevel voltageLevel, XmlReaderContext context) {
        String code = context.getReader().getAttributeValue(null, "code");
        String eic = context.getReader().getAttributeValue(null, "eic");
        String isMcParticipantAsString = context.getReader().getAttributeValue("false", "isMcParticipant");
        String nodeName = context.getReader().getAttributeValue(null, "nodeName");

        boolean isMcParticipant = false;
        if (isMcParticipantAsString.equals(Boolean.toString(true))) {
            isMcParticipant = true;
        }

        voltageLevel.newExtension(VirtualHubExtensionAdder.class).withCode(code).withEic(eic).withMcParticipant(isMcParticipant).withNodeName(nodeName).add();
        return voltageLevel.getExtension(VirtualHubExtension.class);
    }

}
