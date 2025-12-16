import dayjs from 'dayjs';
import { IPostFeed } from 'app/shared/model/post-feed.model';

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
  deletedDate?: dayjs.Dayjs | null;
  post?: IPostFeed;
}

export const defaultValue: Readonly<IPostPoll> = {
  isMultiChoice: false,
};
