package org.apache.cxf.samples;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

@WebService
@XmlSeeAlso({org.apache.cxf.samples.types.ObjectFactory.class})
public interface Person {

    @RequestWrapper(localName = "GetPerson", className = "org.apache.cxf.samples.types.GetPerson")
    @ResponseWrapper(localName = "GetPersonResponse", className = "org.apache.cxf.samples.types.GetPersonResponse")
    @WebMethod(operationName = "GetPerson")
    public void getPerson(
        @WebParam(mode = WebParam.Mode.INOUT, name = "personId")
        javax.xml.ws.Holder<java.lang.String> personId,
        @WebParam(mode = WebParam.Mode.OUT, name = "ssn")
        javax.xml.ws.Holder<java.lang.String> ssn,
        @WebParam(mode = WebParam.Mode.OUT, name = "name")
        javax.xml.ws.Holder<java.lang.String> name
    ) throws UnknownPersonFault;
}
