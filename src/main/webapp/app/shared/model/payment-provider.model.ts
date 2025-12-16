import dayjs from 'dayjs';

export interface IPaymentProvider {
  id?: number;
  providerName?: string;
  description?: string | null;
  apiKeyText?: string;
  apiSecretText?: string;
  endpointText?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IPaymentProvider> = {};
