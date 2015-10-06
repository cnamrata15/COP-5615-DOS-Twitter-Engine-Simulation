




import akka.actor._
import akka.actor.Props
import com.typesafe.config.ConfigFactory



object Boot2 {
  var context:akka.actor.ActorSystem = null;
  
 def main(args: Array[String]) 
{
   if((args.length) == 0){
   println(new java.io.File(".").getCanonicalPath)
      val source = scala.io.Source.fromFile("application.conf")
      val lines = source.mkString
      source.close()
      
      val myConfig = ConfigFactory.load(ConfigFactory.parseString(lines))
	 	context = ActorSystem("TwitterSystem", myConfig);
	 	
       var Server2Actor = context.actorOf(Props[Server2], "Server2");
       Server2Actor ! bootserver2(Server2Actor)
       BootRest.run(context)
   }
   else
   {
     val source = scala.io.Source.fromFile("client.conf")
      val lines = source.mkString
      source.close()
      
      val myConfig = ConfigFactory.load(ConfigFactory.parseString(lines))
	 	context = ActorSystem("PostConnections", myConfig);
     val remoteServer = context.actorFor("akka://TwitterSystem@127.0.0.1:2552/user/Server2")
	 
   }
  }
}