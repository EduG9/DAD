/*package dadVertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class DadVerticle extends AbstractVerticle
{

@Override
public void start(Future<Void> startFuture) {
vertx
        .createHttpServer()
        .requestHandler(r -> {
          r.response().end("<h1>Bienvenido a mi primera aplicacion Vert.x 3</h1>"
          + "Esto es un ejemplo de una Verticle sencillo para probar el despliegue");
        })
        .listen(8081, result -> {
          if (result.succeeded()) {
          System.out.println("Todo correcto");
          } else {
      System.out.print("Fallo");
          }
        });
}
}*/
package dadVertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class DadVerticle extends AbstractVerticle {
	

	@Override
	public void start(Future<Void> startFuture) {
		/*vertx
        	.createHttpServer()
        	.requestHandler(r -> {
        		r.response().end("<h1>Bienvenido a mi primera aplicacion Vert.x 3</h1>"
        				+ "Esto es un ejemplo de una Verticle sencillo para probar el despliegue");
        	})
        	.listen(8081, result -> {
        		if (result.succeeded()) {
        			startFuture.complete();
        		} else {
        			startFuture.fail(result.cause());
        		}
        	});*/
		vertx.deployVerticle(DatabaseVerticle.class.getName());
	}
}