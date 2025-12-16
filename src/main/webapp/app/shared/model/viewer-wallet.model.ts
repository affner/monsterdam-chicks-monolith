import dayjs from 'dayjs';
import { IPayment } from 'app/shared/model/payment.model';
import { IUserLite } from 'app/shared/model/user-lite.model';
import { ViewerWalletTransactionType } from 'app/shared/model/enumerations/viewer-wallet-transaction-type.model';

export interface IViewerWallet {
  id?: number;
  amount?: number;
  currency?: string;
  transactionType?: keyof typeof ViewerWalletTransactionType;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  payment?: IPayment | null;
  viewer?: IUserLite | null;
}

export const defaultValue: Readonly<IViewerWallet> = {};
