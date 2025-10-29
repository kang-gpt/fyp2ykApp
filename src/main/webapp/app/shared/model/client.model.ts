import dayjs from 'dayjs';
import { ClientTier } from 'app/shared/model/enumerations/client-tier.model';

export interface IClient {
  id?: number;
  name?: string | null;
  description?: string | null;
  age?: number | null;
  dob?: dayjs.Dayjs | null;
  tier?: keyof typeof ClientTier | null;
}

export const defaultValue: Readonly<IClient> = {};
