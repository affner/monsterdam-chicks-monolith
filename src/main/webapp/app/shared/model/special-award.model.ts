import dayjs from 'dayjs';
import { IUserLite } from 'app/shared/model/user-lite.model';

export interface ISpecialAward {
  id?: number;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  reason?: string | null;
  altSpecialTitle?: string | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  specialTitleId?: number;
  creator?: IUserLite | null;
  viewer?: IUserLite | null;
}

export const defaultValue: Readonly<ISpecialAward> = {};
