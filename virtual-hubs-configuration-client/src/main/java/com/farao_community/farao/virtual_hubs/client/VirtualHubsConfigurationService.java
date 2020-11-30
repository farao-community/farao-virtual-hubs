/*
 * Copyright (c) 2020, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.virtual_hubs.client;

import com.farao_community.farao.virtual_hubs.VirtualHubsConfiguration;
import com.farao_community.farao.virtual_hubs.json.JsonVirtualHubsConfiguration;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Sebastien Murgey {@literal <sebastien.murgey@rte-france.com>}
 */
public class VirtualHubsConfigurationService implements AutoCloseable {
    private final String host;
    private final int port;

    public VirtualHubsConfigurationService(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void publish(InputStream is, LocalDateTime validFrom, LocalDateTime validTo) {
        URI uri = UriComponentsBuilder.newInstance().scheme("http").host(host).port(port).path("/virtual-hubs-configuration/publish").build().toUri();
        WebClient client = WebClient.create();
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("configurationFile", getStreamBytes(is), MediaType.APPLICATION_XML).filename("inputFile.xml");
        builder.part("validFrom", validFrom.format(DateTimeFormatter.ISO_DATE_TIME));
        builder.part("validTo", validTo.format(DateTimeFormatter.ISO_DATE_TIME));
        ClientResponse response = client.post().uri(uri).body(BodyInserters.fromMultipartData(builder.build())).exchange().block();

        if (!response.statusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error " + response.statusCode());
        }
    }

    private byte[] getStreamBytes(InputStream is) {
        try {
            return IOUtils.toByteArray(is);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public VirtualHubsConfiguration retrieve() {
        URI uri = UriComponentsBuilder.newInstance().scheme("http").host(host).port(port)
                .path("/virtual-hubs-configuration")
                .build().toUri();
        return retrieve(uri);
    }

    public VirtualHubsConfiguration retrieve(LocalDateTime dateTime) {
        URI uri = UriComponentsBuilder.newInstance().scheme("http").host(host).port(port)
                .path("/virtual-hubs-configuration").queryParam("instant", dateTime.format(DateTimeFormatter.ISO_DATE_TIME))
                .build().toUri();
        return retrieve(uri);
    }

    private VirtualHubsConfiguration retrieve(URI uri) {
        WebClient client = WebClient.create();
        ClientResponse response = client.get().uri(uri).exchange().block();

        if (!response.statusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error " + response.statusCode());
        }

        ByteArrayResource resource = response.bodyToMono(ByteArrayResource.class).block();
        return JsonVirtualHubsConfiguration.importConfiguration(new ByteArrayInputStream(resource.getByteArray()));
    }

    @Override
    public void close() throws Exception {

    }
}
