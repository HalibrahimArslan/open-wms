import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AurRole from './aur-role';
import AurRoleDetail from './aur-role-detail';
import AurRoleUpdate from './aur-role-update';
import AurRoleDeleteDialog from './aur-role-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AurRoleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AurRoleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AurRoleDetail} />
      <ErrorBoundaryRoute path={match.url} component={AurRole} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AurRoleDeleteDialog} />
  </>
);

export default Routes;
