package com.example.apachecamel.camel.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.apachecamel.camel.config.ConfigProperties;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SimpleTimer extends RouteBuilder{

    private final ConfigProperties configProperties;

    private final static Logger log = LoggerFactory.getLogger(SimpleTimer.class);

    @Override
    public void configure() throws Exception {

        from("timer:simpleTimer?period=2000")
            .routeId("simpleTimerId")
            .setBody(simple(configProperties.getOutputDir()))
            .process(exchange -> {
                log.info("Body: " + exchange.getIn().getBody());
            })
            .to("file:" + configProperties.getOutputDir() + "?fileName=" + configProperties.getOutputFileName())
            .log(LoggingLevel.INFO, log, "File created with name: ${header.CamelFileName}");

    }

}
