import dayjs from 'dayjs';
import { IUserLite } from 'app/shared/model/user-lite.model';
import { FeedbackType } from 'app/shared/model/enumerations/feedback-type.model';

export interface IFeedback {
  id?: number;
  content?: string;
  feedbackDate?: dayjs.Dayjs;
  feedbackRating?: number | null;
  feedbackType?: keyof typeof FeedbackType | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  creator?: IUserLite | null;
}

export const defaultValue: Readonly<IFeedback> = {
  isDeleted: false,
};
