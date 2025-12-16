import dayjs from 'dayjs';
import { IPayment } from 'app/shared/model/payment.model';
import { GenericStatus } from 'app/shared/model/enumerations/generic-status.model';

export interface IPaymentProviderEvent {
  id?: number;
  providerName?: string;
  eventType?: string;
  eventId?: string;
  payloadJson?: string;
  receivedAt?: dayjs.Dayjs;
  processedAt?: dayjs.Dayjs | null;
  processingStatus?: keyof typeof GenericStatus;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  payment?: IPayment | null;
}

export const defaultValue: Readonly<IPaymentProviderEvent> = {};
