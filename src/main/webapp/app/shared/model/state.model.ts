import dayjs from 'dayjs';

export interface IState {
  id?: number;
  stateName?: string;
  isoCode?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  countryId?: number;
}

export const defaultValue: Readonly<IState> = {
  isDeleted: false,
};
