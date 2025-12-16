import dayjs from 'dayjs';
import { IContentPackage } from 'app/shared/model/content-package.model';
import { IPayment } from 'app/shared/model/payment.model';
import { IUserLite } from 'app/shared/model/user-lite.model';

export interface IPurchasedContent {
  id?: number;
  rating?: number | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  contentPackage?: IContentPackage;
  payment?: IPayment | null;
  viewer?: IUserLite | null;
  creator?: IUserLite | null;
}

export const defaultValue: Readonly<IPurchasedContent> = {};
