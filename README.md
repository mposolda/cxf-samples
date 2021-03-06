How to secure camel-jetty endpoint
----------------------------------
1) Assume keycloak running on localhost:8081 with realm "demo" and application "cxf-app" with redirectUri "http://localhost:8383/cxf/*" and secret "dae59761-d549-41cc-a932-2b80c0b5cdad"

2) Build project    (TODO: Repo must be in settings.xml as of now https://access.redhat.com/documentation/en-US/Red_Hat_JBoss_Fuse/6.1/html/Getting_Started/Develop.html . 
Should be improved and repo added to the project parent pom)

3) In jboss fuse 6.1 run:

> features:addurl mvn:org.keycloak/keycloak-osgi-features/1.1.0.Final-SNAPSHOT/xml/features
> features:install keycloak-core-adapter
> features:list 

Now check that keycloak is installed

4)
> osgi:install mvn:org.apache.cxf.samples/cxf-ws-unsecured/2.7.0.redhat-610-SNAPSHOT
> osgi:start 279 

Replace 279 with the ID of bundle cxf-ws-unsecured installed in previous step

Now there should be unsecured URL available: http://localhost:8181/cxf/PersonServiceCF?wsdl

5) 

> osgi:install mvn:org.apache.cxf.samples/camel-basic-keycloak/2.7.0.redhat-610-SNAPSHOT
> osgi:start 280 (Replace 280 with the ID of bundle camel-basic-keycloak installed in previous step)

Now there is camel endpoint available: http://localhost:8383/cxf/PersonServiceCF?wsdl
and it's secured by Keycloak. Login as john/password (or whatever user with realm role 'user' ) to be able to access endpoint.


How to secure cxf ws endpoint
-----------------------------
1)

> osgi:install mvn:org.apache.cxf.samples/cxf-ws-secured/2.7.0.redhat-610-SNAPSHOT
> osgi:start 281

After doing this, there will be new secured application running on "http://localhost:8484/PersonServiceCF-sec?wsdl" and authenticated by Keycloak.
Login as john/password 


How to secure classic servlet application
-----------------------------------------
It is not possible to easily do it in JBoss Fuse 6.1 due to bug https://ops4j1.jira.com/browse/PAXWEB-666 . Fuse 6.1 has dependency on pax-web 3.0.6 .
In Karaf 3.0.2 it's possible easily as it has newer pax-web 3.1.2:

> bundle:install war:mvn:org.apache.cxf.samples/jax_rs_basic_servlet/2.7.0.redhat-610-SNAPSHOT/war?Web-ContextPath=jax_rs_basic_servlet
> bundle:start 282

When accessing http://localhost:8181/jax_rs_basic_servlet/services/service1/customerservice/customers/123 you can do it as long as you are logged as john.

How to secure whiteboard managed servlet application
----------------------------------------------------

> bundle:install mvn:org.apache.cxf.samples/sample-whiteboard-servlet/2.7.0.redhat-610-SNAPSHOT
> bundle start 283

Then servlet available on "http://localhost:8181/simple_whiteboard_servlet"


How to secure "cxf" context
---------------------------

On karaf 3.0.2:

> feature:repo-add mvn:org.apache.cxf.karaf/apache-cxf/2.7.14/xml/features
> feature:install cxf

then install keycloak:

> feature:repo-add mvn:org.keycloak/keycloak-osgi-features/1.1.0.Final-SNAPSHOT/xml/features
> feature:install keycloak-core-adapter

and finally the application

> bundle:install mvn:org.apache.cxf.samples/sample-whiteboard-servlet/2.7.0.redhat-610-SNAPSHOT
> bundle:start
 
 
