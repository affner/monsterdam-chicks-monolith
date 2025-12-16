import dayjs from 'dayjs';
import { IPayment } from 'app/shared/model/payment.model';
import { IUserLite } from 'app/shared/model/user-lite.model';

export interface IMoneyGift {
  id?: number;
  amount?: number;
  currency?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  messageId?: number | null;
  postId?: number | null;
  payment?: IPayment | null;
  viewer?: IUserLite | null;
  creator?: IUserLite | null;
}

export const defaultValue: Readonly<IMoneyGift> = {};
