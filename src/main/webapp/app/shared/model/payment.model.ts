import dayjs from 'dayjs';
import { IPaymentMethod } from 'app/shared/model/payment-method.model';
import { IPaymentProvider } from 'app/shared/model/payment-provider.model';
import { IUserLite } from 'app/shared/model/user-lite.model';
import { GenericStatus } from 'app/shared/model/enumerations/generic-status.model';

export interface IPayment {
  id?: number;
  amount?: number;
  currency?: string;
  paymentDate?: dayjs.Dayjs;
  paymentStatus?: keyof typeof GenericStatus;
  paymentReference?: string | null;
  cloudTransactionId?: string | null;
  providerPaymentIntentId?: string | null;
  providerChargeId?: string | null;
  providerCustomerId?: string | null;
  providerPaymentMethodId?: string | null;
  providerEventLastId?: string | null;
  countryCode?: string | null;
  providerFeeAmount?: number | null;
  platformFeeAmount?: number;
  creatorNetAmount?: number;
  taxAmount?: number | null;
  authorizedDate?: dayjs.Dayjs | null;
  capturedDate?: dayjs.Dayjs | null;
  method?: IPaymentMethod | null;
  provider?: IPaymentProvider | null;
  viewer?: IUserLite;
}

export const defaultValue: Readonly<IPayment> = {};
