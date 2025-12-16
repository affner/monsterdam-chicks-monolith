import dayjs from 'dayjs';
import { IUserLite } from 'app/shared/model/user-lite.model';
import { MoneyWithdrawStatus } from 'app/shared/model/enumerations/money-withdraw-status.model';

export interface IMoneyWithdraw {
  id?: number;
  amount?: number;
  currency?: string;
  payoutProviderName?: string | null;
  payoutReferenceId?: string | null;
  processedAt?: dayjs.Dayjs | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  withdrawStatus?: keyof typeof MoneyWithdrawStatus;
  creator?: IUserLite | null;
}

export const defaultValue: Readonly<IMoneyWithdraw> = {};
