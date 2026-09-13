import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from '../../shared/error/error-boundary-route';

import AurMenuRoleRel from './aur-menu-role-rel';
import AurMenuRoleRelDetail from './aur-menu-role-rel-detail';
import AurMenuRoleRelUpdate from './aur-menu-role-rel-update';
import AurMenuRoleRelDeleteDialog from './aur-menu-role-rel-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AurMenuRoleRelUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AurMenuRoleRelUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AurMenuRoleRelDetail} />
      <ErrorBoundaryRoute path={match.url} component={AurMenuRoleRel} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AurMenuRoleRelDeleteDialog} />
  </>
);

export default Routes;
