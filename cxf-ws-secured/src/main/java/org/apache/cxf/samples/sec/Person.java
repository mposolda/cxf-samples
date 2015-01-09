package org.apache.cxf.samples.sec;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import org.apache.cxf.samples.sec.types.ObjectFactory;

@WebService
@XmlSeeAlso({ObjectFactory.class})
public interface Person {

    @RequestWrapper(localName = "GetPerson", className = "GetPerson")
    @ResponseWrapper(localName = "GetPersonResponse", className = "GetPersonResponse")
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
