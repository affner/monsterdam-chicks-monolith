import dayjs from 'dayjs';
import { IPlatformAdminUser } from 'app/shared/model/platform-admin-user.model';
import { IIdentityDocumentReview } from 'app/shared/model/identity-document-review.model';
import { IUserLite } from 'app/shared/model/user-lite.model';
import { TicketStatus } from 'app/shared/model/enumerations/ticket-status.model';
import { TicketType } from 'app/shared/model/enumerations/ticket-type.model';

export interface IAssistanceTicket {
  id?: number;
  subject?: string;
  description?: string;
  status?: keyof typeof TicketStatus;
  type?: keyof typeof TicketType;
  openedAt?: dayjs.Dayjs | null;
  closedAt?: dayjs.Dayjs | null;
  comments?: string | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  assignedAdmin?: IPlatformAdminUser | null;
  documentsReview?: IIdentityDocumentReview | null;
  user?: IUserLite;
}

export const defaultValue: Readonly<IAssistanceTicket> = {};
