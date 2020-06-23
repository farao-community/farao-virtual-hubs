/*
 * Copyright (c) 2020, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package com.farao_community.farao.virtual_hubs.client;

import com.farao_community.farao.virtual_hubs.VirtualHubsConfiguration;
import com.farao_community.farao.virtual_hubs.json.JsonVirtualHubsConfiguration;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
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
        try {
            URI uri = (new URIBuilder()).setScheme("http").setHost(host).setPort(port).setPath("/virtual-hubs-configuration/publish").build();

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpEntity entity = MultipartEntityBuilder.create()
                    .addBinaryBody("configurationFile", is.readAllBytes(), ContentType.APPLICATION_XML, "inputfile.xml")
                    .addTextBody("validFrom", validFrom.format(DateTimeFormatter.ISO_DATE_TIME))
                    .addTextBody("validTo", validTo.format(DateTimeFormatter.ISO_DATE_TIME))
                    .build();
            HttpUriRequest request = RequestBuilder.post(uri).setEntity(entity).build();

            HttpResponse httpResponse = httpClient.execute(request);
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Erreur " + httpResponse.getStatusLine().getStatusCode());
            }
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public VirtualHubsConfiguration retrieve(LocalDateTime dateTime) {
        try {
            URI uri = (new URIBuilder()).setScheme("http").setHost(host).setPort(port).setPath("/virtual-hubs-configuration").build();

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpEntity entity = MultipartEntityBuilder.create()
                    .addTextBody("instant", dateTime.format(DateTimeFormatter.ISO_DATE_TIME))
                    .build();

            HttpUriRequest request = RequestBuilder.get(uri).setEntity(entity).build();

            HttpResponse httpResponse = httpClient.execute(request);
            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Erreur " + httpResponse.getStatusLine().getStatusCode());
            }
            return JsonVirtualHubsConfiguration.importConfiguration(httpResponse.getEntity().getContent());
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {

    }
}
