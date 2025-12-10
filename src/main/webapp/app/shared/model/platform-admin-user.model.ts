import dayjs from 'dayjs';
import { AdminGender } from 'app/shared/model/enumerations/admin-gender.model';

export interface IPlatformAdminUser {
  id?: number;
  fullName?: string;
  emailAddress?: string;
  nickName?: string | null;
  gender?: keyof typeof AdminGender;
  mobilePhone?: string | null;
  lastLoginDate?: dayjs.Dayjs;
  birthDate?: dayjs.Dayjs;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
}

export const defaultValue: Readonly<IPlatformAdminUser> = {
  isDeleted: false,
};
