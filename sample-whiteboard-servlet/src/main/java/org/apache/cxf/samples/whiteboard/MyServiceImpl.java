package org.apache.cxf.samples.whiteboard;

import org.osgi.service.blueprint.container.BlueprintContainer;

/**
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public class MyServiceImpl implements MyService {

    private BlueprintContainer blueprintContainer;
    private Object destinationRegistry;

    public MyServiceImpl() {
        System.out.println("MyServiceImpl: Hello from constructor");
    }

    @Override
    public String hello() {
        return "MyServiceImpl hello";
    }

    public BlueprintContainer getBlueprintContainer() {
        return blueprintContainer;
    }

    public void setBlueprintContainer(BlueprintContainer blueprintContainer) {
        this.blueprintContainer = blueprintContainer;
    }

    public Object getDestinationRegistry() {
        return destinationRegistry;
    }

    public void setDestinationRegistry(Object destinationService) {
        this.destinationRegistry = destinationService;
    }

    public void init() {
        System.out.println("MyServiceImpl init");
    }

    public void destroy() {
        System.out.println("MyServiceImpl destroy");
    }
}
