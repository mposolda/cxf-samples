package org.apache.cxf.example;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.resource.URIResolver;

/**
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public class ServletClient {

    private ServletClient() {
    }

    public static void main(String args[]) throws Exception {
        // Sent HTTP GET request to query all customer info
        /*
         * URL url = new URL("http://localhost:9000/customers");
         * System.out.println("Invoking server through HTTP GET to query all
         * customer info"); InputStream in = url.openStream(); StreamSource
         * source = new StreamSource(in); printSource(source);
         */

        // Sent HTTP GET request to query customer info
        System.out.println("Sent HTTP GET request to query customer info");
        URL url = new URL("http://localhost:8080/jax_rs_basic_servlet/services/service1/customerservice/customers/123");
        InputStream in = url.openStream();
        System.out.println(getStringFromInputStream(in));

        // Sent HTTP GET request to query sub resource product info
        System.out.println("\n");
        System.out.println("Sent HTTP GET request to query sub resource product info");
        url = new URL("http://localhost:8080/jax_rs_basic_servlet/services/service1/customerservice/orders/223/products/323");
        in = url.openStream();
        System.out.println(getStringFromInputStream(in));

        // Sent HTTP PUT request to update customer info
        System.out.println("\n");
        System.out.println("Sent HTTP PUT request to update customer info");
        ServletClient client = new ServletClient();
        String inputFile = client.getClass().getResource("/update_customer.xml").getFile();
        URIResolver resolver = new URIResolver(inputFile);
        File input = new File(resolver.getURI());
        PutMethod put = new PutMethod("http://localhost:8080/jax_rs_basic_servlet/services/service1/customerservice/customers");
        RequestEntity entity = new FileRequestEntity(input, "text/xml; charset=ISO-8859-1");
        put.setRequestEntity(entity);
        HttpClient httpclient = new HttpClient();

        try {
            int result = httpclient.executeMethod(put);
            System.out.println("Response status code: " + result);
            System.out.println("Response body: ");
            System.out.println(put.getResponseBodyAsString());
        } finally {
            // Release current connection to the connection pool once you are
            // done
            put.releaseConnection();
        }

        // Sent HTTP POST request to add customer
        System.out.println("\n");
        System.out.println("Sent HTTP POST request to add customer");
        inputFile = client.getClass().getResource("/add_customer.xml").getFile();
        resolver = new URIResolver(inputFile);
        input = new File(resolver.getURI());
        PostMethod post = new PostMethod("http://localhost:8080/jax_rs_basic_servlet/services/service1/customerservice/customers");
        post.addRequestHeader("Accept" , "text/xml");
        entity = new FileRequestEntity(input, "text/xml; charset=ISO-8859-1");
        post.setRequestEntity(entity);
        httpclient = new HttpClient();

        try {
            int result = httpclient.executeMethod(post);
            System.out.println("Response status code: " + result);
            System.out.println("Response body: ");
            System.out.println(post.getResponseBodyAsString());
        } finally {
            // Release current connection to the connection pool once you are
            // done
            post.releaseConnection();
        }

        System.out.println("\n");
        System.exit(0);
    }

    private static String getStringFromInputStream(InputStream in) throws Exception {
        CachedOutputStream bos = new CachedOutputStream();
        IOUtils.copy(in, bos);
        in.close();
        bos.close();
        return bos.getOut().toString();
    }
}
