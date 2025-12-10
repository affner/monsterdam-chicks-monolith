import dayjs from 'dayjs';
import { IUserLite } from 'app/shared/model/user-lite.model';
import { MoneyWithdrawStatus } from 'app/shared/model/enumerations/money-withdraw-status.model';

export interface IMoneyWithdraw {
  id?: number;
  amount?: number;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  withdrawStatus?: keyof typeof MoneyWithdrawStatus;
  creator?: IUserLite | null;
}

export const defaultValue: Readonly<IMoneyWithdraw> = {
  isDeleted: false,
};
