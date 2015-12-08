# feign-client-provider
Client provider for Feign APIs

Provides a general way to bootstrap Feign API's using a builder pattern:

In this example the ConsulApi interface is bootstrapped using a JacksonCallback which by default sets the JAXRSContract, the JacksonDecoder and the JacksonEncoder. It also adds a JSON RequestInterceptor. 

```
 ConsulApi consulApi = new ClientProvider("http://" + hostname + ":" + port, new JacksonCallback() {
    @Override
    public void postConfigure(Builder builder) {
        ObjectMapper om = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.setPropertyNamingStrategy(new PropertyNamingStrategy.PascalCaseStrategy());
        builder.decoder(new JacksonDecoder(om));
        if (tokenProvider != null) {
            builder.requestInterceptor(new RequestInterceptor() {
                @Override
                public void apply(RequestTemplate template) {
                    template.query("token", tokenProvider.getToken());
                }
            });
        }

    }
}).create(ConsulApi.class);
```

The classes as used in the example:
```
public interface ConsulApi {

    @GET
    @Path("/v1/catalog/services")
    public ServiceList getServices();

    @GET
    @Path("/v1/catalog/service/{id}")
    public List<CatalogueService> getServiceFor(
            @PathParam("id") String id
    );
    ...
}
```

```
public interface TokenProvider {
    String getToken();
}
```
