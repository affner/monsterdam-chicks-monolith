import dayjs from 'dayjs';
import { IPostFeed } from 'app/shared/model/post-feed.model';
import { IUserLite } from 'app/shared/model/user-lite.model';

export interface IPostComment {
  id?: number;
  commentContent?: string;
  likeCount?: number | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  post?: IPostFeed | null;
  responseTo?: IPostComment | null;
  commenter?: IUserLite;
}

export const defaultValue: Readonly<IPostComment> = {};
