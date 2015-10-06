
import scala.concurrent.duration._
import akka.actor.ActorSystem

object Global {
  
  var connectedServers = 0;
  var users = 0;
  var tweets = 0;
  var createUserInterval = 1000 milliseconds; //1000 milliseconds;
  var createTweetInterval = 1000 milliseconds; //1000 milliseconds;
  var createFollowerInterval = 0;
  

}