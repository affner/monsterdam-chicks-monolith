import dayjs from 'dayjs';
import { IContentPackage } from 'app/shared/model/content-package.model';
import { IUserLite } from 'app/shared/model/user-lite.model';

export interface ISingleVideo {
  id?: number;
  thumbnailContentType?: string;
  thumbnail?: string;
  thumbnailS3Key?: string;
  contentContentType?: string;
  content?: string;
  contentS3Key?: string;
  duration?: string | null;
  likeCount?: number | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  contentPackage?: IContentPackage | null;
  creator?: IUserLite;
}

export const defaultValue: Readonly<ISingleVideo> = {};
