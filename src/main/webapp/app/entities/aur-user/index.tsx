import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AurUser from './aur-user';
import AurUserDetail from './aur-user-detail';
import AurUserUpdate from './aur-user-update';
import AurUserDeleteDialog from './aur-user-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AurUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AurUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AurUserDetail} />
      <ErrorBoundaryRoute path={match.url} component={AurUser} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AurUserDeleteDialog} />
  </>
);

export default Routes;
