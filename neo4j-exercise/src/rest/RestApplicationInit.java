package rest;



import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;



import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.*;

import java.util.HashSet;
import java.util.Set;


import javax.servlet.ServletConfig;
import javax.ws.rs.ApplicationPath;

 
 
@ApplicationPath("api")
@OpenAPIDefinition (info = 
@io.swagger.v3.oas.annotations.info.Info(
          title = "Employee API",
          version = "0.1",
          description = "REST Interface for Neo4J Exercise",
          contact = @io.swagger.v3.oas.annotations.info.Contact(name = "Michael Johnson", email = "lordmj@gmail.com")
  )
)
public class RestApplicationInit extends Application {
 
   private Set<Object> singletons = new HashSet<Object>();
   private Set<Class<?>> classes = new HashSet<Class<?>>();
 
   public RestApplicationInit(@Context ServletConfig servletConfig) {
	   
	   classes.add(RestApplication.class);

	   singletons.add(new JsonResponseWriter());
	   singletons.add(new JsonStringResponseWriter());
	   singletons.add(new JsonObjectMessageBodyReader());
	   singletons.add(new JsonArrayMessageBodyReader());
	   singletons.add(new JsonValueMessageBodyReader());
	   
       OpenAPI oas = new OpenAPI();
       Info info = new Info()
               .title("Neo4J Exercise")
               .description("REST Applcation")
               //.termsOfService("http://swagger.io/terms/")
               .contact(new Contact()
                       .email("lordmj@gmail.com"))
               .license(new License()
                       .name("Apache 2.0")
                       .url("http://www.apache.org/licenses/LICENSE-2.0.html"));

       oas.info(info);
       SwaggerConfiguration oasConfig = new SwaggerConfiguration()
               .openAPI(oas)
               .prettyPrint(true);

       try {
           new JaxrsOpenApiContextBuilder()
                   .servletConfig(servletConfig)
                   .application(this)
                   .openApiConfiguration(oasConfig)
                   .buildContext(true);
       } catch (OpenApiConfigurationException e) {
           throw new RuntimeException(e.getMessage(), e);
       }	   
	   
	   classes.add(OpenApiResource.class);
	   classes.add(AcceptHeaderOpenApiResource.class);
	   

   }
 
   @Override
   public Set<Class<?>> getClasses() {
      return classes;
   }
 
   @Override
   public Set<Object> getSingletons() {
      return singletons;
   }
}