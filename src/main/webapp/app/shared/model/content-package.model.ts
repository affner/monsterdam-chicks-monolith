import dayjs from 'dayjs';

export interface IContentPackage {
  id?: number;
  amount?: number | null;
  videoCount?: number | null;
  imageCount?: number | null;
  isPaidContent?: boolean;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  messageId?: number | null;
  postId?: number | null;
}

export const defaultValue: Readonly<IContentPackage> = {
  isPaidContent: false,
  isDeleted: false,
};
