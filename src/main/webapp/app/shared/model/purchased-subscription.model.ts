import dayjs from 'dayjs';
import { IUserLite } from 'app/shared/model/user-lite.model';
import { IPayment } from 'app/shared/model/payment.model';
import { ISubscriptionPlanOffer } from 'app/shared/model/subscription-plan-offer.model';
import { PurchasedSubscriptionStatus } from 'app/shared/model/enumerations/purchased-subscription-status.model';

export interface IPurchasedSubscription {
  id?: number;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  subscriptionStatus?: keyof typeof PurchasedSubscriptionStatus;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  viewer?: IUserLite;
  creator?: IUserLite;
  payment?: IPayment | null;
  subscriptionPlanOffer?: ISubscriptionPlanOffer | null;
}

export const defaultValue: Readonly<IPurchasedSubscription> = {};
