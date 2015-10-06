


import akka.actor._
import akka.actor.Actor
import akka.actor.Props


object Server
{
  var num_of_users = -1
  var ServerActors = new Array[akka.actor.ActorRef](1);
  val system = ActorSystem("twitter");
}


class Server  {
  

  def init(numUsers: Int) 
  {  
    Server.num_of_users  = numUsers;
    println("Creating " + numUsers + " users");
    
    Server.ServerActors = new Array[akka.actor.ActorRef](numUsers);
    var ServerBoss = Server.system.actorOf(Props[ServerBoss], "ServerBoss");
    
    ServerBoss ! initServer(numUsers, -1);
    
    //generate followers
/*    for( index <- 0 to numUsers - 1)
    {
         Server.ServerActors(index) ! genFollowers(numUsers, numUsers/5);
    }
    
    
    
    for( index <- 0 to numUsers - 1)
    {
         Server.ServerActors(index) ! printTimeline;
    }
    */
    
  }
}

class ServerBoss extends Actor
{
  var init_count = 0;
  var init_f_count = 0;
  var last_tweeted_user_index = 0;
  def receive() =
  {
    case ini:initServer =>
      {
        for( index <- 0 to Server.num_of_users - 1)
        {
    	 Server.ServerActors(index) = Server.system.actorOf(Props[ServerActor], "ServerActor" + index);
         Server.ServerActors(index) ! initServer(Server.num_of_users, index)
        }
      }
    case init_d: init_done =>
      {
        init_count+=1;
        if(init_count == Server.num_of_users )
        {
        	for( index <- 0 to Server.num_of_users - 1)
        	{
        		Server.ServerActors(index) ! genFollowers(Server.num_of_users, Server.num_of_users/5);
        	}
        }
      }
    case init_f_done: init_follow_done=>
      {
        init_f_count+=1;
        if(init_f_count == Server.num_of_users )
        {
          println("Completed Followers Init");
        }
        self!start_tweet_gen();
      }
    case tweet_gen: start_tweet_gen=>
      {
        if(last_tweeted_user_index  == Server.num_of_users )
        {
          last_tweeted_user_index = 0;
        }
        Server.ServerActors(last_tweeted_user_index)!randTweet();
        last_tweeted_user_index+=1;
        self!start_tweet_gen();
      }
  }

}
