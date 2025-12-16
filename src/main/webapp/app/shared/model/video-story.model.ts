import dayjs from 'dayjs';
import { IUserLite } from 'app/shared/model/user-lite.model';

export interface IVideoStory {
  id?: number;
  thumbnailS3Key?: string;
  contentS3Key?: string;
  duration?: string | null;
  likeCount?: number | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  creator?: IUserLite;
}

export const defaultValue: Readonly<IVideoStory> = {};
