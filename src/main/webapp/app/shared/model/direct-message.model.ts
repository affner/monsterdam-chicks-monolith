import dayjs from 'dayjs';
import { IChatRoom } from 'app/shared/model/chat-room.model';
import { IUserLite } from 'app/shared/model/user-lite.model';

export interface IDirectMessage {
  id?: number;
  messageContent?: string;
  readDate?: dayjs.Dayjs | null;
  likeCount?: number | null;
  isHidden?: boolean | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  repliedStoryId?: number | null;
  responseTo?: IDirectMessage | null;
  chatRoom?: IChatRoom;
  sender?: IUserLite;
}

export const defaultValue: Readonly<IDirectMessage> = {
  isHidden: false,
};
