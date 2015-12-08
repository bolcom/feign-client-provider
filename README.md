Client provider for Feign APIs

Provides a general way to bootstrap Feign API's using a builder pattern:
# Examples

## DockerRegistryApi with custom errordecoder

The first example is about our DockerRegistryApi, it's an easy construct with only a special DockerErrorDecoder
```
return new ClientProvider(url, new JacksonCallback() {
    @Override
    public void postConfigure(Builder builder) {
        builder.errorDecoder(new DockerErrorDecoder());
    }
}).create(DockerRegistryApi.class);
```

DockerRegistryApi.java
```
@Path("/v1/repositories")
public interface DockerRegistryApi {

    @GET
    @Path("/{namespace}/{applicationKey}/tags/{finalBranchCommitReference}")
    public String getImageId(
            @PathParam("namespace") String namespace,
            @PathParam("applicationKey") String applicationKey,
            @PathParam("finalBranchCommitReference") String finalBranchCommitReference
    );

    ...
}
```
DockerErrorDecoder.java
```
public class DockerErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400 && response.status() <= 499) {
            return new DockerException(
                    response.status(),
                    response.reason()
            );
        }
        return errorStatus(methodKey, response);
    }
}
```

## ConsulApi with various customizations
In this example the ConsulApi interface is bootstrapped using a JacksonCallback which by default sets the JAXRSContract, the JacksonDecoder and the JacksonEncoder. It also adds a JSON RequestInterceptor. 

For Consul you have to add some exceptions (for example we want to use the PascalCaseStrategy and we want to add a token to the query params on each request), so that is added within the body of the postConfigure block of the JacksonCallback - ensuring the contract isn't broken.

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
