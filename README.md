How to secure camel-jetty endpoint
----------------------------------
1) Assume keycloak running on localhost:8081 with realm "demo" and application "cxf-app" with redirectUri "http://localhost:8383/cxf/*" and secret "dae59761-d549-41cc-a932-2b80c0b5cdad"

2) Build project    (TODO: Repo must be in settings.xml as of now https://access.redhat.com/documentation/en-US/Red_Hat_JBoss_Fuse/6.1/html/Getting_Started/Develop.html . 
Should be improved and repo added to the project parent pom)

3) In jboss fuse 6.1 run:

features:addurl mvn:org.keycloak/keycloak-osgi-features/1.1.0.Final-SNAPSHOT/xml/features
features:install keycloak-core-adapter
osgi:install mvn:org.apache.cxf.samples/camel-basic-keycloak/2.7.0.redhat-610-SNAPSHOT
osgi:start 280 (Replace 280 with the ID of bundle camel-basic-keycloak installed in previous step)




