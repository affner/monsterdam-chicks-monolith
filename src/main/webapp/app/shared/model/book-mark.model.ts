import dayjs from 'dayjs';
import { IUserLite } from 'app/shared/model/user-lite.model';
import { IPostFeed } from 'app/shared/model/post-feed.model';
import { IDirectMessage } from 'app/shared/model/direct-message.model';

export interface IBookMark {
  id?: number;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  user?: IUserLite | null;
  post?: IPostFeed | null;
  message?: IDirectMessage | null;
}

export const defaultValue: Readonly<IBookMark> = {};
