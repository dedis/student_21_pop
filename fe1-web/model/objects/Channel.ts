import { Hash } from './Hash';

export type Channel = string;
export const ROOT_CHANNEL: Channel = '/root';

export function channelFromId(value?: Hash) : Channel {
  if (value === undefined) return ROOT_CHANNEL;

  const ch = value.valueOf();
  return `${ROOT_CHANNEL}/${ch}`;
}
