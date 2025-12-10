import dayjs from 'dayjs';
import { DocumentStatus } from 'app/shared/model/enumerations/document-status.model';
import { ReviewStatus } from 'app/shared/model/enumerations/review-status.model';

export interface IIdentityDocumentReview {
  id?: number;
  documentStatus?: keyof typeof DocumentStatus | null;
  resolutionDate?: dayjs.Dayjs | null;
  reviewStatus?: keyof typeof ReviewStatus | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  ticketId?: number | null;
}

export const defaultValue: Readonly<IIdentityDocumentReview> = {};
