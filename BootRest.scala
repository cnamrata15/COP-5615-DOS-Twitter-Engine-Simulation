import akka.actor.{ ActorSystem, Props }
import akka.io.IO
import spray.can.Http
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

object BootRest extends App {
  
  
  
  def run(cont : akka.actor.ActorSystem) =
  {
		  // we need an ActorSystem to host our application in
		  implicit val system = cont
		// create and start our service actor
		 val service = system.actorOf(Props[RestServiceActor], "rest-service")
		 implicit val timeout = Timeout(5.seconds)
		// start a new HTTP server on port 8080 with our service actor as the handler
		IO(Http) ? Http.Bind(service, interface = "localhost", port = 8000)
  }
  
}