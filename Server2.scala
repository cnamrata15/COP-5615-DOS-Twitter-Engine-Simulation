

import akka.actor._
import akka.actor.Actor
import akka.actor.Props
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global



class Server2 extends Actor {
  
 def receive() = 
 {
   
   case init: initAsMain2 => 
   {
     //create a bunch of twitter users
     println("initAsMain2")
     var ServerId = init.ServerNumber;
     var Server2Actor = init.CurrentServer;
     Server2Actor ! createAnUser(Server2Actor, 0, ServerId);
     //context.system.scheduler.schedule(0 milliseconds,50 milliseconds,Server2Actor,createAnUser(Server2Actor,Global.users ))   
   }
   
   case bootServer: bootserver2=>
     {
       println("Server Booted!")
       var Server2Actor = bootServer.CurrentServer;
       var serverId = Global.connectedServers;
       var params = new initAsMain2(Server2Actor, serverId);
       Server2Actor ! params;
       Global.connectedServers +=1;
     }
     
   case remoteConnect:remoteConnect=>
     {
       var ServerId = Global.connectedServers;
       Global.connectedServers+=1;
       println("Another Machine connected");
       remoteConnect.Server  ! createAnUser(remoteConnect.Server , 0, ServerId);
     }
     
   case createAnUsr: createAnUser=>
     {
       var Server2Actor = createAnUsr.CurrentServer 
       var ATwitterUser = context.actorOf(Props[TwitterUser2], "TwitterUser" + "-" + createAnUsr.ServerId + "-" + createAnUsr.userId);
       ATwitterUser ! userInit(ATwitterUser, createAnUsr.ServerId ,createAnUsr.userId,Server2Actor)
   //    println("Created a twitter user with UserID " + ATwitterUser.toString + " total = " + Global.users );
       Global.users +=1;
       context.system.scheduler.scheduleOnce(Global.createUserInterval ,Server2Actor,createAnUser( Server2Actor,createAnUsr.userId+1, createAnUsr.ServerId  ))
       
     }
   case getTimeL: getTimeline=>
     {
       println("Server Gets Timeline REQ for user " + getTimeL.UserId );
       var TwitterUser = context.actorFor("/user/Server2/" + "TwitterUser-" + getTimeL.UserId);
       TwitterUser ! getTimeL
    //   getTimeL.requestContext.complete("hello");
       
     }
     
   case doTweet: doTweet=>
     {
       println("Server Gets Tweet REQ for user " + doTweet.CurrentUser  );
       var TwitterUser = context.actorFor("/user/Server2/" + "TwitterUser-" + doTweet.CurrentUser);
       TwitterUser ! nonRanTweet(TwitterUser, doTweet.content , doTweet.requestContext );
     }
     
   case dofollow: dofollow=>
     {
       println("Server gets follow req from " + dofollow.userId + " to " + dofollow.targetuser )
       var TwitterUser = context.actorFor("/user/Server2/" + "TwitterUser-" + dofollow.userId );
       var TwitterUser2 = context.actorFor("/user/Server2/" + "TwitterUser-" + dofollow.targetuser );
       TwitterUser2 ! userFollow(TwitterUser, TwitterUser2, dofollow.requestContext )
     }
     
   case doretwee: doretweet=>
     {
       println("Recieved retweet req")
        var TwitterUser = context.actorFor("/user/Server2/" + "TwitterUser-" + doretwee.userId );
       var TwitterUser2 = context.actorFor("/user/Server2/" + "TwitterUser-" + doretwee.targetUserID  );
       TwitterUser2!retweet(TwitterUser, doretwee.userId,TwitterUser2, doretwee.retweetID.toInt, doretwee.requestContext)
       //case class retweet(userId: akka.actor.ActorRef, userStringID: String, targetUserID: akka.actor.ActorRef, retweetID: Int, requestContext: RequestContext) extends Message
       
     }
     
   case "TEST" =>
   {
     sender ! "TEST"
   }
 
 }

}