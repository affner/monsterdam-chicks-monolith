import dayjs from 'dayjs';

export interface IHelpRelatedArticle {
  id?: number;
  title?: string;
  content?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IHelpRelatedArticle> = {};
