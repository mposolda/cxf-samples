package org.apache.cxf.samples;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Map;

import org.apache.cxf.Bus;
import org.apache.cxf.configuration.jsse.TLSServerParameters;
import org.apache.cxf.transport.http_jetty.JettyHTTPServerEngine;
import org.apache.cxf.transport.http_jetty.JettyHTTPServerEngineFactory;
import org.apache.cxf.transport.http_jetty.ThreadingParameters;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.util.security.Constraint;
import org.keycloak.adapters.jetty.KeycloakJettyAuthenticator;
import org.keycloak.representations.adapters.config.AdapterConfig;

/**
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public class KeycloakJettyHttpServerEngineFactory extends JettyHTTPServerEngineFactory {

    public KeycloakJettyHttpServerEngineFactory() {
        super();
    }

    public KeycloakJettyHttpServerEngineFactory(Bus b) {
        super(b);
    }

    public KeycloakJettyHttpServerEngineFactory(Bus b,
                                        Map<String, TLSServerParameters> tls,
                                        Map<String, ThreadingParameters> threading) {
        super(b, tls, threading);
    }

    @Override
    public synchronized JettyHTTPServerEngine createJettyHTTPServerEngine(String host, int port, String protocol) throws GeneralSecurityException, IOException {
        JettyHTTPServerEngine engine = super.createJettyHTTPServerEngine(host, port, protocol);
        Handler kcHandler = createKeycloakHandler();
        engine.setHandlers(Arrays.asList(kcHandler));
        return engine;
    }

    protected SecurityHandler createKeycloakHandler() {
        KeycloakJettyAuthenticator kcAuthenticator = new KeycloakJettyAuthenticator();
        AdapterConfig cfg = new AdapterConfig();
        cfg.setRealm("test");
        cfg.setResource("cxf-app");
        cfg.setAuthServerUrl("http://localhost:8081/auth");
        cfg.setBearerOnly(true);
        cfg.setSslRequired("EXTERNAL");
        kcAuthenticator.setAdapterConfig(cfg);

        ConstraintSecurityHandler secHandler = new ConstraintSecurityHandler();

        Constraint constraint = secHandler.createConstraint("Customers", true, new String[] { "user" }, 0);
        ConstraintMapping cstMapping = new ConstraintMapping();
        cstMapping.setConstraint(constraint);
        cstMapping.setPathSpec("/*");

        secHandler.addConstraintMapping(cstMapping);
        secHandler.addRole("admin");
        secHandler.addRole("user");

        secHandler.setAuthMethod("BASIC");
        secHandler.setRealmName("does not matter");
        secHandler.setAuthenticator(kcAuthenticator);
        return secHandler;
    }
}
