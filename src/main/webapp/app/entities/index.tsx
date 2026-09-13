import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Depo from './depo';
import AurMenu from './aur-menu';
import AurCompany from './aur-company';
import AurRole from './aur-role';
import AurMenuRoleRel from './aur-menu-role-rel';
import AurUser from './aur-user';
import AurUserRoleRel from './aur-user-role-rel';

/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}depo`} component={Depo} />
      <ErrorBoundaryRoute path={`${match.url}aur-menu`} component={AurMenu} />
      <ErrorBoundaryRoute path={`${match.url}aur-company`} component={AurCompany} />
      <ErrorBoundaryRoute path={`${match.url}aur-role`} component={AurRole} />
      <ErrorBoundaryRoute path={`${match.url}aur-menu-role-rel`} component={AurMenuRoleRel} />
      <ErrorBoundaryRoute path={`${match.url}aur-user`} component={AurUser} />
      <ErrorBoundaryRoute path={`${match.url}aur-user-role-rel`} component={AurUserRoleRel} />

      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
