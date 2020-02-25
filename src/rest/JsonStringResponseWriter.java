package rest;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Provider
@Produces(value={"application/*+json","text/json"})
public class JsonStringResponseWriter implements MessageBodyWriter<String> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType,
                               Annotation[] annotations, MediaType mediaType) {
        return type == String.class;
    }

    @Override
    public long getSize(String val, Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType) {
        // deprecated by JAX-RS 2.0 and ignored by Jersey runtime
        return 0;
    }

    @Override
    public void writeTo(String jsonString, Class<?> type, Type genericType, Annotation[] annotations,
                        MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
                        OutputStream out) throws IOException, WebApplicationException {
    	
		JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
		JsonValue val = jsonReader.read();
		jsonReader.close();	

    		if(val instanceof JsonStructure) {
    	
	        Writer writer = new PrintWriter(out);
	        
			StringWriter sw = new StringWriter();        
			
			Map<String, Object> properties = new HashMap<>(1);
			properties.put(JsonGenerator.PRETTY_PRINTING, true);
	
			JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
			JsonWriter jsonWriter = writerFactory.createWriter(sw);
	
			jsonWriter.write((JsonStructure)val);
			jsonWriter.close();
	
			String prettyPrinted = sw.toString();
			
			writer.write(prettyPrinted);
	
	        writer.flush();
	        writer.close();
	        
    		}else {
    			
    			Writer writer = new PrintWriter(out);
    			
    			writer.write(val.toString());
    			
    			writer.flush();
    			writer.close();
    			
    		}
    }
}