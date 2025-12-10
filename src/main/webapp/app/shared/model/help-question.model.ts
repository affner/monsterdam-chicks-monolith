import { IHelpSubcategory } from 'app/shared/model/help-subcategory.model';

export interface IHelpQuestion {
  id?: number;
  title?: string;
  content?: string;
  isDeleted?: boolean;
  subcategory?: IHelpSubcategory | null;
}

export const defaultValue: Readonly<IHelpQuestion> = {
  isDeleted: false,
};
