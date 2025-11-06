import dayjs from 'dayjs';
import { ITimeSlot } from 'app/shared/model/time-slot.model';
import { BookingStatus } from 'app/shared/model/enumerations/booking-status.model';

export interface IBooking {
  id?: number;
  bookingDate?: dayjs.Dayjs;
  status?: keyof typeof BookingStatus;
  timeSlot?: ITimeSlot | null;
}

export const defaultValue: Readonly<IBooking> = {};
