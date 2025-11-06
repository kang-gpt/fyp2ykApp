import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface IPayment {
  id?: number;
  amount?: number;
  paymentDate?: dayjs.Dayjs | null;
  status?: string;
  qrCodeUrl?: string | null;
  transactionId?: string | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IPayment> = {};
