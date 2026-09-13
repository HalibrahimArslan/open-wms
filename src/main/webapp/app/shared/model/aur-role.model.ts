export interface IAurRole {
  id?: number;
  roleId?: number | null;
  roleName?: string | null;
  companyCode?: number | null;
}

export const defaultValue: Readonly<IAurRole> = {};
