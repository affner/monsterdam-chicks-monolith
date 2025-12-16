import dayjs from 'dayjs';
import { IUserLite } from 'app/shared/model/user-lite.model';
import { ChatRoomType } from 'app/shared/model/enumerations/chat-room-type.model';

export interface IChatRoom {
  id?: number;
  roomType?: keyof typeof ChatRoomType;
  title?: string | null;
  lastAction?: string | null;
  lastConnectionDate?: dayjs.Dayjs | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  participants?: IUserLite[] | null;
}

export const defaultValue: Readonly<IChatRoom> = {};
