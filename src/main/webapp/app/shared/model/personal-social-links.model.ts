import dayjs from 'dayjs';
import { ISocialNetwork } from 'app/shared/model/social-network.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IPersonalSocialLinks {
  id?: number;
  normalImageS3Key?: string | null;
  thumbnailIconS3Key?: string | null;
  socialLink?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  socialNetwork?: ISocialNetwork | null;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<IPersonalSocialLinks> = {};
