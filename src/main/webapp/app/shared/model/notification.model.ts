import dayjs from 'dayjs';
import { IPostComment } from 'app/shared/model/post-comment.model';
import { IPostFeed } from 'app/shared/model/post-feed.model';
import { IDirectMessage } from 'app/shared/model/direct-message.model';
import { IUserLite } from 'app/shared/model/user-lite.model';
import { NotificationKind } from 'app/shared/model/enumerations/notification-kind.model';

export interface INotification {
  id?: number;
  readDate?: dayjs.Dayjs | null;
  notificationKind?: keyof typeof NotificationKind;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  comment?: IPostComment | null;
  post?: IPostFeed | null;
  message?: IDirectMessage | null;
  recipient?: IUserLite;
  actor?: IUserLite | null;
}

export const defaultValue: Readonly<INotification> = {};
