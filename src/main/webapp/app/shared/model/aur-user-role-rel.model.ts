export interface IAurUserRoleRel {
  id?: number;
  userId?: number | null;
  roleId?: number | null;
}

export const defaultValue: Readonly<IAurUserRoleRel> = {};
