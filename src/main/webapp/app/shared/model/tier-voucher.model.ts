import { ClientTier } from './enumerations/client-tier.model';

export interface ITierVoucher {
  id?: number;
  tier?: ClientTier;
  voucherType?: string;
}

export const defaultValue: Readonly<ITierVoucher> = {};
