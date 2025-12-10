import dayjs from 'dayjs';
import { ModerationActionType } from 'app/shared/model/enumerations/moderation-action-type.model';

export interface IModerationAction {
  id?: number;
  actionType?: keyof typeof ModerationActionType;
  reason?: string | null;
  actionDate?: dayjs.Dayjs | null;
  durationDays?: string | null;
  ticketId?: number | null;
}

export const defaultValue: Readonly<IModerationAction> = {};
