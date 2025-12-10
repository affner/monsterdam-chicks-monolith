import dayjs from 'dayjs';
import { IContentPackage } from 'app/shared/model/content-package.model';

export interface ISingleAudio {
  id?: number;
  thumbnailS3Key?: string;
  contentS3Key?: string;
  duration?: string | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  creatorId?: number;
  contentPackage?: IContentPackage | null;
}

export const defaultValue: Readonly<ISingleAudio> = {
  isDeleted: false,
};
