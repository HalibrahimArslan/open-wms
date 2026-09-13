import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AurUserRoleRel from './aur-user-role-rel';
import AurUserRoleRelDetail from './aur-user-role-rel-detail';
import AurUserRoleRelUpdate from './aur-user-role-rel-update';
import AurUserRoleRelDeleteDialog from './aur-user-role-rel-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AurUserRoleRelUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AurUserRoleRelUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AurUserRoleRelDetail} />
      <ErrorBoundaryRoute path={match.url} component={AurUserRoleRel} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AurUserRoleRelDeleteDialog} />
  </>
);

export default Routes;
