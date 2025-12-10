import dayjs from 'dayjs';
import { HashtagType } from 'app/shared/model/enumerations/hashtag-type.model';

export interface IHashTag {
  id?: number;
  tagName?: string;
  hashtagType?: keyof typeof HashtagType;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
}

export const defaultValue: Readonly<IHashTag> = {
  isDeleted: false,
};
