import dayjs from 'dayjs';

export interface IPostPoll {
  id?: number;
  question?: string;
  isMultiChoice?: boolean;
  endDate?: dayjs.Dayjs;
  duration?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  postId?: number;
}

export const defaultValue: Readonly<IPostPoll> = {
  isMultiChoice: false,
  isDeleted: false,
};
