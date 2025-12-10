import dayjs from 'dayjs';
import { IUserLite } from 'app/shared/model/user-lite.model';
import { MoneyEarningType } from 'app/shared/model/enumerations/money-earning-type.model';

export interface IMoneyEarning {
  id?: number;
  amount?: number;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  transactionType?: keyof typeof MoneyEarningType;
  creator?: IUserLite | null;
}

export const defaultValue: Readonly<IMoneyEarning> = {
  isDeleted: false,
};
