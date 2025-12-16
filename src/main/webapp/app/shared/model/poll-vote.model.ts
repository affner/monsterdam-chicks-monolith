import dayjs from 'dayjs';
import { IPollOption } from 'app/shared/model/poll-option.model';
import { IUserLite } from 'app/shared/model/user-lite.model';

export interface IPollVote {
  id?: number;
  createdDate?: dayjs.Dayjs;
  pollOption?: IPollOption;
  voter?: IUserLite;
}

export const defaultValue: Readonly<IPollVote> = {};
