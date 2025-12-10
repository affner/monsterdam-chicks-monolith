import dayjs from 'dayjs';
import { IPostFeed } from 'app/shared/model/post-feed.model';

export interface IPostComment {
  id?: number;
  commentContent?: string;
  likeCount?: number | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  commenterId?: number;
  post?: IPostFeed | null;
  responseTo?: IPostComment | null;
}

export const defaultValue: Readonly<IPostComment> = {
  isDeleted: false,
};
