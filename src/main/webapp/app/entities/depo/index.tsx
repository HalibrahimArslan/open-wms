import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Depo from './depo';
import DepoDetail from './depo-detail';
import DepoUpdate from './depo-update';
import DepoDeleteDialog from './depo-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DepoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DepoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DepoDetail} />
      <ErrorBoundaryRoute path={match.url} component={Depo} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DepoDeleteDialog} />
  </>
);

export default Routes;
