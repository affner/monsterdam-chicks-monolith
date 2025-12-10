import dayjs from 'dayjs';
import { IPayment } from 'app/shared/model/payment.model';
import { ViewerWalletTransactionType } from 'app/shared/model/enumerations/viewer-wallet-transaction-type.model';

export interface IViewerWallet {
  id?: number;
  amount?: number;
  transactionType?: keyof typeof ViewerWalletTransactionType;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  viewerId?: number;
  payment?: IPayment | null;
}

export const defaultValue: Readonly<IViewerWallet> = {
  isDeleted: false,
};
