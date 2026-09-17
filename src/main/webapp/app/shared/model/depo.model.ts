export interface IDepo {
  id?: number;
  code?: number | null;
  name?: string | null;
  companyCode?: number | null;
}

export const defaultValue: Readonly<IDepo> = {};
