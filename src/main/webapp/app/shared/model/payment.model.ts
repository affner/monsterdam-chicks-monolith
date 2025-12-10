import dayjs from 'dayjs';
import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { IPaymentProvider } from 'app/shared/model/payment-provider.model';
import { GenericStatus } from 'app/shared/model/enumerations/generic-status.model';

export interface IPayment {
  id?: number;
  amount?: number;
  paymentDate?: dayjs.Dayjs;
  paymentStatus?: keyof typeof GenericStatus;
  paymentReference?: string | null;
  cloudTransactionId?: string | null;
  viewerId?: number;
  method?: IPaymentMethod | null;
  provider?: IPaymentProvider | null;
}

export const defaultValue: Readonly<IPayment> = {};
