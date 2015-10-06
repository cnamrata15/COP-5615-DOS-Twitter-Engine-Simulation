

import scala.collection.mutable.LinkedList
import java.util.Calendar

class Tweet extends Ordered[Tweet]
{
  var creator = -1;
  var content = "";
  var mentions = LinkedList();
  var created = Calendar.getInstance().getTime();
  
  def compare(y: Tweet) = 
    if (this.created.before(y.created)) 
      -1
    else if (this.created.after(y.created)) 
      1
    else
      0
  
 }