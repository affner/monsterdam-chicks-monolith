import dayjs from 'dayjs';
import { IPostComment } from 'app/shared/model/post-comment.model';
import { IUserLite } from 'app/shared/model/user-lite.model';

export interface ICommentMention {
  id?: number;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  originPostComment?: IPostComment | null;
  mentionedUser?: IUserLite;
}

export const defaultValue: Readonly<ICommentMention> = {};
