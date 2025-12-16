import dayjs from 'dayjs';
import { IPostFeed } from 'app/shared/model/post-feed.model';
import { IUserLite } from 'app/shared/model/user-lite.model';

export interface IPostMention {
  id?: number;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  originPost?: IPostFeed | null;
  mentionedUser?: IUserLite;
}

export const defaultValue: Readonly<IPostMention> = {};
