import dayjs from 'dayjs';
import { LedgerEntryType } from 'app/shared/model/enumerations/ledger-entry-type.model';
import { LedgerAccountType } from 'app/shared/model/enumerations/ledger-account-type.model';
import { LedgerReason } from 'app/shared/model/enumerations/ledger-reason.model';

export interface ILedgerEntry {
  id?: number;
  amount?: number;
  currency?: string;
  entryType?: keyof typeof LedgerEntryType;
  accountType?: keyof typeof LedgerAccountType;
  accountOwnerId?: number | null;
  reason?: keyof typeof LedgerReason;
  referenceType?: string;
  referenceId?: number;
  createdDate?: dayjs.Dayjs;
  createdBy?: string | null;
  deletedDate?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<ILedgerEntry> = {};
