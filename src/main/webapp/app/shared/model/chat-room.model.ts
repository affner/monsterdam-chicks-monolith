import dayjs from 'dayjs';

export interface IChatRoom {
  id?: number;
  lastAction?: string | null;
  lastConnectionDate?: dayjs.Dayjs | null;
  muted?: boolean | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  participantId?: number | null;
}

export const defaultValue: Readonly<IChatRoom> = {
  muted: false,
  isDeleted: false,
};
