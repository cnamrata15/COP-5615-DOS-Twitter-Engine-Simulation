

import java.util.Calendar
import scala.collection.mutable.LinkedList
import akka.actor.Actor
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random
import spray.json._
import spray.json.DefaultJsonProtocol._
import akka.actor.actorRef2Scala


class TwitterUser2  extends Actor
{
  var userId = "";
  var belongsToServerId = 0;
  var followers = new  scala.collection.mutable.MutableList[akka.actor.ActorRef]();
  var created = Calendar.getInstance().getTime();
  var messages = new LinkedList[UserMessage];
  var timeLine = new scala.collection.mutable.TreeSet[Tweet2]();
  var c = Calendar.getInstance().getTime().getMinutes()*60 + Calendar.getInstance().getTime().getSeconds();

  
  def receive()=
  {
    case userIni: userInit =>
      {
        userId = getUserId(userIni.ServerId, userIni.userId);
        belongsToServerId = userIni.ServerId;
        userIni.CurrentUser  ! new genRandomTweet(userIni.CurrentUser , userIni.CurrentServer )
        context.system.scheduler.schedule(10 milliseconds, Global.createTweetInterval, userIni.CurrentUser, new genRandomTweet(userIni.CurrentUser, userIni.CurrentServer ))
        Global.users+=1;
       
        
        if(Global.users % 100000 == 0)
        {
          
          println("USERS = " + Global.users);
        }
      }
      
    case ranTweet: genRandomTweet=>
      {
        var tw_content =  Random.alphanumeric.take(140).mkString;
        var tw = new Tweet2(ranTweet.CurrentUser );
        tw.content = tw_content;
        tw.creator = this.userId;
        
        tw.created  = Calendar.getInstance().getTime();
     //   println("Before " +this.twitterUser.timeLine.size);
   //     println(tw_content)
        
        timeLine.add(tw);
    //    println(timeLine.size);
//        println("Tweeted " + tw_content);
      //  context.system.scheduler.scheduleOnce(Global.createTweetInterval, ranTweet.CurrentUser, new genRandomTweet(ranTweet.CurrentUser, ranTweet.CurrentServer ))
        for(user<-this.followers )
        {
          user!addtoTimeLine(ranTweet.CurrentUser, user, tw_content, this.userId );
        }
        Global.tweets+=1;
       
        
        if(Global.tweets % 100000 == 0)
        {
         
          println("TWEETS " + Global.tweets);
        }
      }
      
    case nonRanTweet: nonRanTweet=>
    {
      val tw = new Tweet2(nonRanTweet.CurrentUser );
      tw.content  = nonRanTweet.content ;
      tw.creator = this.userId;
      tw.created  = Calendar.getInstance().getTime();
      this.timeLine.add(tw);
      nonRanTweet.requestContext.complete(List(tw.content, tw.created.toLocaleString(), "Status:OK").toJson.toString)
      for(user<-this.followers )
        {
          user!addtoTimeLine(nonRanTweet.CurrentUser, user, tw.content, this.userId );
        }
    }
    
    case getTimeL: getTimeline=>
      {
        var count = 1;
        var A:Map[String,List[String]] = Map()
        println(this.timeLine.size)
        for(x<-this.timeLine )
        {
          A+= ("Timeline Entry " + count  -> List(x.created.toString(), x.creator , x.content)  )
          count+=1;
        } 
        getTimeL.requestContext.complete(A.toJson.prettyPrint)
      }
      
    case addtoTimeLine: addtoTimeLine=>
      {
        var tw_content =  addtoTimeLine.content ;
        var tw = new Tweet2(addtoTimeLine.CurrentUser );
        tw.content = tw_content;
        tw.creator = addtoTimeLine.userId;
        
        tw.created  = Calendar.getInstance().getTime();
        this.timeLine.add(tw);
      }
    
    case userF: userFollow=>
      {
        var user = userF.user;
        this.followers += user
        userF.requestContext.complete(Map("Followed"->"True", "STATUS"->"OK").toJson.prettyPrint)
      }
      
    case doretwee: retweet=>
    {
      var c = 0;
      for(x<-timeLine)
      {
        c=c+1
        if(c==doretwee.retweetID)
        {
          //case class addtoTimeLine(CurrentUser: akka.actor.ActorRef, TargetUser: akka.actor.ActorRef, content: String, userId:String) extends Message
          doretwee.userId ! addtoTimeLine(doretwee.userId  , doretwee.targetUserID , x.content , doretwee.userStringID)
          doretwee.requestContext.complete(Map("Retweet"->"OK").toJson.prettyPrint)
        }
      }
      doretwee.requestContext.complete(Map("Retweet"->"FAILED").toJson.prettyPrint)
      
    }
  }
  
  def getUserId(ServerId:Int,userId:Int):String=
  {
    return ServerId+"-"+userId
  }
  
}