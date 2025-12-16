import dayjs from 'dayjs';
import { IUserLite } from 'app/shared/model/user-lite.model';

export interface IPostFeed {
  id?: number;
  postContent?: string;
  isHidden?: boolean | null;
  pinnedPost?: boolean | null;
  likeCount?: number | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  creator?: IUserLite;
}

export const defaultValue: Readonly<IPostFeed> = {
  isHidden: false,
  pinnedPost: false,
};
