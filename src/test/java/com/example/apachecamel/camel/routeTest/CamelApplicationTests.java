package com.example.apachecamel.camel.routeTest;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@CamelSpringBootTest
@SpringBootTest
@UseAdviceWith
class CamelApplicationTests {

	@Autowired
	CamelContext camelContext;

	@Autowired
	ProducerTemplate producerTemplate;

	@EndpointInject("mock:result")
	protected MockEndpoint mockEndpoint;

	@Test
	public void contextLoads() throws Exception{

		String expectedBody = "Hello Camel";

		mockEndpoint.expectedBodiesReceived(expectedBody);
		mockEndpoint.expectedMinimumMessageCount(1);

		AdviceWith.adviceWith(camelContext, "simpleTimerId", routeBuilder -> {
			routeBuilder.weaveAddLast().to(mockEndpoint);
		});

		camelContext.start();
		mockEndpoint.assertIsSatisfied();

	}

	@Test //TEST QUE LEE EL ARCHIVO DE ENTRADA Y PRUEBA SI SE ENCUENTRA EL MENSAJE
	public void transferFileTest() throws Exception{

		String expectedBody = "Input file content";

		mockEndpoint.expectedBodiesReceived(expectedBody);
		mockEndpoint.expectedMinimumMessageCount(1);

		AdviceWith.adviceWith(camelContext, "legacyFileRouteId", routeBuilder -> {
			routeBuilder.weaveByToUri("file:*")
				.replace()
				.to(mockEndpoint);
		});

		camelContext.start();
		mockEndpoint.assertIsSatisfied();

	}

	@Test // TEST QUE SIMULA LA LECTURA DE UN ARCHIVO Y ESCRITURA EN OTRO
	public void testFileMoveByMockingFromEnpoint() throws Exception {

		String expectedBody = "HOLA";
		String errorBody = "hola";

		mockEndpoint.expectedBodiesReceived(expectedBody);
		mockEndpoint.expectedMinimumMessageCount(1);

		AdviceWith.adviceWith(camelContext, "legacyFileRouteId", routeBuilder -> {
			routeBuilder.replaceFromWith("direct:mockStart"); //simula la lectura de un archivo
			routeBuilder.weaveByToUri("file:*") //Simula la escritura de un archivo ()
				.replace()
				.to(mockEndpoint); // reemplaza el endpoint file por un mock
		});

		camelContext.start();
		producerTemplate.sendBody("direct:mockStart", errorBody);
		mockEndpoint.assertIsSatisfied();

	}

}
