import dayjs from 'dayjs';
import { IPayment } from 'app/shared/model/payment.model';

export interface IPurchasedContent {
  id?: number;
  rating?: number | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  viewerId?: number;
  payment?: IPayment | null;
}

export const defaultValue: Readonly<IPurchasedContent> = {
  isDeleted: false,
};
