import dayjs from 'dayjs';
import { IContentPackage } from 'app/shared/model/content-package.model';
import { IUserLite } from 'app/shared/model/user-lite.model';

export interface ISinglePhoto {
  id?: number;
  thumbnailContentType?: string;
  thumbnail?: string;
  thumbnailS3Key?: string;
  contentContentType?: string;
  content?: string;
  contentS3Key?: string;
  likeCount?: number | null;
  isPreview?: boolean;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  contentPackage?: IContentPackage | null;
  creator?: IUserLite;
}

export const defaultValue: Readonly<ISinglePhoto> = {
  isPreview: false,
};
