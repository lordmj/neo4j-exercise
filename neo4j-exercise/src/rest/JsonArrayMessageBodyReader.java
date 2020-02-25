package rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.json.Json;
import javax.json.JsonArray;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

@Provider
@Consumes(value={"application/*+json","text/json"})
public class JsonArrayMessageBodyReader implements MessageBodyReader<JsonArray> {

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
		return JsonArray.class.isAssignableFrom(type);
	}

	@Override
	public JsonArray readFrom(Class<JsonArray> type, Type genericType,
            Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> map, InputStream in) throws IOException, WebApplicationException {
		
		InputStreamReader reader = new InputStreamReader(in);
		
		return Json.createReader(reader).readArray();
	}

}