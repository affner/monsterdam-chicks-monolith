import dayjs from 'dayjs';
import { IUserLite } from 'app/shared/model/user-lite.model';

export interface ITrialLink {
  id?: number;
  linkCode?: string;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  freeDays?: number | null;
  isUsed?: boolean;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  creator?: IUserLite | null;
}

export const defaultValue: Readonly<ITrialLink> = {
  isUsed: false,
  isDeleted: false,
};
