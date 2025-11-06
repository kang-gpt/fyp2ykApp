export interface IClientTier {
  id?: number;
  tierName?: string;
  discountPercentage?: number | null;
}

export const defaultValue: Readonly<IClientTier> = {};
