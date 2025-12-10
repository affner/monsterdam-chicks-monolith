import dayjs from 'dayjs';

export interface IVideoStory {
  id?: number;
  thumbnailS3Key?: string;
  contentS3Key?: string;
  duration?: string | null;
  likeCount?: number | null;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  creatorId?: number;
}

export const defaultValue: Readonly<IVideoStory> = {
  isDeleted: false,
};
