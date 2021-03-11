package ch.epfl.pop.model.network.method.message.data.lao

import ch.epfl.pop.model.network.method.message.data.{ActionType, MessageData, ObjectType, Parsable}
import ch.epfl.pop.model.objects.{Hash, PublicKey, Timestamp, WitnessSignaturePair}

case class StateLao(
                     id: Hash,
                     name: String,
                     creation: Timestamp,
                     last_modified: Timestamp,
                     organizer: PublicKey,
                     witnesses: List[PublicKey],
                     modification_id: Hash,
                     modification_signatures: List[WitnessSignaturePair]
                   ) {
  private final val _object = ObjectType.LAO
  private final val action = ActionType.STATE
}

object StateLao extends Parsable {
  def apply(
             id: Hash,
             name: String,
             creation: Timestamp,
             last_modified: Timestamp,
             organizer: PublicKey,
             witnesses: List[PublicKey],
             modification_id: Hash,
             modification_signatures: List[WitnessSignaturePair]
           ): StateLao = {
    // FIXME add checks
    new StateLao(id, name, creation, last_modified, organizer, witnesses, modification_id, modification_signatures)
  }

  override def buildFromJson(messageData: MessageData, payload: String): Any = ???
}