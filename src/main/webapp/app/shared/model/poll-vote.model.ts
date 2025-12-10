import dayjs from 'dayjs';
import { IPollOption } from 'app/shared/model/poll-option.model';

export interface IPollVote {
  id?: number;
  createdDate?: dayjs.Dayjs;
  votingUserId?: number;
  pollOption?: IPollOption | null;
}

export const defaultValue: Readonly<IPollVote> = {};
