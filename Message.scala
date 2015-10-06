import spray.routing.RequestContext
import spray.routing.RequestContext

sealed trait Message
case class initServer(numUsers: Int, index: Int) extends Message
case class genFollowers(numUsers: Int, numFollowers: Int) extends Message
case class randTweet() extends Message
case class printTimeline() extends Message
case class init_done() extends Message
case class init_follow_done() extends Message
case class start_tweet_gen() extends Message


case class remoteConnect2() extends Message
case class initAsMain2(CurrentServer: akka.actor.ActorRef, ServerNumber: Int) extends Message
case class bootserver2(CurrentServer: akka.actor.ActorRef) extends Message
case class createAnUser(CurrentServer: akka.actor.ActorRef, userId: Int, ServerId: Int) extends Message
case class userInit(CurrentUser: akka.actor.ActorRef, ServerId: Int, userId:Int, CurrentServer: akka.actor.ActorRef)
case class genRandomTweet(CurrentServer: akka.actor.ActorRef, CurrentUser: akka.actor.ActorRef) extends Message
case class nonRanTweet(CurrentUser: akka.actor.ActorRef, content: String, requestContext: RequestContext) extends Message
case class doTweet(CurrentUser: String, content: String, requestContext: RequestContext) extends Message
case class getTimeline(UserId: String, requestContext: RequestContext) extends Message
case class addtoTimeLine(CurrentUser: akka.actor.ActorRef, TargetUser: akka.actor.ActorRef, content: String, userId:String) extends Message
case class dofollow(userId: String, targetuser: String, requestContext: RequestContext) extends Message
case class userFollow(user: akka.actor.ActorRef, target: akka.actor.ActorRef, requestContext: RequestContext) extends Message
case class remoteConnect(Server: akka.actor.ActorRef) extends Message
case class doretweet(userId: String, targetUserID: String, retweetID: Int, requestContext: RequestContext) extends Message
case class retweet(userId: akka.actor.ActorRef, userStringID: String, targetUserID: akka.actor.ActorRef, retweetID: Int, requestContext: RequestContext) extends Message