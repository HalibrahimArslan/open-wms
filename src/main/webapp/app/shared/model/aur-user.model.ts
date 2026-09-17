export interface IAurUser {
  id?: number;
  userId?: number | null;
  userName?: string | null;
  password?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  companyCode?: number | null;
}

export const defaultValue: Readonly<IAurUser> = {};
