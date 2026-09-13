import React from 'react';
import { NavDropdown } from './menu-components';
import { Translate, translate } from 'react-jhipster';
import MenuItem from 'app/shared/layout/menus/menu-item';

const openAPIItem = (
  <MenuItem icon="book" to="/admin/docs">
    <Translate contentKey="global.menu.admin.apidocs"> API </Translate>
  </MenuItem>
);

export const APIMenu = () => (
  <NavDropdown icon="book" name="Entegrasyon">
    {openAPIItem}
  </NavDropdown>
);

export default APIMenu;
