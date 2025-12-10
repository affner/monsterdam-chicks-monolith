import dayjs from 'dayjs';
import { IPayment } from 'app/shared/model/payment.model';

export interface IMoneyGift {
  id?: number;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  messageId?: number | null;
  postId?: number | null;
  viewerId?: number;
  creatorId?: number;
  payment?: IPayment | null;
}

export const defaultValue: Readonly<IMoneyGift> = {
  isDeleted: false,
};
