package dadVertx;
//Servidor HTTP
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mysqlclient.MySQLClient;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;
import types.Pinza;
import types.Sensor;

public class DatabaseVerticle extends AbstractVerticle{
	
	private MySQLPool mySQLPool;
	
	@Override
	public void start(Promise<Void> startPromise) {
		MySQLConnectOptions mySQLConnectOptions = new MySQLConnectOptions().setPort(3306).setHost("localhost")
				.setDatabase("proyectopinza").setUser("root").setPassword("1997");
		PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
		mySQLPool = MySQLPool.pool(vertx, mySQLConnectOptions, poolOptions);
		
		Router router = Router.router(vertx);
		router.route("/api/sensor").handler(BodyHandler.create());
		router.route("/api/pinza").handler(BodyHandler.create());
		
		vertx.createHttpServer().requestHandler(router::handle).listen(8080, result -> {
			if (result.succeeded()) {
				startPromise.complete();
			} else {
				startPromise.fail(result.cause());
			}
		});
		router.get("/api/fechasensor/:timeStamp").handler(this::getValueSensorByTimestamp);
		router.get("/api/sensor/:idsensor").handler(this::getValueBySensor);
		router.put("/api/sensor").handler(this::putValueForSensor);
		
		router.get("/api/fechapinza/:timeStamp").handler(this::getValuePinzaByTimestamp);
		router.get("/api/pinza/:idpinza").handler(this::getValueByPinza);
		router.put("/api/pinza").handler(this::putValueForPinza);
	}
	
	private void getValueByPinza(RoutingContext routingContext) {
		mySQLPool.preparedQuery("SELECT * FROM proyectopinza.pinza WHERE idpinza = " + routingContext.request().getParam("idpinza"), res -> {
			if (res.succeeded()) {
				RowSet<Row> resultSet = res.result();
				System.out.println("El número de elementos obtenidos es " + resultSet.size());
				JsonArray result = new JsonArray();
				for (Row row : resultSet) {
					result.add(JsonObject.mapFrom(new Pinza(row.getInteger("idpinza"),
							row.getInteger("senal"),
							row.getLong("timeStamp"),
							row.getString("ip"))));
				}
				
				routingContext.response().setStatusCode(200).putHeader("conent-type", "application/json")
						.end(result.encodePrettily());
			} else {
				routingContext.response().setStatusCode(401).putHeader("content-type", "application/json") 
					.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
			}
		});
	}
	
	private void getValuePinzaByTimestamp(RoutingContext routingContext) {
		mySQLPool.preparedQuery("SELECT * FROM proyectopinza.pinza WHERE timeStamp = " + routingContext.request().getParam("timeStamp"), res -> {
			if (res.succeeded()) {
				RowSet<Row> resultSet = res.result();
				System.out.println("El número de elementos obtenidos es " + resultSet.size());
				JsonArray result = new JsonArray();
				for (Row row : resultSet) {
					result.add(JsonObject.mapFrom(new Pinza(row.getInteger("idpinza"),
							row.getInteger("senal"),
							row.getLong("timeStamp"),
							row.getString("ip"))));
				}
				
				routingContext.response().setStatusCode(200).putHeader("conent-type", "application/json")
						.end(result.encodePrettily());
			} else {
				routingContext.response().setStatusCode(401).putHeader("content-type", "application/json") 
					.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
			}
		});
	}
	
	private void putValueForPinza(RoutingContext routingContext) {
		Pinza pinza = Json.decodeValue(routingContext.getBodyAsString(), Pinza.class);
		mySQLPool.preparedQuery("INSERT INTO proyectopinza.pinza (idpinza, senal, timeStamp, ip, registro) VALUES (?,?,?,?,?)", Tuple.of(pinza.getIdpinza(), pinza.getSenal(), pinza.getTimeStamp(), pinza.getIp(), pinza.getRegistro()), handler -> {
			if(handler.succeeded()) {
				System.out.println(handler.result().rowCount());
				
				long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
				pinza.setIdpinza((int) id);
				
				routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
					.end(JsonObject.mapFrom(pinza).encodePrettily());
			} else {
				System.out.println(handler.cause().toString());
				routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
					.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
			}
		});
	}
	
	private void putValueForSensor(RoutingContext routingContext) {
		Sensor sensor = Json.decodeValue(routingContext.getBodyAsString(), Sensor.class);
		mySQLPool.preparedQuery("INSERT INTO proyectopinza.sensor (idsensor, timeStamp, base, valor) VALUES (?,?,?,?)", Tuple.of(sensor.getIdsensor(), sensor.getTimeStamp(), sensor.getBase(), sensor.getValor()), handler -> {
			if(handler.succeeded()) {
				System.out.println(handler.result().rowCount());
				
				long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
				sensor.setIdsensor((int) id);
				
				routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
					.end(JsonObject.mapFrom(sensor).encodePrettily());
			} else {
				System.out.println(handler.cause().toString());
				routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
					.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
			}
		});
	}
	
	private void getValueSensorByTimestamp(RoutingContext routingContext) {
		mySQLPool.preparedQuery("SELECT * FROM proyectopinza.sensor WHERE timeStamp = " + 
				routingContext.request().getParam("timeStamp"), res -> {
			if (res.succeeded()) {
				RowSet<Row> resultSet = res.result();
				System.out.println("El número de elementos obtenidos es " + resultSet.size());
				JsonArray result = new JsonArray();
				for (Row row : resultSet) {
					result.add(JsonObject.mapFrom(new Sensor(row.getInteger("idsensor"),
							row.getLong("timeStamp"),
							row.getInteger("base"),
							row.getInteger("valor"))));
				}
				
				routingContext.response().setStatusCode(200).putHeader("conent-type", "application/json")
						.end(result.encodePrettily());
			} else {
				routingContext.response().setStatusCode(401).putHeader("content-type", "application/json") 
					.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
			}
		});
	}
	
	private void getValueBySensor(RoutingContext routingContext) {
		mySQLPool.preparedQuery("SELECT * FROM proyectopinza.sensor WHERE idsensor = " + routingContext.request().getParam("idsensor"), res -> {
			if (res.succeeded()) {
				RowSet<Row> resultSet = res.result();
				System.out.println("El número de elementos obtenidos es " + resultSet.size());
				JsonArray result = new JsonArray();
				for (Row row : resultSet) {
					result.add(JsonObject.mapFrom(new Sensor(row.getInteger("idsensor"),
							row.getLong("timeStamp"),							
							row.getInteger("base"),
							row.getInteger("valor"))));
				}
				
				routingContext.response().setStatusCode(200).putHeader("conent-type", "application/json")
						.end(result.encodePrettily());
			} else {
				routingContext.response().setStatusCode(401).putHeader("content-type", "application/json") 
					.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
			}
		});
	}
	
}
