export const ErpConnectionType = {
  LOCAL: 'LOCAL',
  MIKRO_V16: 'MIKRO_V16',
  UYUMSOFT: 'UYUMSOFT',
  MIKRO_V15: 'MIKRO_V15',
} as const;

export type ErpConnectionType = typeof ErpConnectionType[keyof typeof ErpConnectionType];

export interface IApiParameters {
  erpApiActive?: boolean | null;
  depoNo?: number[] | null;
  username?: string | null;
  password?: string | null;
}

export interface IAurCompany {
  id?: number;
  companyCode?: number | null;
  companyName?: string | null;
  erpType?: ErpConnectionType | null;
  apiEndPoint?: string | null;
  apiParameters?: IApiParameters | null;
}

export const defaultValue: Readonly<IAurCompany> = {};
