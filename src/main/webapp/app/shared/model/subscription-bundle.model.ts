import dayjs from 'dayjs';
import { IUserLite } from 'app/shared/model/user-lite.model';

export interface ISubscriptionBundle {
  id?: number;
  amount?: number;
  duration?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  creator?: IUserLite | null;
}

export const defaultValue: Readonly<ISubscriptionBundle> = {};
