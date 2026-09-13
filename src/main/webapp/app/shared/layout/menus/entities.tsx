import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/depo">
      <Translate contentKey="global.menu.entities.warehouse" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/aur-menu">
      <Translate contentKey="global.menu.entities.aurMenu" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/aur-company">
      <Translate contentKey="global.menu.entities.aurCompany" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/aur-role">
      <Translate contentKey="global.menu.entities.aurRole" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/aur-menu-role-rel">
      <Translate contentKey="global.menu.entities.aurMenuRoleRel" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/aur-user">
      <Translate contentKey="global.menu.entities.aurUser" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/aur-user-role-rel">
      <Translate contentKey="global.menu.entities.aurUserRoleRel" />
    </MenuItem>

    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
