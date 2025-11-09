import dayjs from 'dayjs';
import { ITimeSlot } from 'app/shared/model/time-slot.model';
import { IUser } from 'app/shared/model/user.model';
import { IPayment } from 'app/shared/model/payment.model';
import { BookingStatus } from 'app/shared/model/enumerations/booking-status.model';

export interface IBooking {
  id?: number;
  bookingDate?: dayjs.Dayjs;
  status?: keyof typeof BookingStatus;
  timeSlot?: ITimeSlot | null;
  user?: IUser | null;
  payment?: IPayment | null;
}

export const defaultValue: Readonly<IBooking> = {};
