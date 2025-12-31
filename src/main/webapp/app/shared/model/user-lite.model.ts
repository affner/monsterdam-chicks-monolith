import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { IUserSettings } from 'app/shared/model/user-settings.model';
import { ICountry } from 'app/shared/model/country.model';
import { IChatRoom } from 'app/shared/model/chat-room.model';
import { UserGender } from 'app/shared/model/enumerations/user-gender.model';

export interface IUserLite {
  id?: number;
  thumbnailS3Key?: string | null;
  thumbnailContentType?: string | null;
  thumbnail?: string | null;
  birthDate?: dayjs.Dayjs;
  gender?: keyof typeof UserGender;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  nickName?: string;
  fullName?: string;
  profile?: IUserProfile | null;
  settings?: IUserSettings | null;
  countryOfBirth?: ICountry | null;
  chatRooms?: IChatRoom[] | null;
}

export const defaultValue: Readonly<IUserLite> = {};
