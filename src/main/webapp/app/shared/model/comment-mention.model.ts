import dayjs from 'dayjs';
import { IPostComment } from 'app/shared/model/post-comment.model';

export interface ICommentMention {
  id?: number;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  mentionedUserId?: number;
  originPostComment?: IPostComment | null;
}

export const defaultValue: Readonly<ICommentMention> = {
  isDeleted: false,
};
