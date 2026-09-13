import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AurMenu from './aur-menu';
import AurMenuDetail from './aur-menu-detail';
import AurMenuUpdate from './aur-menu-update';
import AurMenuDeleteDialog from './aur-menu-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AurMenuUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AurMenuUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AurMenuDetail} />
      <ErrorBoundaryRoute path={match.url} component={AurMenu} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AurMenuDeleteDialog} />
  </>
);

export default Routes;
