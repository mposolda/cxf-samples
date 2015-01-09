package org.apache.cxf.samples;

import javax.jws.WebService;
import javax.xml.ws.Holder;

import org.apache.cxf.samples.types.GetPerson;
import org.apache.cxf.samples.types.GetPersonResponse;

@WebService(serviceName = "PersonService", endpointInterface = "org.apache.cxf.samples.Person")
public class PersonImpl implements Person {

    public void getPerson(Holder<String> personId, Holder<String> ssn, Holder<String> name)
        throws UnknownPersonFault
    {
        if (personId.value == null || personId.value.length() == 0) {
            org.apache.cxf.samples.types.UnknownPersonFault fault = new org.apache.cxf.samples.types.UnknownPersonFault();
            fault.setPersonId(personId.value);
            throw new UnknownPersonFault(null,fault);
        }
        name.value = "Guillaume";
        ssn.value = "000-000-0000";
    }

}
