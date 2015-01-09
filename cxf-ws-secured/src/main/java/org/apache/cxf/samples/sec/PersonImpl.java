package org.apache.cxf.samples.sec;

import javax.jws.WebService;
import javax.xml.ws.Holder;

@WebService(serviceName = "PersonService", endpointInterface = "org.apache.cxf.samples.sec.Person")
public class PersonImpl implements Person {

    public void getPerson(Holder<String> personId, Holder<String> ssn, Holder<String> name)
        throws UnknownPersonFault
    {
        if (personId.value == null || personId.value.length() == 0) {
            org.apache.cxf.samples.sec.types.UnknownPersonFault fault = new org.apache.cxf.samples.sec.types.UnknownPersonFault();
            fault.setPersonId(personId.value);
            throw new UnknownPersonFault(null,fault);
        }
        name.value = "Guillaume";
        ssn.value = "000-000-0000";
    }

}
