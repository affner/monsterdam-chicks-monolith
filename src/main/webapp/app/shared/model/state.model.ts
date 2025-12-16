import dayjs from 'dayjs';
import { ICountry } from 'app/shared/model/country.model';

export interface IState {
  id?: number;
  stateName?: string;
  isoCode?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  country?: ICountry;
}

export const defaultValue: Readonly<IState> = {};
