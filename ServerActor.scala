


import akka.actor.Actor
import java.util.Calendar
import scala.util.Random
import akka.actor.actorRef2Scala



object ServerActor
{
  
  var users : List[Int] = List(0);
  val rnd = new scala.util.Random;
  val range = 10000000 to 99999999;
  var total_server_tweets = 0;
}
class ServerActor extends Actor {
  
  var twitterUser = new TwitterUser;
  var ind = -1;
  var user_tweet_count = 0;
  
  def receive() = {
    
    case init: initServer => {
      //println("Creating User");
      twitterUser.created = Calendar.getInstance().getTime();
      this.ind  = init.index;
      
      var userId = (ServerActor.range(ServerActor.rnd.nextInt(ServerActor.range length)));
      while(ServerActor.users.contains(userId))
      {
        userId = (ServerActor.range(ServerActor.rnd.nextInt(ServerActor.range length)));
      }

      ServerActor.users = ServerActor.users ::: List(userId);
      twitterUser.userId = userId;
      println("Generated user(" + this.ind  +") : " + userId);
      sender!init_done();
    }
    
    case genf: genFollowers =>
      {
        println("Starting F generation for " + this.ind );
        val randomf = new scala.util.Random;
        val rangef = 1 to genf.numUsers;
        val no_of_followers = genf.numFollowers;
        var following =  rangef(randomf.nextInt(rangef length));
        for(f <- 0 to no_of_followers)
        {
	        while(following== this.ind )
	        {
	          following =  rangef(randomf.nextInt(rangef length));
	        }

	          this.twitterUser.followers = this.twitterUser.followers ::: List(following);
	        println("Generated follower for (" + this.ind +")" );
        }
        sender!init_follow_done();
      }
      
    case printT: printTimeline=>
      {
        println("entered");
        for(a <- this.twitterUser.timeLine )
        {
          println(a.content + " " + a.creator + " " + a.created );
        }

      }
      
      case rt: randTweet=>
      {
        val tw_content = Random.alphanumeric.take(140).mkString;
        val tw = new Tweet;
        tw.content = tw_content;
        tw.creator = this.twitterUser.userId;
        tw.created  = Calendar.getInstance().getTime();
     //   println("Before " +this.twitterUser.timeLine.size);
        
        this.twitterUser.timeLine =this.twitterUser.timeLine + tw;
        for(usr <- this.twitterUser.followers )
        {
          if(usr != 0)
          {		
        	  Server.ServerActors(usr)!tw;
          }
        }
        Thread.sleep(10);
     //   println("After " + this.twitterUser.timeLine.size);
        this.user_tweet_count +=1;
        ServerActor.total_server_tweets += 1;
        println("USER(" + this.ind + ") tweetCount(" + this.user_tweet_count +") -> " + tw.content + " " + tw.created  + " TotalServerTweets("+ServerActor.total_server_tweets + "), UserTimelineSize(" + this.twitterUser.timeLine.size+")");

      }
      
      case appendTweet: Tweet=>
      {
        this.twitterUser.timeLine =this.twitterUser.timeLine + appendTweet;
    //    Thread.sleep(10);
      }
  
  }

}