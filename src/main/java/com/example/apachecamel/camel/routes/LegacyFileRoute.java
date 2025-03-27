package com.example.apachecamel.camel.routes;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.apachecamel.camel.config.ConfigProperties;
import com.example.apachecamel.camel.models.DataInputFile;
import com.example.apachecamel.camel.processors.TransportFileProcessor;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LegacyFileRoute extends RouteBuilder {
    
    private final ConfigProperties configProperties;

    private static final Logger logger = LoggerFactory.getLogger(LegacyFileRoute.class);

    @Override
    public void configure() throws Exception {

        from("file:"+ configProperties.getInputDir() 
            + "?fileName=" + configProperties.getInputFileName())
                .routeId("legacyFileRouteId")
                .process(exchange -> {
                    DataInputFile dataInputFile = TransportFileProcessor.getDataFile(exchange);
                    logger.info(dataInputFile.toString());
                })
        .to("file:" + configProperties.getOutputDir() + 
            "?fileName=" + configProperties.getOutputFileName());

    }

}
