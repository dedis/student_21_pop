package ch.epfl.pop

import java.io.File
import java.nio.charset.StandardCharsets
import akka.NotUsed
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.stream.scaladsl.{Sink, Source}
import ch.epfl.pop.json.JsonMessages._
import ch.epfl.pop.json._
import ch.epfl.pop.json.JsonMessageParser.serializeMessage
import org.iq80.leveldb.impl.Iq80DBFactory.factory
import org.iq80.leveldb.{DB, Options}

import java.util


/**
 * The role of a DBActor is to serve write and read requests to the database.
 */
object DBActor {

  sealed trait DBMessage

  /**
   * Request to write a message in the database
   * @param message the message to write in the database
   * @param rid the id of the client request
   * @param replyTo the actor to respond to
   */
  final case class Write(channel: ChannelName, message : MessageContent, replyTo: ActorRef[Boolean]) extends DBMessage

  final case class Catchup(channel: ChannelName, rid: Int, replyTo: ActorRef[JsonMessageAnswerServer]) extends DBMessage

  final case class Read(channel: ChannelName, id: Hash, replyTo: ActorRef[Option[MessageContent]]) extends DBMessage


  /**
   * Create an actor handling the database
   * @param path the path of the database
   * @return an actor handling the database
   */
  def apply(path: String): Behavior[DBMessage] = database(path, Map.empty)

  private def database(path : String, channelsDB: Map[String, DB]): Behavior[DBMessage] = Behaviors.receive { (ctx, message) =>
     message match {

      case Write(channel, message, replyTo) =>
        val (newChannelsDB, db) = channelsDB.get(channel) match {
          case Some(db) => (channelsDB, db)
          case None =>
            val options = new Options()
            options.createIfMissing(true)
            val db = factory.open(new File(path + "/" + channel), options)
            (channelsDB + (channel -> db), db)
        }

        val id = message.message_id
        ctx.log.debug("Writing message with id: " + util.Arrays.toString(id) + " on channel " + channel)
        db.put(id, serializeMessage(message).getBytes)

        replyTo ! true

        database(path, newChannelsDB)

      case Catchup(channel, rid, replyTo) =>
        if(channelsDB.contains(channel)) {
          val db = channelsDB(channel)
          val it = db.iterator()
          var messages : List[ChannelMessage] = Nil
          while(it.hasNext) {
            val message: Array[Byte] = it.next().getValue
            ctx.log.debug(new String(message, StandardCharsets.UTF_8))
            val messageParsed: ChannelMessage = JsonMessageParser.parseChannelMessage(message)
            messages =  messageParsed :: messages
          }
          replyTo ! AnswerResultArrayMessageServer(result = ChannelMessages(messages.reverse), id = rid)

        }
        else {
          ctx.log.debug("Error invalid channel " + channel + "on catchup.")
          val error = MessageErrorContent(-2, "Invalid resource: channel " + channel + " does not exist.")
          replyTo ! AnswerErrorMessageServer(error = error, id = rid)
        }
        Behaviors.same

      case Read(channel, id, replyTo) =>
        ctx.log.debug("Writing message with id: " + util.Arrays.toString(id) + " on channel " + channel)
         if(channelsDB.contains(channel)) {
           val db = channelsDB(channel)
           val res = db.get(id) match {
             case null => None
             case value => Some(JsonMessageParser.parseChannelMessage(value))
           }
           replyTo ! res
         }
         else {
           replyTo ! None
         }
         Behaviors.same
     }
  }

}
