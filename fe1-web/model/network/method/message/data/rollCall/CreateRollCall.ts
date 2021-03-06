import { OpenedLaoStore } from 'store';
import {
  Hash, Timestamp, Lao, EventTags,
} from 'model/objects';
import { ProtocolError } from 'model/network/ProtocolError';
import { validateDataObject } from 'model/network/validation';
import { ActionType, MessageData, ObjectType } from '../MessageData';
import { checkTimestampStaleness } from '../Checker';

export class CreateRollCall implements MessageData {
  public readonly object: ObjectType = ObjectType.ROLL_CALL;

  public readonly action: ActionType = ActionType.CREATE;

  public readonly id: Hash;

  public readonly name: string;

  public readonly creation: Timestamp;

  public readonly start?: Timestamp;

  public readonly scheduled?: Timestamp;

  public readonly location: string;

  public readonly roll_call_description?: string;

  constructor(msg: Partial<CreateRollCall>) {
    if (!msg.name) {
      throw new ProtocolError("Undefined 'name' parameter encountered during 'CreateRollCall'");
    }
    this.name = msg.name;

    if (!msg.creation) {
      throw new ProtocolError("Undefined 'creation' parameter encountered during 'CreateRollCall'");
    }
    checkTimestampStaleness(msg.creation);
    this.creation = msg.creation;

    if ((msg.start && msg.scheduled) || (!msg.start && !msg.scheduled)) {
      // if both are present or neither
      throw new ProtocolError("The 'CreateRollCall' data should include"
        + "  either 'start' or 'scheduled' values, but not both");
    }

    if (msg.start) {
      if (msg.start < msg.creation) {
        throw new ProtocolError('Invalid timestamp encountered:'
          + " 'start' parameter smaller than 'creation'");
      }
      this.start = msg.start;
    }

    if (msg.scheduled) {
      if (msg.scheduled < msg.creation) {
        throw new ProtocolError('Invalid timestamp encountered:'
          + " 'scheduled' parameter smaller than 'creation'");
      }
      this.scheduled = msg.scheduled;
    }

    if (!msg.location) {
      throw new ProtocolError("Undefined 'location' parameter encountered during 'CreateRollCall'");
    }
    this.location = msg.location;

    if (msg.roll_call_description) this.roll_call_description = msg.roll_call_description;

    if (!msg.id) {
      throw new ProtocolError("Undefined 'id' parameter encountered during 'CreateRollCall'");
    }
    const lao: Lao = OpenedLaoStore.get();
    const expectedHash = Hash.fromStringArray(
      EventTags.ROLL_CALL, lao.id.toString(), lao.creation.toString(), msg.name,
    );
    if (!expectedHash.equals(msg.id)) {
      throw new ProtocolError("Invalid 'id' parameter encountered during 'CreateRollCall':"
        + ' re-computing the value yields a different result');
    }
    this.id = msg.id;
  }

  public static fromJson(obj: any): CreateRollCall {
    const { errors } = validateDataObject(ObjectType.ROLL_CALL, ActionType.CREATE, obj);

    if (errors !== null) {
      throw new ProtocolError(`Invalid create roll call\n\n${errors}`);
    }

    return new CreateRollCall({
      ...obj,
      creation: new Timestamp(obj.creation),
      start: (obj.start !== undefined) ? new Timestamp(obj.start) : undefined,
      scheduled: (obj.scheduled !== undefined) ? new Timestamp(obj.scheduled) : undefined,
      id: new Hash(obj.id),
    });
  }
}
