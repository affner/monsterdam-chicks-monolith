import dayjs from 'dayjs';
import { IPostFeed } from 'app/shared/model/post-feed.model';

export interface IPostMention {
  id?: number;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  mentionedUserId?: number;
  originPost?: IPostFeed | null;
}

export const defaultValue: Readonly<IPostMention> = {
  isDeleted: false,
};
