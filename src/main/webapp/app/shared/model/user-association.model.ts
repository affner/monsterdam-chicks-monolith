import dayjs from 'dayjs';
import { IUserLite } from 'app/shared/model/user-lite.model';
import { AssociationStatus } from 'app/shared/model/enumerations/association-status.model';

export interface IUserAssociation {
  id?: number;
  requestedDate?: dayjs.Dayjs;
  status?: keyof typeof AssociationStatus | null;
  associationToken?: string;
  expiryDate?: dayjs.Dayjs;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  owner?: IUserLite;
}

export const defaultValue: Readonly<IUserAssociation> = {};
