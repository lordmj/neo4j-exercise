package rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.json.Json;
import javax.json.JsonValue;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

public class JsonValueMessageBodyReader implements MessageBodyReader<JsonValue> {

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
            Annotation[] annotations, MediaType mediaType) {
		return type.isAssignableFrom(JsonValue.class);
	}

	@Override
	public JsonValue readFrom(Class<JsonValue> type, Type genericType,
            Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> map, InputStream in) throws IOException, WebApplicationException {
		
		InputStreamReader reader = new InputStreamReader(in);
		
		return Json.createReader(reader).read();
	}

}
