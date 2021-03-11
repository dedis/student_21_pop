package ch.epfl.pop.model.network.method.message.data.rollCall

import ch.epfl.pop.model.network.method.message.data.{ActionType, MessageData, ObjectType, Parsable}
import ch.epfl.pop.model.objects.{Hash, PublicKey, Timestamp}

case class CloseRollCall(
                           update_id: Hash,
                           closes: Hash,
                           end: Timestamp,
                           attendees: List[PublicKey]
                         ) {
  private final val _object = ObjectType.ROLL_CALL
  private final val action = ActionType.CLOSE
}

object CloseRollCall extends Parsable {
  def apply(
             update_id: Hash,
             closes: Hash,
             end: Timestamp,
             attendees: List[PublicKey]
           ): CloseRollCall = {
    // FIXME add checks
    new CloseRollCall(update_id, closes, end, attendees)
  }

  override def buildFromJson(messageData: MessageData, payload: String): Any = ???
}


