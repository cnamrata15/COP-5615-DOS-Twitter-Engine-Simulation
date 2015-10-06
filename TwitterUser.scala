

import java.util.Calendar
import scala.collection.mutable.LinkedList


class TwitterUser 
{
  var userId = 0;
  var followers = scala.collection.immutable.List(0);
  var created = Calendar.getInstance().getTime();
  var messages = new LinkedList[UserMessage];
  var timeLine =  scala.collection.immutable.SortedSet[Tweet]();
  
}