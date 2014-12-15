package org.apache.cxf.samples;

import demo.jaxrs.server.CustomerService;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.transport.http_jetty.JettyHTTPServerEngineFactory;

/**
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public class ServerWithKeycloakAuth {

    protected ServerWithKeycloakAuth() throws Exception {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();

        sf.setResourceClasses(CustomerService.class);
        sf.setResourceProvider(CustomerService.class,
                new SingletonResourceProvider(new CustomerService()));
        sf.setAddress("http://localhost:9000/");

        addKeycloakAuth(sf);

        sf.create();
    }

    protected void addKeycloakAuth(JAXRSServerFactoryBean sf) {
        Bus bus = sf.getBus();
        KeycloakJettyHttpServerEngineFactory engineFactory = new KeycloakJettyHttpServerEngineFactory(bus);
        bus.setExtension(engineFactory, JettyHTTPServerEngineFactory.class);
    }

    public static void main(String args[]) throws Exception {
        new ServerWithKeycloakAuth();
        System.out.println("Server with keycloak auth ready...");

        Thread.sleep(5 * 6000 * 1000);
        System.out.println("Server with keycloak auth exiting");
        System.exit(0);
    }
}
