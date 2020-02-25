package rest;

import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import io.swagger.v3.oas.annotations.Operation;

@Path("/employees")
@io.swagger.v3.oas.annotations.tags.Tag(name = "App")
public class RestApplication {
	
	static Driver driver;
	
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/employee/{id}/{name}")
    @Operation(summary = "Create a employee",
    description = "Create a new employee")	
	public JsonArray createEmployee(@PathParam("name") String name, @PathParam("id") int id){
		
		try(Session session = getSession();){
			
			Map<String, Object> props = new HashMap<String, Object>();
			props.put("emp_id", id);
			props.put("name", name);
			
			Map<String, Object> superProps = new HashMap<String, Object>();
			superProps.put("props", props);
			
			StatementResult result = session.run("CREATE (employee:EMPLOYEE {props}) RETURN employee.emp_id AS emp_id, employee.name AS emp_name", superProps);
			
			JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
			
			for(Record record : result.list()) {
				
				id = record.get("emp_id").asInt();
				name = record.get("emp_name").asString();
				
				JsonObjectBuilder oBuilder = Json.createObjectBuilder();
				
				oBuilder.add("id", id);
				oBuilder.add("name", name);
				
				arrBuilder.add(oBuilder.build());
				
				
			}
			
			return arrBuilder.build();
		
		}		
		
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/all")
    @Operation(summary = "Retrive all employees",
    description = "Retrieve all employees")	
	public JsonArray getEmployees(){
		
		
		try(Session session = getSession();){
		
			StatementResult result = session.run("MATCH (employee:EMPLOYEE) return employee.emp_id AS emp_id, employee.name AS emp_name");
			
			JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
			
			for(Record record : result.list()) {
				
				int id = record.get("emp_id").asInt();
				String name = record.get("emp_name").asString();
				
				JsonObjectBuilder oBuilder = Json.createObjectBuilder();
				
				oBuilder.add("id", id);
				oBuilder.add("name", name);
				
				arrBuilder.add(oBuilder.build());
				
				
			}
			
			return arrBuilder.build();
		
		}
		
	}
	

	/**
	 * Returns the Session that is active for the current thread or creates a new one
	 * @return
	 */
	static Session getSession(){
		
		//If not, see if the token is in the database
		if(driver == null){

			String masterUser = "";
			String masterPassword = "";
			String dbUrl = "";

			if(System.getenv("neo4j_exercise_url") != null) {
				dbUrl = System.getenv("neo4j_exercise_url");
			}
			
			if(System.getenv("neo4j_exercise_user") != null) {
				masterUser = System.getenv("neo4j_exercise_user");
			}
			
			if(System.getenv("neo4j_exercise_password") != null) {
				masterPassword = System.getenv("neo4j_exercise_password");
			}
			
		    Config noSSL = Config.build().withEncryptionLevel(Config.EncryptionLevel.NONE).toConfig();
	        driver = GraphDatabase.driver(dbUrl,AuthTokens.basic(masterUser, masterPassword),noSSL);
			registerShutdownHook(driver);
		}			

		Session session = driver.session();

		return session;
		
		
	}
	
	
	private static void registerShutdownHook( Driver driver )
	{
		// Registers a shutdown hook for the Neo4j instance so that it
		// shuts down nicely when the VM exits (even if you "Ctrl-C" the
		// running example before it's completed)
		Runtime.getRuntime().addShutdownHook( new Thread()
		{
			@Override
			public void run(){
				
				driver.close();
			}
		} );
	}

}
