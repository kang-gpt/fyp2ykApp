import dayjs from 'dayjs';
import { ICourt } from 'app/shared/model/court.model';

export interface ITimeSlot {
  id?: number;
  startTime?: dayjs.Dayjs;
  endTime?: dayjs.Dayjs;
  court?: ICourt | null;
}

export const defaultValue: Readonly<ITimeSlot> = {};
