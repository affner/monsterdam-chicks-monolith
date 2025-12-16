import dayjs from 'dayjs';
import { IHelpSubcategory } from 'app/shared/model/help-subcategory.model';

export interface IHelpQuestion {
  id?: number;
  title?: string;
  content?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  subcategory?: IHelpSubcategory | null;
}

export const defaultValue: Readonly<IHelpQuestion> = {};
