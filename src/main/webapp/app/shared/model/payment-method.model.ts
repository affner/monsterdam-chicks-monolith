import dayjs from 'dayjs';

export interface IPaymentMethod {
  id?: number;
  methodName?: string;
  tokenText?: string;
  expirationDate?: dayjs.Dayjs | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IPaymentMethod> = {};
