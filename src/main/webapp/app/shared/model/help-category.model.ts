export interface IHelpCategory {
  id?: number;
  name?: string;
  isDeleted?: boolean;
}

export const defaultValue: Readonly<IHelpCategory> = {
  isDeleted: false,
};
