import dayjs from 'dayjs';
import { AdminGender } from 'app/shared/model/enumerations/admin-gender.model';

export interface IPlatformAdminUser {
  id?: number;
  fullName?: string;
  emailAddress?: string;
  nickName?: string | null;
  gender?: keyof typeof AdminGender;
  mobilePhone?: string | null;
  lastLoginDate?: dayjs.Dayjs | null;
  birthDate?: dayjs.Dayjs;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IPlatformAdminUser> = {};
