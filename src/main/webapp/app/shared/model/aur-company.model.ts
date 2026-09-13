export interface IAurCompany {
  id?: number;
  companyCode?: number | null;
  companyName?: string | null;
  erpTipi?: string | null;
  apiEndPoint?: string | null;
  apiParameters?: string | null;
}

export const defaultValue: Readonly<IAurCompany> = {};
