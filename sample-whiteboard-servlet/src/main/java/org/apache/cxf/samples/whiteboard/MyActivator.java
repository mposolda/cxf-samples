package org.apache.cxf.samples.whiteboard;

import java.net.URL;
import java.util.Arrays;

import javax.servlet.Servlet;

import org.ops4j.pax.web.service.WebContainer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpContext;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public class MyActivator implements BundleActivator {

    private BundleContext _context;
    private ServiceTracker _tracker;
    private HttpContext defaultCtx;

    public void start(BundleContext context) throws Exception {

        _context = context;

        // Use _tracker to capture when a HttpService comes and goes.
        //
        // When this bundle is started, a HttpService may not be alive. Thus, we use
        // ServiceTracker to automatically monitor when a HttpService comes alive and
        // then register this our CXF-based JAX-RS service with it.
        //
        _tracker = new ServiceTracker(
                _context,
                WebContainer.class.getName(),
                new ServiceTrackerCustomizer() {
                    public Object addingService(ServiceReference serviceReference) {
                        try {
                            WebContainer service = (WebContainer)_context.getService(serviceReference);
                            /*Dictionary<String, String> initParams = new Hashtable<String, String>();
                            initParams.put("javax.ws.rs.Application", SampleApplication.class.getName());
                            service.registerServlet(_path, new SampleServlet(), initParams, null);*/
                            defaultCtx = service.createDefaultHttpContext();
                            // TODO: classloading
                            URL jettyWebXml = _context.getBundle().getResource("/WEB-INF/jetty-web.xml");
                            if (jettyWebXml != null){
                                service.registerJettyWebXml(jettyWebXml, defaultCtx);
                            }

                            // TODO: Constraint mapping
                            service.registerConstraintMapping("Customers", "/*", null, null, true, Arrays.asList("user"), defaultCtx);
                            service.registerLoginConfig("BASIC", "does-not-matter", null, null, defaultCtx);

                            unregisterCurrentCxf();

                            return service;
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            throw new RuntimeException(ex);
                        }
                    }

                    public void modifiedService(ServiceReference serviceReference, Object o) {
                        // do nothing
                    }

                    public void removedService(ServiceReference serviceReference, Object o) {
                        WebContainer service = (WebContainer)_context.getService(serviceReference);
                        if (service != null) {
                            service.unregisterLoginConfig(defaultCtx);
                            service.unregisterConstraintMapping(defaultCtx);
                        }
                    }
                }
        );
        _tracker.open(true);

    }

    protected void unregisterCurrentCxf() throws InvalidSyntaxException {
        ServiceReference[] serviceReferences = _context.getServiceReferences(javax.servlet.Servlet.class.getName(), "(osgi.service.blueprint.compname=osgiServlet)");
        if (serviceReferences.length == 0) {
            // log. no registered cxf
            return;
        } else if (serviceReferences.length > 1) {
            throw new IllegalStateException("Not expected that more osgiServlets are registered");
        }
        ServiceReference cxfServletRef = serviceReferences[0];
        Servlet cxfServlet = (Servlet)_context.getService(serviceReferences[0]);

        BundleContext cxfTransportBundleContext = cxfServletRef.getBundle().getBundleContext();
        ServiceReference webContainerReference = cxfTransportBundleContext.getServiceReference(WebContainer.class.getName());
        WebContainer cxfContainer = (WebContainer) cxfTransportBundleContext.getService(webContainerReference);

        // TODO:
        try {
            cxfContainer.unregisterServlet(cxfServlet);
        } catch (Exception e) {
            System.err.println("CXFServlet already unregistered");
        }
    }

    public void stop(BundleContext bundleContext) throws Exception {
        _tracker.remove(_tracker.getServiceReference());
    }

}
