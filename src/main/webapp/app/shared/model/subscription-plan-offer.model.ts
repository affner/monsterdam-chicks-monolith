import dayjs from 'dayjs';
import { IUserLite } from 'app/shared/model/user-lite.model';
import { OfferPromotionType } from 'app/shared/model/enumerations/offer-promotion-type.model';

export interface ISubscriptionPlanOffer {
  id?: number;
  freeDaysDuration?: string | null;
  discountPercentage?: number | null;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  subscriptionsLimit?: number | null;
  promotionType?: keyof typeof OfferPromotionType;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  creator?: IUserLite | null;
}

export const defaultValue: Readonly<ISubscriptionPlanOffer> = {};
