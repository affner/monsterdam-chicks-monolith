import dayjs from 'dayjs';
import { IChatRoom } from 'app/shared/model/chat-room.model';

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
  isDeleted?: boolean;
  repliedStoryId?: number | null;
  senderId?: number;
  responseTo?: IDirectMessage | null;
  chatRoom?: IChatRoom | null;
}

export const defaultValue: Readonly<IDirectMessage> = {
  isHidden: false,
  isDeleted: false,
};
