export interface IAurMenu {
  id?: number;
  menuId?: number | null;
  parentMenuId?: number | null;
  menuName?: string | null;
  menuType?: string | null;
  companyCode?: number | null;
}

export const defaultValue: Readonly<IAurMenu> = {};
