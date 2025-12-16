import dayjs from 'dayjs';
import { IUserLite } from 'app/shared/model/user-lite.model';
import { MoneyEarningType } from 'app/shared/model/enumerations/money-earning-type.model';

export interface IMoneyEarning {
  id?: number;
  amount?: number;
  currency?: string;
  transactionType?: keyof typeof MoneyEarningType;
  sourceType?: string;
  sourceId?: number;
  isPaid?: boolean;
  payoutId?: number | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  creator?: IUserLite | null;
}

export const defaultValue: Readonly<IMoneyEarning> = {
  isPaid: false,
};
