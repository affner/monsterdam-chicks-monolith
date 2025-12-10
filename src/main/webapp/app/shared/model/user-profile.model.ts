import dayjs from 'dayjs';
import { IState } from 'app/shared/model/state.model';

export interface IUserProfile {
  id?: number;
  emailContact?: string;
  profilePhotoS3Key?: string | null;
  coverPhotoS3Key?: string | null;
  mainContentUrl?: string | null;
  mobilePhone?: string | null;
  websiteUrl?: string | null;
  amazonWishlistUrl?: string | null;
  lastLoginDate?: dayjs.Dayjs;
  biography?: string | null;
  isFree?: boolean | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  stateOfResidence?: IState | null;
}

export const defaultValue: Readonly<IUserProfile> = {
  isFree: false,
  isDeleted: false,
};
