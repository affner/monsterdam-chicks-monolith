import dayjs from 'dayjs';
import { IUserLite } from 'app/shared/model/user-lite.model';
import { AuctionStatus } from 'app/shared/model/enumerations/auction-status.model';

export interface IAuction {
  id?: number;
  title?: string;
  description?: string | null;
  startingPrice?: number;
  currentPrice?: number | null;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs;
  auctionStatus?: keyof typeof AuctionStatus;
  createdDate?: dayjs.Dayjs;
  lastModifiedDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  lastModifiedBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
  creator?: IUserLite | null;
  winner?: IUserLite | null;
}

export const defaultValue: Readonly<IAuction> = {};
