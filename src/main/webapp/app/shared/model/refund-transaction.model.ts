import dayjs from 'dayjs';
import { IPayment } from 'app/shared/model/payment.model';
import { GenericStatus } from 'app/shared/model/enumerations/generic-status.model';

export interface IRefundTransaction {
  id?: number;
  amount?: number;
  currency?: string;
  reason?: string | null;
  paymentReference?: string | null;
  providerChargeId?: string | null;
  status?: keyof typeof GenericStatus;
  createdDate?: dayjs.Dayjs;
  processedAt?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  payment?: IPayment | null;
}

export const defaultValue: Readonly<IRefundTransaction> = {};
