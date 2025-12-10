import dayjs from 'dayjs';
import { IState } from 'app/shared/model/state.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IStateUserRelation {
  id?: number;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  isDeleted?: boolean;
  state?: IState | null;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<IStateUserRelation> = {
  isDeleted: false,
};
