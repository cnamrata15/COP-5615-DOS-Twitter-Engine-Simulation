

import akka.actor.Actor
import spray.routing._
import spray.http._
import spray.http.MediaTypes._
import spray.routing.Directive.pimpApply
// we want to be able to test it independently, without having to spin up an actor
class RestServiceActor extends Actor with RestService {
  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context
  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}
// this trait defines our service behavior independently from the service actor
trait RestService extends HttpService {
  val myRoute =
    get {
      path("") {
        respondWithMediaType(`text/html`) { // XML is marshalled to `text/xml` by default, so we simply override here
          complete {
            <html>
              <body>
                <h3>Twitter Simulation Services</h3>
                <ul>
                  <li><a href="/tweet/0-10/Hi This is my first tweet">Tweet</a>(Syntax - Current UserID / Tweet String)</li>
                  <li><a href="/dofollow/0-10/0-11">Follow</a>(Syntax - UserID / Target UserID)</li>
                  <li><a href="/dogettimeline/0-10">GetTimeline</a>(Syntax - UserID)</li>
        	  		<li><a href="/doretweet/0-10/0-11/3">Reweet</a>(Syntax - Current USER ID / Target USER ID / Tweet ID</li>
                </ul>
              </body>
            </html>
          }
        }
      } ~ 
      path("dogettimeline" / Segment) { userId=>
        requestContext=>
         // XML is marshalled to `text/xml` by default, so we simply override here
        var Server2Actor = Boot2.context.actorFor("/user/" + "Server2");
        Server2Actor ! getTimeline(userId, requestContext);
     
    }~ 
      path("tweet" / Segment / Segment) { (userId, tweetConent)=>
        requestContext=>
        var Server2Actor = Boot2.context.actorFor("/user/" + "Server2");
        Server2Actor ! doTweet(userId, tweetConent, requestContext)
     
    }~ 
      path("dofollow" / Segment / Segment) { (userId, targetUserId)=>
        requestContext=>
        var Server2Actor = Boot2.context.actorFor("/user/" + "Server2");
        Server2Actor ! dofollow(userId, targetUserId, requestContext)
     
    }~
    path("doretweet" / Segment / Segment / Segment) { (userId, targetUserID,retweetID)=>
        requestContext=>
        var Server2Actor = Boot2.context.actorFor("/user/" + "Server2");
        Server2Actor ! doretweet(userId, targetUserID, retweetID.toInt, requestContext)
     
    }
  }
}