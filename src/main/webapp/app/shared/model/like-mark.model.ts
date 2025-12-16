import dayjs from 'dayjs';
import { IUserLite } from 'app/shared/model/user-lite.model';
import { LikeEntityKind } from 'app/shared/model/enumerations/like-entity-kind.model';

export interface ILikeMark {
  id?: number;
  entityType?: keyof typeof LikeEntityKind;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  multimediaId?: number | null;
  messageId?: number | null;
  postId?: number | null;
  commentId?: number | null;
  liker?: IUserLite;
}

export const defaultValue: Readonly<ILikeMark> = {};
