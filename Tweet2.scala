

import scala.collection.mutable.LinkedList
import java.util.Calendar
import akka.actor.ActorRef

class Tweet2(creator: ActorRef) extends Ordered[Tweet2]
{
  
  var content = "";
  var mentions = LinkedList();
  var created = Calendar.getInstance().getTime();
  var creator = "";
  
  def compare(y: Tweet2) = 
    if (this.created.before(y.created)) 
      -1
    else if (this.created.after(y.created)) 
      1
    else
      0
  
 }