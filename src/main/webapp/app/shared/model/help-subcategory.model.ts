import dayjs from 'dayjs';
import { IHelpCategory } from 'app/shared/model/help-category.model';

export interface IHelpSubcategory {
  id?: number;
  name?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  category?: IHelpCategory | null;
}

export const defaultValue: Readonly<IHelpSubcategory> = {};
