import dayjs from 'dayjs';
import { IUserLite } from 'app/shared/model/user-lite.model';
import { UserEventStatus } from 'app/shared/model/enumerations/user-event-status.model';

export interface IUserEvent {
  id?: number;
  title?: string;
  description?: string;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  creatorEventStatus?: keyof typeof UserEventStatus | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  creator?: IUserLite | null;
}

export const defaultValue: Readonly<IUserEvent> = {
  isDeleted: false,
};
