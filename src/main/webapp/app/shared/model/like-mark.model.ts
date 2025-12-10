import dayjs from 'dayjs';
import { LikeEntityKind } from 'app/shared/model/enumerations/like-entity-kind.model';

export interface ILikeMark {
  id?: number;
  entityType?: keyof typeof LikeEntityKind;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  multimediaId?: number | null;
  messageId?: number | null;
  postId?: number | null;
  commentId?: number | null;
  likerId?: number;
}

export const defaultValue: Readonly<ILikeMark> = {
  isDeleted: false,
};
