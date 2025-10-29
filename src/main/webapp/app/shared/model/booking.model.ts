import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { ICourt } from 'app/shared/model/court.model';

export interface IBooking {
  id?: number;
  startTime?: string | null;
  endTime?: string | null;
  user?: IUser | null;
  court?: ICourt | null;
}

export const defaultValue: Readonly<IBooking> = {};
