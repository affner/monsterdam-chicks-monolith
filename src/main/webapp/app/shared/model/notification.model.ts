import dayjs from 'dayjs';
import { IPostComment } from 'app/shared/model/post-comment.model';
import { IPostFeed } from 'app/shared/model/post-feed.model';
import { IDirectMessage } from 'app/shared/model/direct-message.model';
import { NotificationKind } from 'app/shared/model/enumerations/notification-kind.model';

export interface INotification {
  id?: number;
  readDate?: dayjs.Dayjs | null;
  notificationKind?: keyof typeof NotificationKind;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  commentedUserId?: number | null;
  messagedUserId?: number | null;
  mentionerIdInPost?: number | null;
  mentionerIdInComment?: number | null;
  comment?: IPostComment | null;
  post?: IPostFeed | null;
  message?: IDirectMessage | null;
}

export const defaultValue: Readonly<INotification> = {
  isDeleted: false,
};
