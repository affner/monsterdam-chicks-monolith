import { IPostPoll } from 'app/shared/model/post-poll.model';

export interface IPollOption {
  id?: number;
  optionDescription?: string;
  poll?: IPostPoll | null;
}

export const defaultValue: Readonly<IPollOption> = {};
