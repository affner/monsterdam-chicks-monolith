import dayjs from 'dayjs';

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
  isDeleted?: boolean;
}

export const defaultValue: Readonly<IPostFeed> = {
  isHidden: false,
  pinnedPost: false,
  isDeleted: false,
};
