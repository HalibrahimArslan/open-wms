export interface IAurUserMenus {
  menuId?: number;
  parentMenuId?: number | null;
  menuName?: string | null;
}

export const defaultValue: Readonly<IAurUserMenus> = {};
