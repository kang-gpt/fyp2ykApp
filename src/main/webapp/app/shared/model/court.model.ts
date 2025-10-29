import { ISport } from 'app/shared/model/sport.model';

export interface ICourt {
  id?: number;
  name?: string | null;
  sport?: ISport | null;
}

export const defaultValue: Readonly<ICourt> = {};
